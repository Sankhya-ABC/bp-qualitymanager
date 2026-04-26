# Card 008 — Tela Implementacao (Fase 8)

## Identificacao
| Atributo        | Valor                                         |
|:----------------|:----------------------------------------------|
| Modulo          | RNC — Registro de Nao Conformidades                       |
| Fase Roadmap    | Fase 1 — Core + Modulo NC/RNC                 |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 10.2.1f     |
| Ordem no modulo | 008 de 014                                    |
| Depende de      | Card 001 (THGQMGREG)                    |

---

## Tabela Principal

| Atributo  | Valor                   |
|:----------|:------------------------|
| Nome      | `THGQMGIMPL`  |
| Instancia | `QmRncImplementacao`     |
| Sequencia | AUTO (`CODIMPL`)|
| Dual-DB   | Oracle + SQL Server     |

---

## Campos

| # | Rotulo              | Coluna             | Tipo      | Tam | Obrig | Aba    | Grupo | Opcoes / Comportamento                                     |
|:--|:--------------------|:-------------------|:----------|:----|:------|:-------|:------|:-----------------------------------------------------------|
| 1 | Id Implementacao    | `CODIMPL`  | INTEIRO   | -   | PK    | __main | -     | readOnly, auto sequence                                    |
| 2 | RNC                 | `CODRNC`             | INTEIRO   | -   | Sim   | __main | -     | readOnly. FK para THGQMGREG.CODRNC                     |
| 3 | Descricao           | `DESCRICAO`        | HTML      | -   | Sim   | __main | -     | Registro do que foi implementado                           |
| 4 | Responsavel         | `CODPARC`          | PESQUISA  | -   | Nao   | __main | -     | targetInstance=Parceiro. Quem executou a implementacao     |
| 5 | Data Implementacao  | `DTIMPLEMENTACAO`  | DATA_HORA | -   | Nao   | __main | -     | Data em que a acao foi implementada                        |
| 6 | Nao se Aplica       | `NAOSEAPLICA`      | CHECKBOX  | 1   | Nao   | __main | -     | Permite avancar sem preencher                              |
| 7 | Aberto por          | `CODUSU`           | PESQUISA  | -   | Nao   | __main | -     | readOnly. Auto: $ctx_usuario_logado. visivel=N             |
| 8 | Criado em           | `DHCREATE`        | DATA_HORA | -   | Nao   | __main | -     | readOnly. Auto: $ctx_dh_atual. visivel=N                   |
| 9 | Alterado em         | `DHALTER`          | DATA_HORA | -   | Nao   | __main | -     | readOnly. Trigger BIU. visivel=N                           |

---

## Tabelas Filhas (grids na tela)

| Tabela filha     | Instancia filha   | Vinculo FK        | Descricao                                            |
|:-----------------|:------------------|:------------------|:-----------------------------------------------------|
| `THGQMGEVID`| `QmRncEvidencia`  | `CODRNC` + ORIGEM=2 | Evidencias de implementacao (ORIGEM=2 nesta fase)    |

---

## Action Buttons

| Botao              | Classe Java                           | Instancia           | Transacao | Descricao                                       |
|:-------------------|:--------------------------------------|:--------------------|:----------|:------------------------------------------------|
| Mudar Fase         | `MudarFaseImplementacaoActionButton`  | `QmRncImplementacao` | AUTOMATIC | Avanca RNC para Fase 9 (Liberacao de Produto)   |
| Voltar Fase        | `VoltarFaseImplementacaoActionButton` | `QmRncImplementacao` | AUTOMATIC | Retorna RNC para Fase 7 (Riscos)                |
| Cancelar RNC       | `CancelarRncImplementacaoActionButton`| `QmRncImplementacao` | AUTOMATIC | Cancela a RNC (STATUS='C')                      |
| Enviar Notificacao | `NotificarImplementacaoActionButton`  | `QmRncImplementacao` | AUTOMATIC | Envia e-mail ao responsavel                     |

---

## Regras de Negocio

1. Tela so e acessivel quando THGQMGREG.CODFASE = 8
2. Campo DESCRICAO e obrigatorio para avancar, exceto se NAOSEAPLICA='S'
3. Evidencias desta fase usam ORIGEM=2 (Implementacao) em THGQMGEVID
4. Toda mudanca registra entrada em THGQMGLOG

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGIMPL, triggers, sequence

### Dicionario de Dados
- [ ] `datadictionary/THGQMGIMPL.xml`

### Backend Java
- [ ] `model/.../actionButtons/MudarFaseImplementacaoActionButton.java`
- [ ] `model/.../actionButtons/VoltarFaseImplementacaoActionButton.java`
- [ ] `model/.../actionButtons/CancelarRncImplementacaoActionButton.java`
- [ ] `model/.../actionButtons/NotificarImplementacaoActionButton.java`

### Menu
- [x] `datadictionary/menu.xml` — instancia QmRncImplementacao ja registrada como item 08
