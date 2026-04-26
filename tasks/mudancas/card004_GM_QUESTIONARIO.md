# Card GM-04 — Questionario de Avaliacao de Impacto

## Identificacao
| Atributo        | Valor                                   |
|:----------------|:----------------------------------------|
| Modulo          | Gestao de Mudancas                      |
| Fase Roadmap    | Fase 3                                  |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 6.3b  |
| Ordem no modulo | 004 de 008                              |
| Depende de      | GM-01, GM-02, FORN-05 (reutiliza estrutura) |

## Tabelas
| Tabela                  | Instancia           | Sequencia       | Descricao                             |
|:------------------------|:--------------------|:----------------|:--------------------------------------|
| `THGQMGGMQUEST`   | `QmGmQuestionario`  | `CODAVAL`   | Questionario de avaliacao de impacto  |
| `THGQMGGMPERG`       | `QmGmPergunta`      | `CODPERGGM` | Perguntas do questionario             |

## Campos — THGQMGGMQUEST
| # | Rotulo        | Coluna        | Tipo      | Tam | Obrig | Opcoes                                         |
|:--|:--------------|:--------------|:----------|:----|:------|:-----------------------------------------------|
| 1 | Id            | `CODAVAL` | INTEIRO   | -   | PK    | readOnly, auto sequence                        |
| 2 | Gestao        | `CODGM`    | INTEIRO   | -   | Sim   | readOnly. FK para THGQMGGM                |
| 3 | Questionario  | `CODQUEST`     | INTEIRO   | -   | Sim   | FK para THGQMGFQUEST (ORIGEM != 1)    |
| 4 | Fase Origem   | `ORIGEM`      | INTEIRO   | -   | Sim   | Fase que criou: 2, 4 ou 5                      |
| 5 | Criado em     | `DHCREATE`   | DATA_HORA | -   | Nao   | readOnly. Auto: $ctx_dh_atual                  |

## Regras de Negocio
1. Questionarios criados AUTOMATICAMENTE pelo FaseGmBusinessService ao avancar para fases 2, 4 ou 5
2. Reutiliza THGQMGFQUEST com ORIGEM != 1 (nao e qualificacao de fornecedor)
3. Nao criado manualmente — apenas visualizado e respondido pelo usuario

## Artefatos
- [ ] `datadictionary/THGQMGGMQUEST.xml`
- [ ] `datadictionary/THGQMGGMPERG.xml`
- [ ] `dbscripts/V1.xml` — DDL das 2 tabelas
- [ ] `model/.../services/FaseGmBusinessService.java` — metodo criaQuestionariosGestao()
