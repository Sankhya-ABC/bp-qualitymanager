# Card GM-01 — Cadastro de Gestao de Mudanca

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Gestao de Mudancas                       |
| Fase Roadmap    | Fase 3                                   |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 6.3    |
| Ordem no modulo | 001 de 008                               |
| Depende de      | CORE-01                                  |

---

## Tabela

| Atributo  | Valor               |
|:----------|:--------------------|
| Nome      | `THGQMGGM`     |
| Instancia | `ThgGmCadastro`      |
| Sequencia | AUTO (`CODGM`)   |
| Dual-DB   | Oracle + SQL Server |

### Instancias da tela
| Instancia      | Tipo       | Descricao                                      |
|:---------------|:-----------|:-----------------------------------------------|
| `ThgGmConsulta` | Lista/Grid | Visao de todas as mudancas com filtros         |
| `ThgGmCadastro` | Formulario | Formulario completo de registro da mudanca     |

---

## Campos

| # | Rotulo              | Coluna          | Tipo      | Tam | Obrig | Aba    | Grupo       | Opcoes / Comportamento                                          |
|:--|:--------------------|:----------------|:----------|:----|:------|:-------|:------------|:----------------------------------------------------------------|
| 1 | Id Gestao           | `CODGM`      | INTEIRO   | -   | PK    | __main | -           | readOnly, auto sequence                                         |
| 2 | Numero              | `NUMGM`         | TEXTO     | 20  | Nao   | __main | -           | readOnly. Trigger: GM-YYYY-NNNN. isPresentation=S              |
| 3 | Status              | `STATUS`        | LISTA     | 1   | Nao   | __main | -           | readOnly. A=Em andamento, C=Aprovada, R=Reprovada, X=Cancelada |
| 4 | Fase Atual          | `CODFASE`        | INTEIRO   | -   | Nao   | __main | -           | readOnly. Default 1. FK para THGQMGGMFASE                       |
| 5 | Processo Afetado    | `PROCESSO`      | TEXTO     | 200 | Sim   | __main | Identificacao| Nome do processo que sera modificado                          |
| 6 | Tipo de Mudanca     | `TIPOMUDANCA`   | LISTA     | 2   | Sim   | __main | Identificacao| PR=Processo, PD=Produto, SG=Sistema de Gestao, IN=Infraestrutura |
| 7 | Situacao Atual      | `SITUACAOATUAL` | HTML      | -   | Sim   | __main | Descricao   | Descricao da situacao antes da mudanca                         |
| 8 | Proposta de Mudanca | `PROPOSTA`      | HTML      | -   | Sim   | __main | Descricao   | O que se propoe mudar e por que                                |
| 9 | Justificativa       | `JUSTIFICATIVA` | HTML      | -   | Nao   | __main | Descricao   | Motivacao da mudanca                                           |
| 10| Responsavel         | `CODPARC`       | PESQUISA  | -   | Nao   | __main | -           | targetInstance=Parceiro. Responsavel pela conducao             |
| 11| Data Prevista       | `DTPREVISTA`    | DATA_HORA | -   | Nao   | __main | Datas       | Quando a mudanca sera implementada                             |
| 12| Data Implementacao  | `DTIMPL`        | DATA_HORA | -   | Nao   | __main | Datas       | readOnly. Preenchido ao aprovar                                |
| 13| Aberto por          | `CODUSU`        | PESQUISA  | -   | Nao   | __main | -           | readOnly. Auto: $ctx_usuario_logado. visivel=N                 |
| 14| Criado em           | `DHCREATE`     | DATA_HORA | -   | Nao   | __main | -           | readOnly. Auto: $ctx_dh_atual. visivel=N                       |

---

## Tabelas Filhas (grids na tela)

| Tabela filha          | Instancia filha     | Vinculo FK  | Descricao                                      |
|:----------------------|:--------------------|:------------|:-----------------------------------------------|
| `THGQMGGMACAO`         | `ThgGmAcao`          | `CODGM`  | Acoes 5W2H — card GM-03                        |
| `THGQMGGMRESP`  | `ThgGmResponsavel`   | `CODGM`  | Aprovadores — card GM-06                       |
| `THGQMGGMQUEST` | `ThgGmQuestionario`  | `CODGM`  | Questionarios criados automaticamente — GM-04  |

---

## Action Buttons

| Botao        | Classe Java                    | Instancia      | Transacao | Descricao                                            |
|:-------------|:-------------------------------|:---------------|:----------|:-----------------------------------------------------|
| Mudar Fase   | `MudarFaseGmActionButton`      | `ThgGmCadastro` | AUTOMATIC | Avanca para proxima fase. Cria questionarios auto nas fases 2, 4 e 5 |
| Cancelar GM  | `CancelarGmActionButton`       | `ThgGmCadastro` | AUTOMATIC | STATUS='X', encerra workflow, audit log              |

---

## Regras de Negocio

1. NUMGM gerado pelo Listener BI: formato GM-YYYY-NNNN
2. STATUS inicia como 'A', CODFASE inicia como 1
3. Questionarios criados automaticamente pelo FaseGmBusinessService nas fases 2, 4 e 5
4. STATUS='C' (Aprovada) apenas quando todos os aprovadores confirmaram
5. Toda mudanca de STATUS ou FASE registra em THGQMGLOG

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGGM, Listener BI (NUMGM), sequence, indices

### Dicionario de Dados
- [ ] `datadictionary/THGQMGGM.xml` — 2 instancias, grupos Identificacao, Descricao, Datas

### Backend Java
- [ ] `model/.../actionButtons/MudarFaseGmActionButton.java`
- [ ] `model/.../actionButtons/CancelarGmActionButton.java`
- [ ] `model/.../services/FaseGmBusinessService.java`

### Menu
- [ ] `datadictionary/menu.xml` — instancias ThgGmConsulta e ThgGmCadastro em pasta Gestao de Mudancas
