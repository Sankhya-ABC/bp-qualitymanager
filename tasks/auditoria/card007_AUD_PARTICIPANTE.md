# Card AUD-07 — Participantes das Reunioes

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Auditoria                                |
| Fase Roadmap    | Fase 3                                   |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 9.2.2  |
| Ordem no modulo | 007 de 009                               |
| Depende de      | AUD-02                                   |

## Tabela
| Atributo  | Valor                     |
|:----------|:--------------------------|
| Nome      | `THGQMGAUDPART`    |
| Instancia | `QmAudParticipante`       |
| Sequencia | AUTO (`CODPART`)   |
| Dual-DB   | Oracle + SQL Server       |

## Campos
| # | Rotulo       | Coluna            | Tipo      | Tam | Obrig | Opcoes                                           |
|:--|:-------------|:------------------|:----------|:----|:------|:-------------------------------------------------|
| 1 | Id           | `CODPART`  | INTEIRO   | -   | PK    | readOnly, auto sequence                          |
| 2 | Auditoria    | `CODAUD`     | INTEIRO   | -   | Sim   | readOnly. FK para THGQMGAUD              |
| 3 | Tipo Reuniao | `TIPOREUNIAO`     | LISTA     | 1   | Sim   | A=Abertura, E=Encerramento                       |
| 4 | Parceiro     | `CODPARC`         | PESQUISA  | -   | Sim   | targetInstance=Parceiro. Participante            |
| 5 | Funcao       | `FUNCAO`          | TEXTO     | 100 | Nao   | Papel na reuniao. Ex: Responsavel do processo    |
| 6 | Presenca     | `PRESENCA`        | CHECKBOX  | 1   | Nao   | S=Confirmou presenca. Default S                  |
| 7 | Data         | `DHCREATE`       | DATA_HORA | -   | Nao   | readOnly. Auto: $ctx_dh_atual. visivel=N         |

## Regras de Negocio
1. Reuniao de abertura (A) e encerramento (E) devem ter participantes — evidencia ISO obrigatoria
2. Evidencia de que auditados foram comunicados (abertura) e informados dos resultados (encerramento)

## Artefatos
- [ ] `datadictionary/THGQMGAUDPART.xml`
- [ ] `dbscripts/V1.xml` — DDL THGQMGAUDPART
