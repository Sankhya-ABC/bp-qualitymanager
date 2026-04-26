# Card AUD-04 — Checklist de Verificacao ISO

## Identificacao
| Atributo        | Valor                                   |
|:----------------|:----------------------------------------|
| Modulo          | Auditoria                               |
| Fase Roadmap    | Fase 3                                  |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 9.2   |
| Ordem no modulo | 004 de 009                              |
| Depende de      | AUD-02                                  |

## Tabela
| Atributo  | Valor                  |
|:----------|:-----------------------|
| Nome      | `THGQMGAUDCHK`    |
| Instancia | `QmAudChecklist`       |
| Sequencia | AUTO (`CODCHK`)   |
| Dual-DB   | Oracle + SQL Server    |

## Campos
| # | Rotulo       | Coluna          | Tipo      | Tam | Obrig | Opcoes                                                                      |
|:--|:-------------|:----------------|:----------|:----|:------|:----------------------------------------------------------------------------|
| 1 | Id           | `CODCHK`   | INTEIRO   | -   | PK    | readOnly, auto sequence                                                     |
| 2 | Auditoria    | `CODAUD`   | INTEIRO   | -   | Sim   | readOnly. FK para THGQMGAUD                                         |
| 3 | Norma        | `NORMAISO`      | LISTA     | 4   | Sim   | 9001=ISO 9001, 1400=ISO 14001, 1702=ISO 17025                               |
| 4 | Clausula     | `CLAUSULA`      | TEXTO     | 20  | Sim   | Clausula ISO. Ex: 8.7, 10.2.1                                               |
| 5 | Pergunta     | `PERGUNTA`      | TEXTO     | 500 | Sim   | isPresentation=S. Pergunta de verificacao                                   |
| 6 | Resultado    | `RESULTADO`     | LISTA     | 2   | Nao   | CF=Conforme, NM=NC Menor, MA=NC Maior, OB=Observacao, NA=Nao aplicavel     |
| 7 | Evidencia    | `EVIDENCIA`     | TEXTO     | 500 | Nao   | Evidencia coletada para este item                                           |
| 8 | Auditado por | `CODUSU`        | PESQUISA  | -   | Nao   | readOnly. Auto: $ctx_usuario_logado. visivel=N                              |
| 9 | Respondido em| `DHRESPOSTA`    | DATA_HORA | -   | Nao   | readOnly. Preenchido ao responder                                           |

## Dados Iniciais
Checklist padrao ISO 9001:2015 inserido via V1.xml com perguntas de auditoria
para clausulas 4.1 a 10.3 — facilita auditor interno iniciante.

## Artefatos
- [ ] `datadictionary/THGQMGAUDCHK.xml`
- [ ] `dbscripts/V1.xml` — DDL THGQMGAUDCHK + INSERT perguntas padrao ISO 9001
