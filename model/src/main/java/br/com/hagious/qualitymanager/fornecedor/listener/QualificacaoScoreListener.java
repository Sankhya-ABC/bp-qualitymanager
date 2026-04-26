package br.com.hagious.qualitymanager.fornecedor.listener;

import br.com.hagious.qualitymanager.fornecedor.entity.Resposta;
import br.com.hagious.qualitymanager.fornecedor.service.ScoreFornBusinessService;
import br.com.sankhya.studio.persistence.PersistenceEventAdapter;
import br.com.sankhya.studio.stereotypes.Component;
import lombok.Log;

import com.google.inject.Inject;

@Log
@Component
public class QualificacaoScoreListener extends PersistenceEventAdapter {

    private final ScoreFornBusinessService scoreFornBusinessService;

    @Inject
    public QualificacaoScoreListener(ScoreFornBusinessService scoreFornBusinessService) {
        this.scoreFornBusinessService = scoreFornBusinessService;
    }

    @Override
    public String getInstanceName() {
        return "ThgFornResposta";
    }

    @Override
    public void afterInsert(Object entity) {
        if (!(entity instanceof Resposta)) {
            return;
        }

        try {
            Resposta resposta = (Resposta) entity;
            scoreFornBusinessService.atualizarQualificacao(resposta.getCodQual());
        } catch (Exception e) {
            log.severe("[QM-FORN] Erro ao calcular score apos insercao de resposta: " + e.getMessage());
        }
    }
}
