# Card AUD-03 — Plano de Auditoria

## Identificacao
| Atributo        | Valor                                   |
|:----------------|:----------------------------------------|
| Modulo          | Auditoria                               |
| Fase Roadmap    | Fase 3                                  |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 9.2   |
| Ordem no modulo | 003 de 009                              |
| Depende de      | AUD-02                                  |

## Tabela
| Atributo  | Valor               |
|:----------|:--------------------|
| Nome      | `THGQMGAUDPLAN`     |
| Instancia | `ThgAudPlano`        |
| Sequencia | AUTO (`CODPLAN`)    |
| Dual-DB   | Oracle + SQL Server |

## Campos
| # | Rotulo         | Coluna         | Tipo      | Tam | Obrig | Opcoes                                                   |
|:--|:---------------|:---------------|:----------|:----|:------|:---------------------------------------------------------|
| 1 | Id Plano       | `CODPLAN`      | INTEIRO   | -   | PK    | readOnly, auto sequence                                  |
| 2 | Auditoria      | `CODAUD`  | INTEIRO   | -   | Sim   | readOnly. FK para THGQMGAUD                      |
| 3 | Atividade      | `ATIVIDADE`    | TEXTO     | 300 | Sim   | O que sera feito neste item do plano                     |
| 4 | Responsavel    | `CODPARC`      | PESQUISA  | -   | Nao   | targetInstance=Parceiro. Auditado responsavel            |
| 5 | Horario Inicio | `DTINICIO`     | DATA_HORA | -   | Nao   | Horario previsto de inicio da atividade                  |
| 6 | Horario Fim    | `DTFIM`        | DATA_HORA | -   | Nao   | Horario previsto de termino                              |
| 7 | Clausulas      | `CLAUSULAS`    | TEXTO     | 100 | Nao   | Clausulas ISO a verificar. Ex: 8.4, 10.2                 |
| 8 | Local          | `LOCAL`        | TEXTO     | 200 | Nao   | Local fisico onde a atividade ocorrera                   |
| 9 | Criado por     | `CODUSU`       | PESQUISA  | -   | Nao   | readOnly. Auto: $ctx_usuario_logado. visivel=N           |

## Artefatos
- [ ] `datadictionary/THGQMGAUDPLAN.xml`
- [ ] `dbscripts/V1.xml` — DDL THGQMGAUDPLAN
