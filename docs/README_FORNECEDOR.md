# Modulo Fornecedores

ISO 9001 clausula 8.4 (Controle de processos, produtos e servicos providos externamente).

## Funcionalidades

- Qualificacao de fornecedores com score IQF automatico
- Criterios de avaliacao com pesos configuraveis
- Documentos com controle de validade
- Avaliacoes periodicas com notas por criterio
- Questionarios enviados via URL tokenizada
- Bloqueio automatico por reincidencia de NCs

## Tabelas

| Tabela | Instance | Descricao |
|:-------|:---------|:----------|
| THGQMGQUAL | QmFornQualificacao | Qualificacao do fornecedor (master) |
| THGQMGCRIT | QmFornCriterio | Criterios de avaliacao (Qualidade 40%, Tecnico 30%, Prazo 20%, Seguranca 10%) |
| THGQMGFORNDOC | QmFornDocumento | Certificados e documentos do fornecedor |
| THGQMGFAVAL | QmFornAvaliacao | Avaliacoes periodicas com notas |
| THGQMGFQUEST | QmFornQuestionario | Questionarios de qualificacao |
| THGQMGFPERG | QmFornPergunta | Perguntas do questionario |
| THGQMGFRESP | QmFornResposta | Respostas do fornecedor |

**Extensao nativa:** TGFPAR + campo `QM_EMAILQUESTIONARIO` (e-mail especifico qualidade)

## Score IQF (Indice de Qualificacao de Fornecedor)

| Resultado | Pontuacao | STATUS |
|:----------|:----------|:-------|
| Aprovado | >= 80% | A |
| Condicional | 50-79% | B |
| Reprovado | < 50% | T |

**Calculo:** Score recalculado automaticamente via `QualificacaoScoreListener` a cada resposta inserida.

**Regras de pontuacao:**
- Sim/Nao: SIM=1.0, NAO=0.0
- Numerica (0-100): <10=0.15, <30=0.30, <50=0.45, <70=0.60, <90=0.75, >=90=0.90

## Services

### ScoreFornBusinessService
- `calcularScore(idQualificacao)` — calcula pontuacao com BigDecimal HALF_UP
- `classificarResultado(pontuacao)` — A/B/T conforme faixas
- `atualizarQualificacao(idQualificacao)` — persiste score + resultado + status

### QuestionarioFornService
- `gerarUrlQuestionario(...)` — URL com tokens Base64 pra sistema externo

## Artefatos
- dbscripts: V021-V028 (8 scripts)
- datadictionary: 8 XMLs + TGFPAR nativa
- Java: 7 entities, 7 repositories, 2 services, 1 listener
