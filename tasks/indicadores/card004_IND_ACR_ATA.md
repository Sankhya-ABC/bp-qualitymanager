# Card IND-04 — Analise Critica pela Direcao (§9.3)

## Identificacao
| Atributo        | Valor                                   |
|:----------------|:----------------------------------------|
| Modulo          | Indicadores / Analise Critica           |
| Fase Roadmap    | Fase 4                                  |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 9.3   |
| Ordem no modulo | 004 de 005                              |
| Depende de      | Todos os modulos anteriores             |

## Tabelas
| Tabela             | Instancia      | Sequencia      | Descricao                                  |
|:-------------------|:---------------|:---------------|:-------------------------------------------|
| `THGQMGATA`      | `ThgAcrAta`     | `CODACR`        | Ata da analise critica pela direcao        |
| `THGQMGENT`  | `ThgAcrEntrada` | `CODENTRADA`    | Entradas obrigatorias da reuniao (§9.3.2)  |
| `THGQMGSAI`    | `ThgAcrSaida`   | `CODSAIDA`      | Decisoes e acoes resultantes (§9.3.3)      |

## Campos — THGQMGATA
| # | Rotulo         | Coluna         | Tipo      | Tam | Obrig | Opcoes                                                   |
|:--|:---------------|:---------------|:----------|:----|:------|:---------------------------------------------------------|
| 1 | Id ACR         | `CODACR`        | INTEIRO   | -   | PK    | readOnly, auto sequence                                  |
| 2 | Numero         | `NUMACR`       | TEXTO     | 20  | Nao   | readOnly. Trigger: ACR-YYYY-NN. isPresentation=S         |
| 3 | Periodo        | `PERIODO`      | TEXTO     | 10  | Sim   | Ex: 2025-01 (1o semestre 2025)                           |
| 4 | Data Reuniao   | `DTREUNIAO`    | DATA_HORA | -   | Sim   | Data e hora da reuniao de analise critica                |
| 5 | Local          | `LOCAL`        | TEXTO     | 200 | Nao   | Local fisico ou virtual da reuniao                       |
| 6 | Status         | `STATUS`       | LISTA     | 1   | Nao   | readOnly. R=Rascunho, A=Aprovada, E=Encerrada            |
| 7 | Conclusao Geral| `CONCLUSAO`    | HTML      | -   | Nao   | Avaliacao geral do desempenho do SGQ no periodo          |
| 8 | Criado por     | `CODUSU`       | PESQUISA  | -   | Nao   | readOnly. Auto: $ctx_usuario_logado. visivel=N           |
| 9 | Criado em      | `DHCREATE`    | DATA_HORA | -   | Nao   | readOnly. Auto: $ctx_dh_atual. visivel=N                 |

## Entradas obrigatorias §9.3.2 (registradas em THGQMGENT)

| Entrada ISO                              | Fonte de dados no produto              |
|:-----------------------------------------|:---------------------------------------|
| a) Status de acoes de revisoes anteriores| THGQMGSAI de ACRs anteriores       |
| b) Mudancas no contexto                  | Registro manual                        |
| c) Desempenho e eficacia do SGQ          | THGQMGMETA + THGQMGMED         |
| d) Adequacao de recursos                 | Registro manual                        |
| e) Eficacia das acoes para riscos        | THGQMGEFIC + THGQMGGMAVAL    |
| f) Oportunidades de melhoria             | THGQMGAUDCON tipo OM            |

## Artefatos
- [ ] `datadictionary/THGQMGATA.xml`
- [ ] `datadictionary/THGQMGENT.xml`
- [ ] `datadictionary/THGQMGSAI.xml`
- [ ] `dbscripts/V1.xml` — DDL das 3 tabelas
- [ ] `datadictionary/menu.xml` — ThgAcrAta em pasta Indicadores
