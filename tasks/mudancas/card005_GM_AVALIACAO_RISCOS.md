# Card GM-05 — Avaliacao de Riscos das Acoes

## Identificacao
| Atributo        | Valor                                   |
|:----------------|:----------------------------------------|
| Modulo          | Gestao de Mudancas                      |
| Fase Roadmap    | Fase 3                                  |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 6.1   |
| Ordem no modulo | 005 de 008                              |
| Depende de      | GM-01, GM-03                            |

## Tabela
| Atributo  | Valor                  |
|:----------|:-----------------------|
| Nome      | `THGQMGGMAVAL`     |
| Instancia | `QmGmAvaliacao`        |
| Sequencia | AUTO (`CODAVALRISCO`)   |
| Dual-DB   | Oracle + SQL Server    |

## Campos
| # | Rotulo        | Coluna          | Tipo     | Tam | Obrig | Opcoes                                                  |
|:--|:--------------|:----------------|:---------|:----|:------|:--------------------------------------------------------|
| 1 | Id            | `CODAVALRISCO`   | INTEIRO  | -   | PK    | readOnly, auto sequence                                 |
| 2 | Gestao        | `CODGM`      | INTEIRO  | -   | Sim   | readOnly. FK para THGQMGGM                         |
| 3 | Acao Avaliada | `CODACAOGM`      | INTEIRO  | -   | Nao   | FK opcional para THGQMGGMACAO                            |
| 4 | Risco         | `RISCO`         | HTML     | -   | Sim   | Descricao do risco identificado para esta acao          |
| 5 | Probabilidade | `PROBABILIDADE` | LISTA    | 1   | Nao   | B=Baixa, M=Media, A=Alta                                |
| 6 | Impacto       | `IMPACTO`       | LISTA    | 1   | Nao   | B=Baixo, M=Medio, A=Alto                                |
| 7 | Nivel Risco   | `NIVELRISCO`    | LISTA    | 1   | Nao   | readOnly. Calculado: BB=Baixo, outros com A=Alto        |
| 8 | Mitigacao     | `MITIGACAO`     | HTML     | -   | Nao   | Acao para reduzir o risco identificado                  |
| 9 | Aberto por    | `CODUSU`        | PESQUISA | -   | Nao   | readOnly. Auto: $ctx_usuario_logado. visivel=N          |

## Action Buttons
| Botao      | Classe Java                            | Instancia       | Descricao                           |
|:-----------|:---------------------------------------|:----------------|:------------------------------------|
| Mudar Fase | `MudarFaseAvaliacaoRiscoActionButton`  | `QmGmAvaliacao` | Avanca GM para Fase 5 (Aprovacao)   |
| Voltar Fase| `VoltarFaseAvaliacaoRiscoActionButton` | `QmGmAvaliacao` | Retorna GM para Fase 3 (Acoes)      |

## Artefatos
- [ ] `datadictionary/THGQMGGMAVAL.xml`
- [ ] `dbscripts/V1.xml` — DDL THGQMGGMAVAL
- [ ] `model/.../actionButtons/MudarFaseAvaliacaoRiscoActionButton.java`
- [ ] `model/.../actionButtons/VoltarFaseAvaliacaoRiscoActionButton.java`
