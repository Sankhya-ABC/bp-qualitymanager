package br.com.hagious.qualitymanager.fornecedor.service;

import br.com.hagious.qualitymanager.core.service.AuditLogService;
import br.com.hagious.qualitymanager.fornecedor.entity.Criterio;
import br.com.hagious.qualitymanager.fornecedor.entity.Pergunta;
import br.com.hagious.qualitymanager.fornecedor.entity.Qualificacao;
import br.com.hagious.qualitymanager.fornecedor.entity.Resposta;
import br.com.hagious.qualitymanager.fornecedor.repository.CriterioRepository;
import br.com.hagious.qualitymanager.fornecedor.repository.PerguntaRepository;
import br.com.hagious.qualitymanager.fornecedor.repository.QualificacaoRepository;
import br.com.hagious.qualitymanager.fornecedor.repository.RespostaRepository;
import br.com.sankhya.studio.stereotypes.Component;
import lombok.Log;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Log
@Component
public class ScoreFornBusinessService {

    private final QualificacaoRepository qualificacaoRepository;
    private final RespostaRepository respostaRepository;
    private final PerguntaRepository perguntaRepository;
    private final CriterioRepository criterioRepository;
    private final AuditLogService auditLogService;

    @Inject
    public ScoreFornBusinessService(QualificacaoRepository qualificacaoRepository,
                                     RespostaRepository respostaRepository,
                                     PerguntaRepository perguntaRepository,
                                     CriterioRepository criterioRepository,
                                     AuditLogService auditLogService) {
        this.qualificacaoRepository = qualificacaoRepository;
        this.respostaRepository = respostaRepository;
        this.perguntaRepository = perguntaRepository;
        this.criterioRepository = criterioRepository;
        this.auditLogService = auditLogService;
    }

    public BigDecimal calcularScore(BigDecimal idQualificacao) {
        List<Resposta> respostas = respostaRepository.findByQualificacao(idQualificacao);

        BigDecimal pontos = BigDecimal.ZERO;
        int totalPerguntas = 0;

        for (Resposta resposta : respostas) {
            totalPerguntas++;
            String tipoResposta = getTipoResposta(resposta.getCodPerg());

            if ("S".equals(tipoResposta)) {
                if ("SIM".equalsIgnoreCase(resposta.getResposta())) {
                    pontos = pontos.add(BigDecimal.ONE);
                }
            } else if ("N".equals(tipoResposta)) {
                int valor = Integer.parseInt(resposta.getResposta());
                if (valor < 10) {
                    pontos = pontos.add(new BigDecimal("0.15"));
                } else if (valor < 30) {
                    pontos = pontos.add(new BigDecimal("0.30"));
                } else if (valor < 50) {
                    pontos = pontos.add(new BigDecimal("0.45"));
                } else if (valor < 70) {
                    pontos = pontos.add(new BigDecimal("0.60"));
                } else if (valor < 90) {
                    pontos = pontos.add(new BigDecimal("0.75"));
                } else {
                    pontos = pontos.add(new BigDecimal("0.90"));
                }
            }
        }

        if (totalPerguntas == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal score = pontos.divide(new BigDecimal(totalPerguntas), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100))
                .setScale(2, RoundingMode.HALF_UP);

        log.info("[QM-FORN] Score calculado para qualificacao " + idQualificacao + ": " + score);
        return score;
    }

    public String classificarResultado(BigDecimal pontuacao) {
        if (pontuacao.compareTo(new BigDecimal(80)) >= 0) {
            return "A";
        } else if (pontuacao.compareTo(new BigDecimal(50)) >= 0) {
            return "B";
        } else {
            return "T";
        }
    }

    public void atualizarQualificacao(BigDecimal idQualificacao) {
        Optional<Qualificacao> optQual = qualificacaoRepository.findByPK(idQualificacao);
        if (!optQual.isPresent()) {
            log.warning("[QM-FORN] Qualificacao nao encontrada: " + idQualificacao);
            return;
        }

        Qualificacao qual = optQual.get();
        BigDecimal score = calcularScore(idQualificacao);
        String resultado = classificarResultado(score);
        String statusAnterior = qual.getStatus();

        qual.setPontuacao(score);
        qual.setResultadoIqf(resultado);
        qual.setStatus(resultado);
        qualificacaoRepository.save(qual);

        auditLogService.registrar("FORN", "THGQMGQUAL", idQualificacao, "CALCULO_SCORE",
                "PONTUACAO", statusAnterior, score.toString(),
                "Score recalculado: " + score + " - Resultado: " + resultado);

        log.info("[QM-FORN] Qualificacao " + idQualificacao + " atualizada: score=" + score + " resultado=" + resultado);
    }

    private String getTipoResposta(BigDecimal codPerg) {
        Optional<Pergunta> optPerg = perguntaRepository.findByPK(codPerg);
        if (optPerg.isPresent()) {
            return optPerg.get().getTipoResposta();
        }
        return "T";
    }
}
