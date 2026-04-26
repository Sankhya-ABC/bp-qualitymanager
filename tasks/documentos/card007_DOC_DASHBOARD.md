# Card DOC-07 — Dashboard KPI Documentos

## Identificacao
| Atributo        | Valor                                  |
|:----------------|:---------------------------------------|
| Modulo          | Documentos                             |
| Fase Roadmap    | Fase 2                                 |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 9.1  |
| Ordem no modulo | 007 de 007                             |
| Depende de      | Cards DOC-01 a DOC-05                  |

---

## Arquivo

| Atributo  | Valor                        |
|:----------|:-----------------------------|
| Arquivo   | `dashboard_doc_kpi.xml`      |
| Pasta     | `dashboards/`                |

---

## KPIs

| KPI                      | Query base                                                      | Alerta                  |
|:-------------------------|:----------------------------------------------------------------|:------------------------|
| Documentos por status    | COUNT(*) GROUP BY STATUS em THGQMGDOC                   | Vencidos > 0 = vermelho |
| Vencendo em 30 dias      | COUNT(*) WHERE DTVALIDADE BETWEEN hoje E hoje+30               | > 0 = amarelo           |
| Vencendo em 60 dias      | COUNT(*) WHERE DTVALIDADE BETWEEN hoje+31 E hoje+60            | informativo             |
| Tempo medio de aprovacao | AVG(dias entre criacao e STATUS='A') via THGQMGHIST     | > 7 dias = amarelo      |
| Copias impressas ativas  | SUM(QTDECOPIAS) em THGQMGIMP (docs STATUS != 'O')       | informativo             |
| Documentos por norma ISO | COUNT(*) GROUP BY NORMAISO                                     | informativo             |

---

## Artefatos

### Dashboard
- [ ] `dashboards/dashboard_doc_kpi.xml` — criado no construtor visual e exportado
