package br.com.hagious.qualitymanager.fornecedor.service;

import br.com.sankhya.studio.stereotypes.Component;
import lombok.Log;

import java.math.BigDecimal;
import java.util.Base64;

@Log
@Component
public class QuestionarioFornService {

    public String gerarUrlQuestionario(BigDecimal idQuestionario, BigDecimal codParc,
                                        BigDecimal idQualificacao, String email,
                                        String urlBase, String login, String senha) {
        String emailBase64 = Base64.getEncoder().encodeToString(email.getBytes());
        String codQuestBase64 = Base64.getEncoder().encodeToString(idQuestionario.toString().getBytes());
        String codQualifBase64 = Base64.getEncoder().encodeToString(idQualificacao.toString().getBytes());
        String loginBase64 = Base64.getEncoder().encodeToString(login.getBytes());
        String senhaBase64 = Base64.getEncoder().encodeToString(senha.getBytes());

        String auth = emailBase64 + "?" + codQualifBase64 + "?"
                + codQuestBase64 + "?" + loginBase64 + "?" + senhaBase64 + "?";

        String urlFinal = urlBase + "?" + auth;

        log.info("[QM-FORN] URL questionario gerada para parceiro " + codParc);
        return urlFinal;
    }
}
