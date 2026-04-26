# Card FORN-09 — Dashboard KPI Fornecedores

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Fornecedores                             |
| Fase Roadmap    | Fase 2                                   |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 9.1    |
| Ordem no modulo | 009 de 009                               |
| Depende de      | Cards FORN-01 a FORN-05                  |

---

## Arquivo

| Atributo  | Valor                        |
|:----------|:-----------------------------|
| Arquivo   | `dashboard_forn_kpi.xml`     |
| Pasta     | `dashboards/`                |
| Tipo      | Dashboard Sankhya Om embarcado|

---

## KPIs — Cards de Metrica (simple-value)

| KPI                          | Query base                                                   | Alerta                      |
|:-----------------------------|:-------------------------------------------------------------|:----------------------------|
| Score medio da base          | AVG(PONTUACAO) em THGQMGQUAL WHERE ATIVO='S'     | < 70 = alerta amarelo       |
| Fornecedores aprovados       | COUNT(*) WHERE STATUS='A'                                    | informativo                 |
| Fornecedores bloqueados      | COUNT(*) WHERE BLOQUEADO='S'                                 | qualquer = alerta vermelho  |
| Qualificacoes vencendo 30d   | COUNT(*) WHERE DTVALIDADE BETWEEN hoje E hoje+30            | > 0 = alerta amarelo        |
| Documentos vencidos          | COUNT(*) em THGQMGFORNDOC WHERE STATUS='E'             | > 0 = alerta vermelho       |

---

## KPIs — Graficos

| Grafico                      | Tipo     | Dimensao X          | Dimensao Y      | Filtro       |
|:-----------------------------|:---------|:--------------------|:----------------|:-------------|
| Fornecedores por status      | Pizza    | STATUS              | Qtde            | ATIVO='S'    |
| Score por tipo de fornecimento| Barras  | TIPOFORN            | AVG(PONTUACAO)  | —            |
| NCs por fornecedor (top 5)   | Barras H | CODPARC (nome)      | COUNT NCs       | Ultimo ano   |
| Evolucao de qualificacoes    | Linha    | Mes/Ano             | Novas qualif.   | Ultimo ano   |

---

## Parametros do Dashboard

| Parametro  | Tipo  | Default      | Descricao                     |
|:-----------|:------|:-------------|:------------------------------|
| DT_INICIO  | DATA  | 01/mes atual | Inicio do periodo de analise  |
| DT_FIM     | DATA  | hoje         | Fim do periodo de analise     |
| CODEMP     | INT   | emp. logada  | Filtro por empresa            |

---

## Artefatos

### Dashboard
- [ ] `dashboards/dashboard_forn_kpi.xml` — criado no construtor visual do Sankhya Om e exportado

### Menu
- [x] `datadictionary/menu.xml` — entrada ja registrada em pasta Relatorios e KPIs

---

## Observacoes Tecnicas

- Dashboard criado no construtor visual e exportado como XML — nao criar o XML manualmente
- Queries devem ser validadas no banco (Oracle e SQL Server) antes de configurar
- Score medio deve usar COALESCE(PONTUACAO, 0) para nao quebrar com valores nulos
