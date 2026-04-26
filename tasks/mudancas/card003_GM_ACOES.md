# Card GM-03 — Acoes da Mudanca 5W2H

## Identificacao
| Atributo        | Valor                                   |
|:----------------|:----------------------------------------|
| Modulo          | Gestao de Mudancas                      |
| Fase Roadmap    | Fase 3                                  |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 6.3c  |
| Ordem no modulo | 003 de 008                              |
| Depende de      | GM-01                                   |

## Tabelas
| Tabela         | Instancia    | Sequencia    | Descricao                           |
|:---------------|:-------------|:-------------|:------------------------------------|
| `THGQMGGMACAO`  | `ThgGmAcao`   | `CODACAOGM`   | Acao 5W2H necessaria para a mudanca |
| `THGQMGGMQUEM`  | `ThgGmQuem`   | `CODQUEMGM`   | Responsaveis da acao                |

## Campos — THGQMGGMACAO
| # | Rotulo        | Coluna       | Tipo      | Tam | Obrig | Opcoes                                          |
|:--|:--------------|:-------------|:----------|:----|:------|:------------------------------------------------|
| 1 | Id Acao GM    | `CODACAOGM`   | INTEIRO   | -   | PK    | readOnly, auto sequence                         |
| 2 | Gestao        | `CODGM`   | INTEIRO   | -   | Sim   | readOnly. FK para THGQMGGM                 |
| 3 | O Que         | `OQUE`       | TEXTO     | 500 | Nao   | Acao a ser executada para a mudanca             |
| 4 | Como          | `COMO`       | TEXTO     | 500 | Nao   | Como sera executada                             |
| 5 | Onde          | `ONDE`       | TEXTO     | 500 | Nao   | Onde sera executada                             |
| 6 | Por Que       | `PORQUE`     | TEXTO     | 500 | Nao   | Justificativa                                   |
| 7 | Quando        | `QUANDO`     | DATA_HORA | -   | Nao   | Prazo previsto                                  |
| 8 | Quanto        | `QUANTO`     | TEXTO     | 200 | Nao   | Recursos necessarios                            |
| 9 | Nao se Aplica | `NAOSEAPLICA`| CHECKBOX  | 1   | Nao   | Fase nao aplicavel                              |
| 10| Aberto por    | `CODUSU`     | PESQUISA  | -   | Nao   | readOnly. Auto: $ctx_usuario_logado. visivel=N  |
| 11| Criado em     | `DHCREATE`  | DATA_HORA | -   | Nao   | readOnly. Auto: $ctx_dh_atual. visivel=N        |

## Campos — THGQMGGMQUEM
| # | Rotulo     | Coluna       | Tipo     | Tam | Obrig | Opcoes                                        |
|:--|:-----------|:-------------|:---------|:----|:------|:----------------------------------------------|
| 1 | Id         | `CODQUEMGM`   | INTEIRO  | -   | PK    | readOnly, auto sequence                       |
| 2 | Acao       | `CODACAOGM`   | INTEIRO  | -   | Sim   | readOnly. FK para THGQMGGMACAO                 |
| 3 | Parceiro   | `CODPARC`    | PESQUISA | -   | Sim   | targetInstance=Parceiro. Responsavel da acao  |

## Action Buttons
| Botao      | Classe Java                          | Instancia  | Descricao                               |
|:-----------|:-------------------------------------|:-----------|:----------------------------------------|
| Mudar Fase | `MudarFaseAcoesMudancaActionButton`  | `ThgGmAcao` | Avanca GM para Fase 4 (Avaliacao Riscos)|
| Voltar Fase| `VoltarFaseAcoesMudancaActionButton` | `ThgGmAcao` | Retorna GM para Fase 2                  |

## Artefatos
- [ ] `datadictionary/THGQMGGMACAO.xml`
- [ ] `datadictionary/THGQMGGMQUEM.xml`
- [ ] `dbscripts/V1.xml` — DDL das 2 tabelas
- [ ] `model/.../actionButtons/MudarFaseAcoesMudancaActionButton.java`
- [ ] `model/.../actionButtons/VoltarFaseAcoesMudancaActionButton.java`
