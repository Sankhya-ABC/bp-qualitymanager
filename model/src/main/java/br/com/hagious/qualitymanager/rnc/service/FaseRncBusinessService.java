package br.com.hagious.qualitymanager.rnc.service;

import br.com.hagious.qualitymanager.core.entity.FaseRnc;
import br.com.hagious.qualitymanager.core.repository.FaseRncRepository;
import br.com.hagious.qualitymanager.core.service.AuditLogService;
import br.com.hagious.qualitymanager.rnc.entity.RegistroFase;
import br.com.hagious.qualitymanager.rnc.entity.RegistroRnc;
import br.com.hagious.qualitymanager.rnc.exception.FaseInvalidaException;
import br.com.hagious.qualitymanager.rnc.repository.RegistroFaseRepository;
import br.com.hagious.qualitymanager.rnc.repository.RegistroRncRepository;
import br.com.sankhya.studio.stereotypes.Component;
import lombok.Log;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Log
@Component
public class FaseRncBusinessService {

    private final RegistroRncRepository registroRncRepository;
    private final RegistroFaseRepository registroFaseRepository;
    private final FaseRncRepository faseRncRepository;
    private final AuditLogService auditLogService;

    @Inject
    public FaseRncBusinessService(RegistroRncRepository registroRncRepository,
                                  RegistroFaseRepository registroFaseRepository,
                                  FaseRncRepository faseRncRepository,
                                  AuditLogService auditLogService) {
        this.registroRncRepository = registroRncRepository;
        this.registroFaseRepository = registroFaseRepository;
        this.faseRncRepository = faseRncRepository;
        this.auditLogService = auditLogService;
    }

    public void avancarFase(BigDecimal rncId, String origem) throws FaseInvalidaException {
        Optional<RegistroRnc> optRnc = registroRncRepository.findByPK(rncId);
        if (!optRnc.isPresent()) {
            throw new FaseInvalidaException("RNC nao encontrada: " + rncId);
        }

        RegistroRnc rnc = optRnc.get();
        if ("C".equals(rnc.getStatus())) {
            throw new FaseInvalidaException("RNC cancelada nao pode avancar de fase");
        }

        BigDecimal faseAnterior = rnc.getCodFase();
        BigDecimal proximaFase = calcularProximaFase(faseAnterior, origem);

        rnc.setCodFase(proximaFase);
        if (proximaFase.intValue() > 1) {
            rnc.setStatus("P");
        }

        registroRncRepository.save(rnc);
        registrarTransicaoFase(rncId, faseAnterior, proximaFase);

        auditLogService.registrar("NC", "THGQMGREG", rncId, "AVANCAR_FASE",
                "CODFASE", faseAnterior.toString(), proximaFase.toString(),
                "Avanco de fase " + faseAnterior + " para " + proximaFase);

        log.info("[QM-NC] RNC " + rncId + " avancou fase " + faseAnterior + " -> " + proximaFase);
    }

    private BigDecimal calcularProximaFase(BigDecimal faseAtual, String origem) {
        int atual = faseAtual.intValue();

        // Desvio de fluxo: Riscos/Oportunidades pula fase 4 (Abrangencia) -> vai direto pra 5
        if (atual == 3 && ("A".equals(origem) || "O".equals(origem))) {
            return new BigDecimal(5);
        }

        // Busca proxima fase ativa apos a atual
        List<FaseRnc> todasFases = faseRncRepository.findAll();
        BigDecimal proximaFase = faseAtual.add(BigDecimal.ONE);

        for (int candidata = atual + 1; candidata <= 10; candidata++) {
            boolean ativa = true;
            for (FaseRnc fase : todasFases) {
                if (fase.getCodFase().intValue() == candidata && "N".equals(fase.getAtivo())) {
                    ativa = false;
                    break;
                }
            }
            if (ativa) {
                proximaFase = new BigDecimal(candidata);
                break;
            }
        }

        return proximaFase;
    }

    public void retornarFase(BigDecimal rncId) throws FaseInvalidaException {
        Optional<RegistroRnc> optRnc = registroRncRepository.findByPK(rncId);
        if (!optRnc.isPresent()) {
            throw new FaseInvalidaException("RNC nao encontrada: " + rncId);
        }

        RegistroRnc rnc = optRnc.get();

        if ("C".equals(rnc.getStatus())) {
            throw new FaseInvalidaException("RNC cancelada nao pode retornar de fase");
        }

        BigDecimal faseAtual = rnc.getCodFase();
        if (faseAtual.intValue() <= 1) {
            throw new FaseInvalidaException("Fase 1 nao pode retornar");
        }

        BigDecimal faseAnterior = faseAtual;
        BigDecimal novaFase = faseAtual.subtract(BigDecimal.ONE);
        rnc.setCodFase(novaFase);
        registroRncRepository.save(rnc);
        registrarTransicaoFase(rncId, faseAnterior, novaFase);

        auditLogService.registrar("NC", "THGQMGREG", rncId, "RETORNAR_FASE",
                "CODFASE", faseAnterior.toString(), novaFase.toString(),
                "Retorno de fase " + faseAnterior + " para " + novaFase);

        log.info("[QM-NC] RNC " + rncId + " retornou fase " + faseAnterior + " -> " + novaFase);
    }

    public void cancelarRnc(BigDecimal rncId) throws FaseInvalidaException {
        Optional<RegistroRnc> optRnc = registroRncRepository.findByPK(rncId);
        if (!optRnc.isPresent()) {
            throw new FaseInvalidaException("RNC nao encontrada: " + rncId);
        }

        RegistroRnc rnc = optRnc.get();
        String statusAnterior = rnc.getStatus();
        rnc.setStatus("C");
        rnc.setDtEncerramento(new Timestamp(System.currentTimeMillis()));
        registroRncRepository.save(rnc);

        auditLogService.registrar("NC", "THGQMGREG", rncId, "CANCELAR",
                "STATUS", statusAnterior, "C", "RNC cancelada");

        log.info("[QM-NC] RNC " + rncId + " cancelada");
    }

    public void encerrarRnc(BigDecimal rncId) throws FaseInvalidaException {
        Optional<RegistroRnc> optRnc = registroRncRepository.findByPK(rncId);
        if (!optRnc.isPresent()) {
            throw new FaseInvalidaException("RNC nao encontrada: " + rncId);
        }

        RegistroRnc rnc = optRnc.get();
        String statusAnterior = rnc.getStatus();
        rnc.setStatus("E");
        rnc.setDtEncerramento(new Timestamp(System.currentTimeMillis()));
        registroRncRepository.save(rnc);

        auditLogService.registrar("NC", "THGQMGREG", rncId, "ENCERRAR",
                "STATUS", statusAnterior, "E", "RNC encerrada com eficacia verificada");

        log.info("[QM-NC] RNC " + rncId + " encerrada com eficacia");
    }

    public void reabrirParaFase5(BigDecimal rncId) throws FaseInvalidaException {
        Optional<RegistroRnc> optRnc = registroRncRepository.findByPK(rncId);
        if (!optRnc.isPresent()) {
            throw new FaseInvalidaException("RNC nao encontrada: " + rncId);
        }

        RegistroRnc rnc = optRnc.get();
        BigDecimal faseAnterior = rnc.getCodFase();
        BigDecimal fase5 = new BigDecimal(5);

        rnc.setCodFase(fase5);
        rnc.setStatus("P");
        registroRncRepository.save(rnc);
        registrarTransicaoFase(rncId, faseAnterior, fase5);

        auditLogService.registrar("NC", "THGQMGREG", rncId, "REABRIR_INEFICAZ",
                "CODFASE", faseAnterior.toString(), "5",
                "RNC reaberta para fase 5 - verificacao ineficaz");

        log.info("[QM-NC] RNC " + rncId + " reaberta para fase 5 (ineficaz)");
    }

    private void registrarTransicaoFase(BigDecimal rncId, BigDecimal faseDe, BigDecimal fasePara) {
        RegistroFase regFase = new RegistroFase();
        regFase.setCodRnc(rncId);
        regFase.setCodFaseDe(faseDe);
        regFase.setCodFasePara(fasePara);
        registroFaseRepository.save(regFase);
    }
}
