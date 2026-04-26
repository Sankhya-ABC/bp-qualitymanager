# Card IND-03 — Medicoes Periodicas

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Indicadores                              |
| Fase Roadmap    | Fase 4                                   |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 9.1.1  |
| Ordem no modulo | 003 de 005                               |
| Depende de      | IND-01, IND-02                           |

## Tabela
| Atributo  | Valor                |
|:----------|:---------------------|
| Nome      | `THGQMGMED`    |
| Instancia | `QmIndMedicao`       |
| Sequencia | AUTO (`CODMEDICAO`)   |
| Dual-DB   | Oracle + SQL Server  |

## Campos
| # | Rotulo       | Coluna         | Tipo      | Tam | Obrig | Opcoes                                              |
|:--|:-------------|:---------------|:----------|:----|:------|:----------------------------------------------------|
| 1 | Id Medicao   | `CODMEDICAO`    | INTEIRO   | -   | PK    | readOnly, auto sequence                             |
| 2 | Indicador    | `CODIND`  | INTEIRO   | -   | Sim   | readOnly. FK para THGQMGIND                 |
| 3 | Periodo      | `PERIODO`      | TEXTO     | 10  | Sim   | isPresentation=S. Formato MM/YYYY                   |
| 4 | Valor        | `VALOR`        | DECIMAL   | -   | Sim   | Valor medido no periodo                             |
| 5 | Observacoes  | `OBSERVACOES`  | TEXTO     | 300 | Nao   | Contexto da medicao                                 |
| 6 | Medido por   | `CODUSU`       | PESQUISA  | -   | Nao   | readOnly. Auto: $ctx_usuario_logado                 |
| 7 | Data Medicao | `DHCREATE`    | DATA_HORA | -   | Nao   | readOnly. Auto: $ctx_dh_atual                       |

## Regras de Negocio
1. Apos INSERT: MedicaoIndicadorListener atualiza THGQMGMETA.REALIZADO e ATINGIMENTO
2. Historico de medicoes gera a tendencia no dashboard IND-05
3. instanceName do listener: `QmIndMedicao` — copiar EXATAMENTE do XML

## Artefatos
- [ ] `datadictionary/THGQMGMED.xml`
- [ ] `dbscripts/V1.xml` — DDL THGQMGMED
- [ ] `model/.../listeners/MedicaoIndicadorListener.java`
