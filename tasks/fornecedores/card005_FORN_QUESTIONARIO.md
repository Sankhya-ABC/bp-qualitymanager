# Card FORN-05 — Questionario de Qualificacao

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Fornecedores                             |
| Fase Roadmap    | Fase 2                                   |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 8.4.1  |
| Ordem no modulo | 005 de 009                               |
| Depende de      | FORN-01                                  |

---

## Tabelas

| Tabela                    | Instancia            | Sequencia         | Descricao                         |
|:--------------------------|:---------------------|:------------------|:----------------------------------|
| `THGQMGFQUEST`   | `ThgFornQuestionario` | `CODQUEST`  | Cabecalho do questionario         |
| `THGQMGFPERG`       | `ThgFornPergunta`     | `CODPERG`      | Perguntas do questionario         |
| `THGQMGFRESP`       | `ThgFornResposta`     | `CODRESPTA`      | Respostas do fornecedor           |

---

## Campos — THGQMGFQUEST

| # | Rotulo      | Coluna            | Tipo     | Tam | Obrig | Opcoes / Comportamento                                     |
|:--|:------------|:------------------|:---------|:----|:------|:-----------------------------------------------------------|
| 1 | Id          | `CODQUEST`  | INTEIRO  | -   | PK    | readOnly, auto sequence                                    |
| 2 | Descricao   | `DESCRICAO`       | TEXTO    | 200 | Sim   | isPresentation=S. Nome do questionario                     |
| 3 | Origem      | `ORIGEM`          | LISTA    | 1   | Sim   | 1=Qualificacao inicial, 2=Reavaliacao, 3=Auditoria fornecedor |
| 4 | Ativo       | `ATIVO`           | CHECKBOX | 1   | Nao   | Default S                                                  |

## Campos — THGQMGFPERG

| # | Rotulo        | Coluna            | Tipo     | Tam | Obrig | Opcoes / Comportamento                                    |
|:--|:--------------|:------------------|:---------|:----|:------|:----------------------------------------------------------|
| 1 | Id            | `CODPERG`      | INTEIRO  | -   | PK    | readOnly, auto sequence                                   |
| 2 | Questionario  | `CODQUEST`  | INTEIRO  | -   | Sim   | readOnly. FK para THGQMGFQUEST                   |
| 3 | Pergunta      | `PERGUNTA`        | TEXTO    | 500 | Sim   | isPresentation=S. Texto da pergunta                       |
| 4 | Tipo Resposta | `TIPORESPOSTA`    | LISTA    | 1   | Sim   | S=Sim/Nao, N=Numerica (0-100), T=Texto livre              |
| 5 | Ordem         | `ORDEM`           | INTEIRO  | -   | Nao   | Sequencia de exibicao no questionario                     |

## Campos — THGQMGFRESP

| # | Rotulo       | Coluna           | Tipo     | Tam | Obrig | Opcoes / Comportamento                                    |
|:--|:-------------|:-----------------|:---------|:----|:------|:----------------------------------------------------------|
| 1 | Id           | `CODRESPTA`     | INTEIRO  | -   | PK    | readOnly, auto sequence                                   |
| 2 | Qualificacao | `CODQUAL` | INTEIRO  | -   | Sim   | FK para THGQMGQUAL                             |
| 3 | Pergunta     | `CODPERG`     | INTEIRO  | -   | Sim   | FK para THGQMGFPERG                                 |
| 4 | Resposta     | `RESPOSTA`       | TEXTO    | 200 | Sim   | Valor: SIM, NAO ou valor numerico 0-100                   |

---

## Regras de Negocio

1. Questionario enviado ao fornecedor via URL tokenizada Base64 (Card FORN-07)
2. Listener QualificacaoScoreListener acionado apos INSERT em THGQMGFRESP
3. Calculo: SIM=1 ponto. Numerica: <10=0.15, 10-29=0.30, 30-49=0.45, 50-69=0.60, 70-89=0.75, >=90=0.90
4. Score = (somaPontos / totalPerguntas) * 100 com BigDecimal HALF_UP escala 2
5. instanceName do listener: `ThgFornResposta` — copiar EXATAMENTE do XML da tabela de respostas

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL das 3 tabelas, triggers BI, sequences

### Dicionario de Dados
- [ ] `datadictionary/THGQMGFQUEST.xml`
- [ ] `datadictionary/THGQMGFPERG.xml`
- [ ] `datadictionary/THGQMGFRESP.xml`

### Backend Java
- [ ] `model/.../listeners/QualificacaoScoreListener.java` — instanceName: `ThgFornResposta`
- [ ] `model/.../services/ScoreFornBusinessService.java`

### Menu
- [ ] `datadictionary/menu.xml` — ThgFornQuestionario em pasta Fornecedores
