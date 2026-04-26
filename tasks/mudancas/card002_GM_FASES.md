# Card GM-02 — Dominio Fases GM

## Identificacao
| Atributo        | Valor                                 |
|:----------------|:--------------------------------------|
| Modulo          | Gestao de Mudancas                    |
| Fase Roadmap    | Fase 3                                |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 6.3 |
| Ordem no modulo | 002 de 008                            |
| Depende de      | —                                     |

## Tabela
| Atributo  | Valor               |
|:----------|:--------------------|
| Nome      | `THGQMGGMFASE`       |
| Instancia | `ThgGmFase`          |
| Sequencia | MANUAL (`CODFASEGM`) |
| Dual-DB   | Oracle + SQL Server |

## Campos
| # | Rotulo     | Coluna       | Tipo     | Tam | Obrig | Opcoes                                 |
|:--|:-----------|:-------------|:---------|:----|:------|:---------------------------------------|
| 1 | Id Fase    | `CODFASEGM`   | INTEIRO  | -   | PK    | readOnly                               |
| 2 | Descricao  | `DESCFASEGM` | TEXTO    | 100 | Sim   | isPresentation=S                       |
| 3 | Ativo      | `ATIVO`      | CHECKBOX | 1   | Nao   | Default S                              |

## Dados Iniciais
| CODFASEGM | DESCFASEGM                        |
|:---------|:----------------------------------|
| 1        | Cadastro da Gestao de Mudanca     |
| 2        | Questionario de Avaliacao         |
| 3        | Acoes da Gestao de Mudanca        |
| 4        | Avaliacao de Riscos das Acoes     |
| 5        | Aprovacao da Proposta             |

## Artefatos
- [ ] `datadictionary/THGQMGGMFASE.xml`
- [ ] `dbscripts/V1.xml` — DDL + INSERT 5 fases
