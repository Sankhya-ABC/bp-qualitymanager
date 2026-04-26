# Card AUD-02 — Registro de Auditoria

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Auditoria                                |
| Fase Roadmap    | Fase 3                                   |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 9.2.2  |
| Ordem no modulo | 002 de 009                               |
| Depende de      | AUD-01, AUD-08                           |

## Tabela
| Atributo  | Valor                  |
|:----------|:-----------------------|
| Nome      | `THGQMGAUD`    |
| Instancia | `ThgAudRegistro`        |
| Sequencia | AUTO (`CODAUD`)   |
| Dual-DB   | Oracle + SQL Server    |

## Instancias
| Instancia        | Tipo       | Descricao                                    |
|:-----------------|:-----------|:---------------------------------------------|
| `ThgAudConsulta`  | Lista/Grid | Visao de todas as auditorias                 |
| `ThgAudRegistro`  | Formulario | Formulario completo da auditoria             |

## Campos
| # | Rotulo           | Coluna          | Tipo      | Tam | Obrig | Aba    | Grupo | Opcoes / Comportamento                                              |
|:--|:-----------------|:----------------|:----------|:----|:------|:-------|:------|:--------------------------------------------------------------------|
| 1 | Id Auditoria     | `CODAUD`   | INTEIRO   | -   | PK    | __main | -     | readOnly, auto sequence                                             |
| 2 | Numero           | `NUMAUDITORIA`  | TEXTO     | 20  | Nao   | __main | -     | readOnly. Trigger: AUD-YYYY-NNNN. isPresentation=S                  |
| 3 | Tipo             | `TIPO`          | LISTA     | 1   | Sim   | __main | -     | 1=Primeira parte (interna), 2=Segunda parte, 3=Terceira parte (certificacao) |
| 4 | Norma ISO        | `NORMAISO`      | LISTA     | 4   | Sim   | __main | -     | 9001=ISO 9001, 1400=ISO 14001, 1702=ISO 17025                        |
| 5 | Escopo           | `ESCOPO`        | TEXTO     | 500 | Sim   | __main | -     | Descricao do escopo                                                 |
| 6 | Processo         | `PROCESSO`      | TEXTO     | 200 | Sim   | __main | -     | Processo ou area sendo auditada                                     |
| 7 | Status           | `STATUS`        | LISTA     | 1   | Nao   | __main | -     | readOnly. P=Planejada, A=Em andamento, C=Concluida, X=Cancelada     |
| 8 | Auditor Lider    | `CODAUDTOR`     | PESQUISA  | -   | Sim   | __main | -     | targetInstance=ThgAudAuditor                                         |
| 9 | Empresa Auditada | `CODEMP`        | PESQUISA  | -   | Nao   | __main | -     | targetInstance=Empresa                                              |
| 10| Data Prevista    | `DTPREVISTA`    | DATA_HORA | -   | Nao   | __main | Datas | Data planejada para execucao                                        |
| 11| Data Inicio      | `DTINICIO`      | DATA_HORA | -   | Nao   | __main | Datas | readOnly. Data real de inicio                                       |
| 12| Data Conclusao   | `DTCONCLUSAO`   | DATA_HORA | -   | Nao   | __main | Datas | readOnly. Data real de conclusao                                    |
| 13| Programa Origem  | `CODPROG`    | INTEIRO   | -   | Nao   | __main | -     | FK opcional para THGQMGAUDPROG                                   |
| 14| Criado por       | `CODUSU`        | PESQUISA  | -   | Nao   | __main | -     | readOnly. Auto: $ctx_usuario_logado. visivel=N                      |
| 15| Criado em        | `DHCREATE`     | DATA_HORA | -   | Nao   | __main | -     | readOnly. Auto: $ctx_dh_atual. visivel=N                            |

## Tabelas Filhas
| Tabela                  | Instancia filha       | Vinculo FK     | Descricao                     |
|:------------------------|:----------------------|:---------------|:------------------------------|
| `THGQMGAUDPLAN`         | `ThgAudPlano`          | `CODAUD`  | Plano de auditoria — AUD-03   |
| `THGQMGAUDCON`   | `ThgAudConstatacao`    | `CODAUD`  | Constatacoes — AUD-05         |
| `THGQMGAUDEVID`     | `ThgAudEvidencia`      | `CODAUD`  | Evidencias — AUD-06           |
| `THGQMGAUDPART`  | `ThgAudParticipante`   | `CODAUD`  | Participantes — AUD-07        |

## Vinculo critico com Modulo RNC
- Campo `CODAUD` adicionado em `THGQMGREG` para rastreabilidade bidirecional
- NC Maior de auditoria -> PRIORIDADE=3 (Critico) automaticamente
- NC Menor de auditoria -> PRIORIDADE=2 (Prioritario) automaticamente
- ORIGEM='A' na NC indica que veio de auditoria

## Artefatos
- [ ] `datadictionary/THGQMGAUD.xml` — 2 instancias
- [ ] `dbscripts/V1.xml` — DDL THGQMGAUD, Listener BI (NUMAUDITORIA AUD-YYYY-NNNN)
- [ ] `datadictionary/menu.xml` — ThgAudConsulta e ThgAudRegistro em pasta Auditoria
