# Card IND-05 — Dashboard Maturidade ISO (Painel do Auditor)

## Identificacao
| Atributo        | Valor                                         |
|:----------------|:----------------------------------------------|
| Modulo          | Indicadores                                   |
| Fase Roadmap    | Fase 4                                        |
| ISO             | Transversal — todas as normas ativas          |
| Ordem no modulo | 005 de 005                                    |
| Depende de      | Todos os modulos ativados                     |

## Arquivo
| Atributo  | Valor                           |
|:----------|:--------------------------------|
| Arquivo   | `dashboard_maturidade_iso.xml`  |
| Pasta     | `dashboards/`                   |
| Acesso    | Restrito — perfil Auditor/Gestor|

## Logica de Score por Clausula ISO 9001

| Clausula | Fonte de evidencia no produto              | Score 100% quando                       |
|:---------|:-------------------------------------------|:----------------------------------------|
| §7.5     | THGQMGDOC com STATUS='A'            | Docs aprovados / total ativos >= 80%    |
| §8.4     | THGQMGQUAL STATUS != 'T'        | Aprovados ou condicionais >= 90%        |
| §8.7     | THGQMGREG com CODFASE=10             | NCs encerradas com eficacia >= 80%      |
| §9.2     | THGQMGAUDPROG STATUS='C' no ano        | Programa >= 80% executado               |
| §10.2    | THGQMGEFIC RESULTADO='E'             | Taxa de eficacia >= 80%                 |
| §6.3     | THGQMGGM STATUS='C'                 | Mudancas aprovadas vs abertas           |
| §9.1     | THGQMGMETA com medicoes no periodo      | Indicadores com META.STATUS != 'N' >= 70% |
| §9.3     | THGQMGATA no periodo                    | Ata realizada no periodo = 100%         |

Score total = media ponderada das clausulas com modulo ativo.

## KPIs
| KPI                         | Alerta                    |
|:----------------------------|:--------------------------|
| Score geral de maturidade   | < 70% = vermelho          |
| Score por clausula (radar)  | clausula < 50% = alerta   |
| Gaps criticos               | lista vermelha            |
| Evolucao historica 12 meses | tendencia de queda        |
| Prontidao para certificacao | Pronto / Com ressalvas / Nao pronto |

## Regras de Acesso
1. Visivel apenas para perfil Gestor de Qualidade ou Auditor Interno
2. Auditor externo de certificacao: perfil temporario durante auditoria
3. Usuario operacional nao ve este dashboard

## Artefatos
- [ ] `dashboards/dashboard_maturidade_iso.xml`
- [ ] Documentar logica de calculo de score por clausula antes de criar o dashboard
