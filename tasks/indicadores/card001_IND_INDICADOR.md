# Card IND-01 — Cadastro de Indicadores de Qualidade

## Identificacao
| Atributo        | Valor                                          |
|:----------------|:-----------------------------------------------|
| Modulo          | Indicadores                                    |
| Fase Roadmap    | Fase 4                                         |
| ISO             | ABNT NBR ISO 9001:2015 — Clausulas 6.2 + 9.1.1 |
| Ordem no modulo | 001 de 005                                     |
| Depende de      | CORE-01                                        |

---

## Tabela
| Atributo  | Valor                  |
|:----------|:-----------------------|
| Nome      | `THGQMGIND`    |
| Instancia | `QmIndIndicador`       |
| Sequencia | AUTO (`CODIND`)   |
| Dual-DB   | Oracle + SQL Server    |

## Instancias
| Instancia         | Tipo       | Descricao                                     |
|:------------------|:-----------|:----------------------------------------------|
| `QmIndConsulta`   | Lista/Grid | Visao de todos os indicadores cadastrados     |
| `QmIndIndicador`  | Formulario | Formulario de cadastro e configuracao         |

---

## Campos

| # | Rotulo          | Coluna          | Tipo      | Tam | Obrig | Aba    | Grupo        | Opcoes / Comportamento                                              |
|:--|:----------------|:----------------|:----------|:----|:------|:-------|:-------------|:--------------------------------------------------------------------|
| 1 | Id Indicador    | `CODIND`   | INTEIRO   | -   | PK    | __main | -            | readOnly, auto sequence                                             |
| 2 | Codigo          | `CODIGO`        | TEXTO     | 20  | Sim   | __main | -            | isPresentation=S. Codigo unico. UNIQUE                              |
| 3 | Nome            | `NOME`          | TEXTO     | 200 | Sim   | __main | -            | Nome descritivo. Ex: Taxa de Reincidencia de NC                     |
| 4 | Processo        | `PROCESSO`      | TEXTO     | 200 | Sim   | __main | Identificacao| Processo ao qual o KPI pertence                                     |
| 5 | Tipo            | `TIPO`          | LISTA     | 1   | Sim   | __main | Identificacao| Q=Qualidade, E=Eficiencia, P=Prazo, S=Satisfacao, A=Ambiental       |
| 6 | Norma ISO       | `NORMAISO`      | LISTA     | 4   | Nao   | __main | Identificacao| 9001=ISO 9001, 1400=ISO 14001, 1702=ISO 17025, NA=Nao aplicavel     |
| 7 | Clausula ISO    | `CLAUSULAISO`   | TEXTO     | 20  | Nao   | __main | Identificacao| Clausula de referencia. Ex: 10.2.1f                                 |
| 8 | Unidade         | `UNIDADE`       | TEXTO     | 30  | Sim   | __main | Medicao      | Unidade de medida. Ex: %, dias, numero, pontos                      |
| 9 | Frequencia      | `FREQUENCIA`    | LISTA     | 1   | Sim   | __main | Medicao      | D=Diario, S=Semanal, M=Mensal, T=Trimestral, A=Anual                |
| 10| Formula         | `FORMULA`       | TEXTO     | 500 | Nao   | __main | Medicao      | Descricao de como calcular o indicador                              |
| 11| Responsavel     | `CODPARC`       | PESQUISA  | -   | Nao   | __main | -            | targetInstance=Parceiro. Responsavel pela medicao                   |
| 12| Meta Padrao     | `METAPADRAO`    | DECIMAL   | -   | Nao   | __main | Medicao      | Valor de meta global                                                |
| 13| Sentido         | `SENTIDO`       | LISTA     | 1   | Nao   | __main | Medicao      | M=Maior e melhor, N=Menor e melhor                                  |
| 14| Ativo           | `ATIVO`         | CHECKBOX  | 1   | Nao   | __main | -            | Default S                                                           |
| 15| Criado por      | `CODUSU`        | PESQUISA  | -   | Nao   | __main | -            | readOnly. Auto: $ctx_usuario_logado. visivel=N                      |
| 16| Criado em       | `DHCREATE`     | DATA_HORA | -   | Nao   | __main | -            | readOnly. Auto: $ctx_dh_atual. visivel=N                            |

## Tabelas Filhas
| Tabela              | Instancia filha  | Vinculo FK     | Descricao                            |
|:--------------------|:-----------------|:---------------|:-------------------------------------|
| `THGQMGMETA`      | `QmIndMeta`      | `CODIND`  | Metas por periodo — card IND-02      |
| `THGQMGMED`   | `QmIndMedicao`   | `CODIND`  | Medicoes periodicas — card IND-03    |

## Artefatos
- [ ] `datadictionary/THGQMGIND.xml` — 2 instancias, grupos Identificacao e Medicao
- [ ] `dbscripts/V1.xml` — DDL THGQMGIND, Listener BI, sequence
- [ ] `datadictionary/menu.xml` — QmIndConsulta e QmIndIndicador em pasta Indicadores
