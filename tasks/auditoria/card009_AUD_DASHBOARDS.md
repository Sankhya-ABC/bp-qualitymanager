# Card AUD-09 — Dashboards de Auditoria

## Identificacao
| Atributo        | Valor                                         |
|:----------------|:----------------------------------------------|
| Modulo          | Auditoria                                     |
| Fase Roadmap    | Fase 3                                        |
| ISO             | ABNT NBR ISO 9001:2015 — Clausulas 9.1 + 9.2  |
| Ordem no modulo | 009 de 009                                    |
| Depende de      | Cards AUD-01 a AUD-08                         |

## Arquivos — 2 dashboards

| Arquivo                        | Descricao                                      |
|:-------------------------------|:-----------------------------------------------|
| `dashboard_aud_programa.xml`   | Acompanhamento do programa anual               |
| `dashboard_aud_resultados.xml` | Resultados e constatacoes das auditorias       |

## KPIs — Dashboard Programa
| KPI                       | Fonte                                                      | Alerta                   |
|:--------------------------|:-----------------------------------------------------------|:-------------------------|
| % execucao do programa    | Concluidas / Planejadas * 100 em THGQMGAUDPROG          | < 80% = amarelo          |
| Proximas auditorias 30d   | COUNT(*) WHERE STATUS='P' AND DTPREVISTA <= hoje+30        | informativo              |
| Processos vencidos >12m   | Processos sem auditoria em THGQMGAUD ha 12+ meses  | qualquer = vermelho      |
| Auditorias por norma      | COUNT(*) GROUP BY NORMAISO                                 | informativo              |

## KPIs — Dashboard Resultados
| KPI                       | Fonte                                                      | Alerta                   |
|:--------------------------|:-----------------------------------------------------------|:-------------------------|
| Constatacoes por tipo     | COUNT(*) GROUP BY TIPO em THGQMGAUDCON              | NC Maior > 0 = vermelho  |
| NCs abertas de auditoria  | COUNT(*) em THGQMGREG WHERE ORIGEM='A' e STATUS aberto | > 0 = alerta           |
| Processos com mais NCs    | TOP 5 por constatacoes NC                                  | informativo              |
| Tendencia de conformidade | % Conformes nos ultimos 6 ciclos                           | queda = alerta           |
| NCs auditoria atrasadas   | ORIGEM='A' e DTPREVENCERRAR < hoje                         | qualquer = vermelho      |

## Artefatos
- [ ] `dashboards/dashboard_aud_programa.xml`
- [ ] `dashboards/dashboard_aud_resultados.xml`
