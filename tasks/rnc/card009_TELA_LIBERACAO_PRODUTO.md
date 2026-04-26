# Card 009 — Tela Liberacao de Produto (Fase 9)

## Identificacao
| Atributo        | Valor                                         |
|:----------------|:----------------------------------------------|
| Modulo          | RNC — Registro de Nao Conformidades                       |
| Fase Roadmap    | Fase 1 — Core + Modulo NC/RNC                 |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 8.6         |
| Ordem no modulo | 009 de 014                                    |
| Depende de      | Card 001 (THGQMGREG)                    |

---

## Tabela Principal

| Atributo  | Valor                |
|:----------|:---------------------|
| Nome      | `THGQMGLIB`   |
| Instancia | `QmRncLiberacao`      |
| Sequencia | AUTO (`CODLIB`) |
| Dual-DB   | Oracle + SQL Server  |

---

## Campos

| # | Rotulo                 | Coluna           | Tipo      | Tam | Obrig | Aba    | Grupo | Opcoes / Comportamento                                         |
|:--|:-----------------------|:-----------------|:----------|:----|:------|:-------|:------|:---------------------------------------------------------------|
| 1 | Id Liberacao           | `CODLIB`    | INTEIRO   | -   | PK    | __main | -     | readOnly, auto sequence                                        |
| 2 | RNC                    | `CODRNC`           | INTEIRO   | -   | Sim   | __main | -     | readOnly. FK para THGQMGREG.CODRNC                         |
| 3 | Produto/Lote           | `PRODUTO`        | TEXTO     | 200 | Nao   | __main | -     | Identificacao do produto ou lote sendo liberado                |
| 4 | Quantidade             | `QUANTIDADE`     | DECIMAL   | -   | Nao   | __main | -     | Quantidade do produto liberado                                 |
| 5 | Unidade                | `UNIDADE`        | TEXTO     | 20  | Nao   | __main | -     | Unidade de medida (kg, un, m², etc.)                           |
| 6 | Decisao                | `DECISAO`        | LISTA     | 1   | Nao   | __main | -     | L=Liberado, R=Retido, D=Descartado, D=Devolvido ao fornecedor  |
| 7 | Justificativa          | `JUSTIFICATIVA`  | HTML      | -   | Nao   | __main | -     | Justificativa da decisao de liberacao                          |
| 8 | Responsavel Liberacao  | `CODPARC`        | PESQUISA  | -   | Nao   | __main | -     | targetInstance=Parceiro. Quem autorizou a liberacao            |
| 9 | Data Liberacao         | `DTLIBERACAO`    | DATA_HORA | -   | Nao   | __main | -     | Data da decisao de liberacao                                   |
| 10| Nao se Aplica          | `NAOSEAPLICA`    | CHECKBOX  | 1   | Nao   | __main | -     | Fase nao aplicavel para este tipo de RNC                       |
| 11| Aberto por             | `CODUSU`         | PESQUISA  | -   | Nao   | __main | -     | readOnly. Auto: $ctx_usuario_logado. visivel=N                 |
| 12| Criado em              | `DHCREATE`      | DATA_HORA | -   | Nao   | __main | -     | readOnly. Auto: $ctx_dh_atual. visivel=N                       |

---

## Action Buttons

| Botao        | Classe Java                        | Instancia        | Transacao | Descricao                                     |
|:-------------|:-----------------------------------|:-----------------|:----------|:----------------------------------------------|
| Mudar Fase   | `MudarFaseLiberacaoActionButton`   | `QmRncLiberacao`  | AUTOMATIC | Avanca RNC para Fase 10 (Verificacao Eficacia) |
| Voltar Fase  | `VoltarFaseLiberacaoActionButton`  | `QmRncLiberacao`  | AUTOMATIC | Retorna RNC para Fase 8 (Implementacao)        |
| Cancelar RNC | `CancelarRncLiberacaoActionButton` | `QmRncLiberacao`  | AUTOMATIC | Cancela a RNC (STATUS='C')                     |

---

## Regras de Negocio

1. Tela so e acessivel quando THGQMGREG.CODFASE = 9
2. DECISAO e rastreavel — ISO 9001 §8.6 exige evidencia de quem autorizou a liberacao (CODPARC)
3. Se NAOSEAPLICA='S', avanca sem preencher (ex: RNC de processo, sem produto fisico)
4. Toda mudanca registra entrada em THGQMGLOG
5. Opcoes de DECISAO — L=Liberado, R=Retido (aguarda reprocessamento), D=Descartado (sucateado), V=Devolvido ao fornecedor

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGLIB, triggers, sequence

### Dicionario de Dados
- [ ] `datadictionary/THGQMGLIB.xml`

### Backend Java
- [ ] `model/.../actionButtons/MudarFaseLiberacaoActionButton.java`
- [ ] `model/.../actionButtons/VoltarFaseLiberacaoActionButton.java`
- [ ] `model/.../actionButtons/CancelarRncLiberacaoActionButton.java`

### Menu
- [x] `datadictionary/menu.xml` — instancia QmRncLiberacao ja registrada como item 09
