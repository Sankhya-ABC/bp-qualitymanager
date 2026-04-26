# Card FORN-04 — Avaliacao Periodica de Fornecedor

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Fornecedores                             |
| Fase Roadmap    | Fase 2                                   |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 8.4.1  |
| Ordem no modulo | 004 de 009                               |
| Depende de      | FORN-01, FORN-02                         |

---

## Tabela

| Atributo  | Valor                  |
|:----------|:-----------------------|
| Nome      | `THGQMGFAVAL`   |
| Instancia | `QmFornAvaliacao`      |
| Sequencia | AUTO (`CODAVAL`)   |
| Dual-DB   | Oracle + SQL Server    |

---

## Campos

| # | Rotulo           | Coluna           | Tipo      | Tam | Obrig | Aba    | Grupo | Opcoes / Comportamento                                      |
|:--|:-----------------|:-----------------|:----------|:----|:------|:-------|:------|:------------------------------------------------------------|
| 1 | Id Avaliacao     | `CODAVAL`    | INTEIRO   | -   | PK    | __main | -     | readOnly, auto sequence                                     |
| 2 | Qualificacao     | `CODQUAL` | INTEIRO   | -   | Sim   | __main | -     | readOnly. FK para THGQMGQUAL                     |
| 3 | Periodo          | `PERIODO`        | TEXTO     | 10  | Sim   | __main | -     | isPresentation=S. Formato MM/YYYY. Ex: 04/2025              |
| 4 | Nota Qualidade   | `NOTA_QUALIDADE` | DECIMAL   | -   | Nao   | __main | Notas | 0 a 10. Criterio Qualidade                                  |
| 5 | Nota Prazo       | `NOTA_PRAZO`     | DECIMAL   | -   | Nao   | __main | Notas | 0 a 10. Criterio Prazo                                      |
| 6 | Nota Tecnico     | `NOTA_TECNICO`   | DECIMAL   | -   | Nao   | __main | Notas | 0 a 10. Criterio Tecnico                                    |
| 7 | Nota Seguranca   | `NOTA_SEGURANCA` | DECIMAL   | -   | Nao   | __main | Notas | 0 a 10. Criterio Seguranca                                  |
| 8 | Score Final      | `SCORE_FINAL`    | DECIMAL   | -   | Nao   | __main | -     | readOnly. Calculado: media ponderada pelos pesos em THGQMGCRIT |
| 9 | Observacoes      | `OBSERVACOES`    | HTML      | -   | Nao   | __main | -     | Campo livre para comentarios da avaliacao                   |
| 10| Avaliador        | `CODUSU`         | PESQUISA  | -   | Nao   | __main | -     | readOnly. Auto: $ctx_usuario_logado                         |
| 11| Data Avaliacao   | `DHCREATE`      | DATA_HORA | -   | Nao   | __main | -     | readOnly. Auto: $ctx_dh_atual                               |

---

## Regras de Negocio

1. SCORE_FINAL calculado automaticamente apos INSERT via AvaliacaoFornListener
2. Formula: (NOTA_QUALIDADE * pesoQualidade + NOTA_PRAZO * pesoPrazo + ...) / somaTotal
3. BigDecimal.divide() com escala 2 e HALF_UP — obrigatorio
4. Tabela e aba filha de QmFornQualificacao (card FORN-01)

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGFAVAL, Listener BI, sequence

### Dicionario de Dados
- [ ] `datadictionary/THGQMGFAVAL.xml` — grupo Notas para os 4 campos de nota

### Backend Java
- [ ] `model/.../listeners/AvaliacaoFornListener.java` — instanceName: `QmFornAvaliacao`
