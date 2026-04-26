# Card 014 — Dashboard KPI de Registro de Nao Conformidades

## Identificacao
| Atributo        | Valor                                         |
|:----------------|:----------------------------------------------|
| Modulo          | RNC — Registro de Nao Conformidades                       |
| Fase Roadmap    | Fase 1 — Core + Modulo NC/RNC                 |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 9.1         |
| Ordem no modulo | 014 de 014                                    |
| Depende de      | Cards 001 a 012 (todos os dados ja existem)   |

---

## Arquivo

| Atributo  | Valor                       |
|:----------|:----------------------------|
| Arquivo   | `dashboard_nc_kpi.xml`      |
| Pasta     | `dashboards/`               |
| Tipo      | Dashboard Sankhya Om embarcado no add-on |

---

## KPIs do dashboard

### Cards de metrica (simple-value)

| KPI                     | Query base                                                          | Meta / Alerta               |
|:------------------------|:--------------------------------------------------------------------|:----------------------------|
| RNCs abertas            | COUNT(*) WHERE STATUS IN ('A','P')                                  | — (informativo)             |
| RNCs atrasadas          | COUNT(*) WHERE STATUS IN ('A','P') AND DTPREVENCERRAR < SYSDATE     | > 0 = alerta vermelho       |
| RNCs encerradas (mes)   | COUNT(*) WHERE STATUS='E' AND DTENCERRAMENTO >= inicio do mes       | — (informativo)             |
| Taxa de eficacia        | COUNT(RESULTADO='E') / COUNT(*) * 100 em THGQMGEFIC           | Meta configuravel (default 80%) |
| Taxa de reincidencia    | COUNT(*) WHERE REINCIDENTE='S' / COUNT(*) * 100                    | > 10% = alerta amarelo      |

### Graficos

| Grafico                  | Tipo      | Dimensao X     | Dimensao Y         | Filtro          |
|:-------------------------|:----------|:---------------|:-------------------|:----------------|
| RNCs por fase (gargalo)  | Barras    | Fase (1 a 10)  | Qtde de RNCs       | STATUS != 'E'   |
| RNCs por origem          | Pizza     | ORIGEM         | Qtde               | Periodo         |
| RNCs por prioridade      | Pizza     | PRIORIDADE     | Qtde               | STATUS aberta   |
| Evolucao mensal          | Linha     | Mes/Ano        | Abertas vs Encerradas | Ultimo ano   |
| Top responsaveis         | Barras H  | Responsavel    | Qtde RNCs abertas  | STATUS aberta   |

### Parametros do dashboard

| Parametro   | Tipo      | Default        | Descricao                     |
|:------------|:----------|:---------------|:------------------------------|
| DT_INICIO   | DATA      | 01/mes atual   | Inicio do periodo de analise  |
| DT_FIM      | DATA      | hoje           | Fim do periodo de analise     |
| CODEMP      | INTEIRO   | empresa logada | Filtro por empresa            |

---

## Regras de Negocio do Dashboard

1. Dashboard disponivel no menu dentro da pasta "Relatorios e KPIs" do add-on
2. Visivel para todos os perfis com acesso ao modulo RNC
3. KPI "Taxa de eficacia" usa meta configurada em THGQMGCFG (campo futuro)
4. Alerta de atrasadas (vermelho) quando DTPREVENCERRAR < data atual e STATUS aberta
5. Filtro de periodo padrao: mes atual — usuario pode alterar
6. Grafico de gargalo por fase mostra onde as RNCs estao paradas — visao operacional do Kanban

---

## Artefatos

### Dashboard
- [ ] `dashboards/dashboard_nc_kpi.xml` — exportado do construtor Sankhya Om com os KPIs acima

### Menu
- [x] `datadictionary/menu.xml` — entrada dashboard_nc_kpi.xml ja registrada em "Relatorios e KPIs"

---

## Observacoes Tecnicas

- Dashboard criado no construtor visual do Sankhya Om e exportado como XML
- Nao e possivel criar o XML de dashboard manualmente do zero — deve ser construido no Om e exportado
- As queries SQL dos KPIs devem ser validadas no banco antes de configurar no dashboard
- Verificar suporte a dual-DB (Oracle e SQL Server) nas funcoes de data usadas nas queries
