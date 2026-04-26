package br.com.hagious.qualitymanager.core.service;

import br.com.hagious.qualitymanager.core.entity.LogAuditoria;
import br.com.hagious.qualitymanager.core.repository.LogAuditoriaRepository;
import br.com.sankhya.studio.stereotypes.Component;
import lombok.Log;

import javax.inject.Inject;
import java.math.BigDecimal;

@Log
@Component
public class AuditLogService {

    private final LogAuditoriaRepository logAuditoriaRepository;

    @Inject
    public AuditLogService(LogAuditoriaRepository logAuditoriaRepository) {
        this.logAuditoriaRepository = logAuditoriaRepository;
    }

    public void registrar(String modulo, String entidade, BigDecimal idEntidade,
                          String acao, String campoAlterado, String valorAnterior,
                          String valorNovo, String descricao) {
        LogAuditoria logAuditoria = new LogAuditoria();
        logAuditoria.setModulo(modulo);
        logAuditoria.setEntidade(entidade);
        logAuditoria.setIdEntidade(idEntidade);
        logAuditoria.setAcao(acao);
        logAuditoria.setCampoAlterado(campoAlterado);
        logAuditoria.setValorAnterior(valorAnterior);
        logAuditoria.setValorNovo(valorNovo);
        logAuditoria.setDescricao(descricao);

        logAuditoriaRepository.save(logAuditoria);
        log.info("Audit log: " + modulo + " | " + entidade + " | " + acao);
    }
}
