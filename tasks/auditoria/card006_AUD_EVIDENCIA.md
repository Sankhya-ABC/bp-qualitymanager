# Card AUD-06 — Evidencias de Auditoria

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Auditoria                                |
| Fase Roadmap    | Fase 3                                   |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 9.2.2f |
| Ordem no modulo | 006 de 009                               |
| Depende de      | AUD-02                                   |

## Tabela
| Atributo  | Valor                  |
|:----------|:-----------------------|
| Nome      | `THGQMGAUDEVID`    |
| Instancia | `QmAudEvidencia`       |
| Sequencia | AUTO (`CODEVAUD`)     |
| Dual-DB   | Oracle + SQL Server    |

## Campos
| # | Rotulo          | Coluna           | Tipo      | Tam | Obrig | Opcoes                                          |
|:--|:----------------|:-----------------|:----------|:----|:------|:------------------------------------------------|
| 1 | Id              | `CODEVAUD`      | INTEIRO   | -   | PK    | readOnly, auto sequence                         |
| 2 | Auditoria       | `CODAUD`    | INTEIRO   | -   | Sim   | readOnly. FK para THGQMGAUD             |
| 3 | Descricao       | `DESCRICAO`      | TEXTO     | 300 | Nao   | Descricao da evidencia coletada                 |
| 4 | Arquivo         | `ARQUIVO`        | ARQUIVO   | -   | Nao   | dataType=ARQUIVO. Foto, PDF, print, etc.        |
| 5 | Tipo Evidencia  | `TIPOEVIDENCIA`  | LISTA     | 1   | Nao   | D=Documento, E=Entrevista, O=Observacao         |
| 6 | Coletado por    | `CODUSU`         | PESQUISA  | -   | Nao   | readOnly. Auto: $ctx_usuario_logado. visivel=N  |
| 7 | Coletado em     | `DHCREATE`      | DATA_HORA | -   | Nao   | readOnly. Auto: $ctx_dh_atual. visivel=N        |

## Artefatos
- [ ] `datadictionary/THGQMGAUDEVID.xml` — campo ARQUIVO com dataType=ARQUIVO
- [ ] `dbscripts/V1.xml` — DDL THGQMGAUDEVID
