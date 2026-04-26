# Card GM-06 — Aprovacao da Mudanca

## Identificacao
| Atributo        | Valor                                   |
|:----------------|:----------------------------------------|
| Modulo          | Gestao de Mudancas                      |
| Fase Roadmap    | Fase 3                                  |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 6.3c  |
| Ordem no modulo | 006 de 008                              |
| Depende de      | GM-01                                   |

## Tabela
| Atributo  | Valor                   |
|:----------|:------------------------|
| Nome      | `THGQMGGMRESP`    |
| Instancia | `ThgGmAprovacao`         |
| Sequencia | AUTO (`CODRESPGM`)       |
| Dual-DB   | Oracle + SQL Server     |

## Campos
| # | Rotulo        | Coluna          | Tipo      | Tam | Obrig | Opcoes                                               |
|:--|:--------------|:----------------|:----------|:----|:------|:-----------------------------------------------------|
| 1 | Id            | `CODRESPGM`      | INTEIRO   | -   | PK    | readOnly, auto sequence                              |
| 2 | Gestao        | `CODGM`      | INTEIRO   | -   | Sim   | readOnly. FK para THGQMGGM                      |
| 3 | Aprovador     | `CODPARC`       | PESQUISA  | -   | Sim   | targetInstance=Parceiro. Aprovador responsavel       |
| 4 | Status        | `STATUS`        | LISTA     | 1   | Nao   | P=Pendente, C=Aprovado, R=Reprovado                  |
| 5 | Justificativa | `JUSTIFICATIVA` | TEXTO     | 500 | Nao   | Obrigatoria se STATUS='R' (Reprovado)                |
| 6 | Data Decisao  | `DTDECISAO`     | DATA_HORA | -   | Nao   | readOnly. Preenchido ao aprovar ou reprovar          |

## Action Buttons
| Botao      | Classe Java                              | Instancia       | Descricao                                  |
|:-----------|:-----------------------------------------|:----------------|:-------------------------------------------|
| Mudar Fase | `MudarFaseAprovacaoFinalActionButton`    | `ThgGmAprovacao` | Todos aprovados -> STATUS GM='C', DTIMPL   |
| Voltar Fase| `VoltarFaseAprovacaoFinalActionButton`   | `ThgGmAprovacao` | Retorna para Fase 4 (Avaliacao Riscos)     |

## Regras de Negocio
1. Multiplos aprovadores — TODOS devem ter STATUS='C' para a mudanca ser aprovada
2. Qualquer STATUS='R' => THGQMGGM.STATUS='R', notificacao ao responsavel
3. Justificativa obrigatoria para reprovacao — validacao no action button
4. Ao aprovacao total: THGQMGGM.STATUS='C', DTIMPL=now(), audit log

## Artefatos
- [ ] `datadictionary/THGQMGGMRESP.xml`
- [ ] `dbscripts/V1.xml` — DDL THGQMGGMRESP
- [ ] `model/.../actionButtons/MudarFaseAprovacaoFinalActionButton.java`
- [ ] `model/.../actionButtons/VoltarFaseAprovacaoFinalActionButton.java`
