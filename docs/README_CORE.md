# Modulo Core

Tabelas transversais que suportam todos os modulos do QualityManager.

## Tabelas

### THGQMGCFG — Configuracao do Addon
| Campo | Tipo | Descricao |
|:------|:-----|:----------|
| CODCFG | PK AUTO | Cod. configuracao |
| CODEMP | PESQUISA | Empresa (UNIQUE - uma config por empresa) |
| MODNCATIVO | CHECKBOX | Modulo RNC ativo (default S) |
| MODFORNATIVO | CHECKBOX | Modulo Fornecedores ativo (default N) |
| MODDOCATIVO | CHECKBOX | Modulo Documentos ativo (default N) |
| MODGMATIVO | CHECKBOX | Modulo Gestao Mudancas ativo (default N) |
| MODAUDATIVO | CHECKBOX | Modulo Auditoria ativo (default N) |
| MODINDATIVO | CHECKBOX | Modulo Indicadores ativo (default N) |
| EMAILNOTIF | TEXTO(100) | E-mail para alertas gerais |
| DIASAVISO_DOC | INTEIRO | Dias aviso doc a vencer (default 30) |
| DIASRESP_FORN | INTEIRO | Prazo resposta fornecedor (default 10) |

**Instance:** `QmConfig`

### THGQMGLOG — Log de Auditoria (INSERT ONLY)
| Campo | Tipo | Descricao |
|:------|:-----|:----------|
| CODLOG | PK AUTO | Cod. log |
| DTREGISTRO | DATA_HORA | Data/hora do evento |
| MODULO | LISTA | NC, FORN, DOC, GM, AUD, CORE |
| ENTIDADE | TEXTO(50) | Tabela afetada |
| IDENTIDADE | INTEIRO | PK do registro afetado |
| ACAO | TEXTO(40) | INSERT, UPDATE_STATUS, AVANCAR_FASE, etc |
| CAMPO_ALTERADO | TEXTO(60) | Coluna que mudou |
| VALOR_ANTERIOR | TEXTO(2000) | Valor antes |
| VALOR_NOVO | TEXTO(2000) | Valor depois |
| DESCRICAO | TEXTO(500) | Contexto legivel |

**Instance:** `QmLogAuditoria` — somente leitura, INSERT via `AuditLogService`

### THGQMGPRIO — Prioridades (Dominio)
| CODPRIO | DESCRICAO | PRAZODIAS |
|:--------|:----------|:----------|
| 1 | Simples | 30 dias |
| 2 | Prioritario | 15 dias |
| 3 | Critico | 5 dias |

**Instance:** `QmDomPrioridade` — sequenceType=M (manual)

### THGQMGNCFASE — Fases RNC (Dominio)
10 fases do workflow RNC: Registro, Acoes Imediatas, Causa Raiz, Abrangencia, Acoes Corretivas, Revisao Documentos, Riscos/Oportunidades, Implementacao, Liberacao Produto, Verificacao Eficacia.

**Instance:** `QmRncFase` — sequenceType=M (manual), fases desativaveis (ATIVO=N)

## Services

### AuditLogService
Servico central de audit log. Metodo `registrar(modulo, entidade, idEntidade, acao, campo, valorAnt, valorNovo, descricao)`. Deve ser injetado em todos os BusinessService do produto.

## Artefatos
- dbscripts: V001-V006
- datadictionary: THGQMGCFG, THGQMGLOG, THGQMGPRIO, THGQMGNCFASE
- Java: 4 entities, 4 repositories, 1 service (AuditLogService)
