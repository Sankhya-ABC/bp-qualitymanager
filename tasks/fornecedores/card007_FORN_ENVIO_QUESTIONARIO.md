# Card FORN-07 — Envio de Questionario por E-mail (Java)

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Fornecedores                             |
| Fase Roadmap    | Fase 2                                   |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 8.4.1  |
| Ordem no modulo | 007 de 009                               |
| Depende de      | FORN-01, FORN-05, FORN-08 (campo nativo) |

---

## Descricao

Card de logica Java — nao cria tela. Cria o service responsavel por gerar a URL tokenizada
e enviar o questionario ao fornecedor via e-mail atraves do MSDFilaMensagem do Sankhya.

---

## Classe Java — QuestionarioFornService.java

| Metodo               | Parametros                               | O que faz                                                            |
|:---------------------|:-----------------------------------------|:---------------------------------------------------------------------|
| `enviarQuestionario` | idQuestionario, codParc, idQualificacao  | Gera URL tokenizada Base64 e insere e-mail em MSDFilaMensagem        |
| `enviarNotificacao`  | codParc, assunto, mensagemHtml           | Envia notificacao generica via MSDFilaMensagem usando EMAIL de TGFPAR|

---

## Logica de geracao da URL tokenizada

```java
String emailBase64     = Base64.encode(TGFPAR.QM_EMAILQUESTIONARIO);
String codQuestBase64  = Base64.encode(String.valueOf(idQuestionario));
String codQualifBase64 = Base64.encode(String.valueOf(idQualificacao));
String loginBase64     = Base64.encode(param("LOGINQLD"));
String senhaBase64     = Base64.encode(param("PSWQLD"));

String auth = emailBase64 + "?" + codQualifBase64 + "?"
            + codQuestBase64 + "?" + loginBase64 + "?" + senhaBase64 + "?";

String urlFinal = param("URLQUALIDADE") + "?" + auth;
```

---

## Parametros do sistema necessarios (parameter.xml)

| Parametro         | Descricao                                      | Obrigatorio |
|:------------------|:-----------------------------------------------|:------------|
| `LOGINQLD`        | Login para autenticacao no sistema externo     | Sim         |
| `PSWQLD`          | Senha para autenticacao                        | Sim         |
| `URLQUALIDADE`    | URL base do sistema externo de questionario    | Sim         |
| `NOMEEMPQLF`      | Nome da empresa para personalizar e-mails      | Nao         |
| `HTMLEMAILQUEST`  | Template HTML com placeholders {URL} e {EMPRESA}| Sim        |
| `HTMLRNCFORNEC`   | Template HTML de NC ao fornecedor com {RNC}, {DETALHAMENTO}, {EMPRESA} | Nao |

---

## Artefatos

### Backend Java
- [ ] `model/.../services/QuestionarioFornService.java`
- [ ] `model/src/main/resources/META-INF/parameter.xml` — declarar os 6 parametros acima

---

## Observacoes Tecnicas

- QM_EMAILQUESTIONARIO e o e-mail especifico para qualidade — diferente do EMAIL padrao de TGFPAR
- Se QM_EMAILQUESTIONARIO estiver vazio, usar EMAIL padrao de TGFPAR como fallback
- Log obrigatorio: `[QM-FORN] Questionario {id} enviado para {email} do parceiro {codParc}`
