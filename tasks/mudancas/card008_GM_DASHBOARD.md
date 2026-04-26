# Card GM-08 — Dashboard KPI Gestao de Mudancas

## Identificacao
| Atributo        | Valor                                  |
|:----------------|:---------------------------------------|
| Modulo          | Gestao de Mudancas                     |
| Fase Roadmap    | Fase 3                                 |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 9.1  |
| Ordem no modulo | 008 de 008                             |
| Depende de      | Cards GM-01 a GM-06                    |

## Arquivo
| Atributo | Valor                     |
|:---------|:--------------------------|
| Arquivo  | `dashboard_gm_kpi.xml`    |
| Pasta    | `dashboards/`             |

## KPIs
| KPI                     | Query base                                               | Alerta                  |
|:------------------------|:---------------------------------------------------------|:------------------------|
| Mudancas por fase       | COUNT(*) GROUP BY CODFASE em THGQMGGM               | informativo             |
| Taxa de aprovacao       | COUNT(STATUS='C') / COUNT(*) * 100                      | < 70% = amarelo         |
| Tempo medio aprovacao   | AVG(DTIMPL - DHCREATE) WHERE STATUS='C'                | > 30 dias = amarelo     |
| Mudancas por tipo       | COUNT(*) GROUP BY TIPOMUDANCA                           | informativo             |
| Em andamento            | COUNT(*) WHERE STATUS='A'                               | informativo             |

## Artefatos
- [ ] `dashboards/dashboard_gm_kpi.xml` — criado no construtor visual e exportado
