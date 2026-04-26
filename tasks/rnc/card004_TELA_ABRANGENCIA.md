# Card 004 — Tela Abrangencia 5W2H (Fase 4)

## Identificacao
| Atributo        | Valor                                          |
|:----------------|:-----------------------------------------------|
| Modulo          | RNC — Registro de Nao Conformidades                        |
| Fase Roadmap    | Fase 1 — Core + Modulo NC/RNC                  |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 10.2.1d      |
| Ordem no modulo | 004 de 014                                     |
| Depende de      | Card 001 (THGQMGREG), Card 003           |

---

## Tabelas Principais

### THGQMGABR — cabecalho 5W2H

| Atributo  | Valor                   |
|:----------|:------------------------|
| Nome      | `THGQMGABR`    |
| Instancia | `ThgRncAbrangencia`       |
| Sequencia | AUTO (`CODABR`)  |
| Dual-DB   | Oracle + SQL Server     |

### THGQMGRNCQUEM — responsaveis da abrangencia (grid filho)

| Atributo  | Valor              |
|:----------|:-------------------|
| Nome      | `THGQMGRNCQUEM`      |
| Instancia | `ThgRncQuem`         |
| Sequencia | AUTO (`CODQUEM`)    |
| Dual-DB   | Oracle + SQL Server|

---

## Campos — THGQMGABR

| # | Rotulo         | Coluna          | Tipo      | Tam | Obrig | Aba     | Grupo | Opcoes / Comportamento                              |
|:--|:---------------|:----------------|:----------|:----|:------|:--------|:------|:----------------------------------------------------|
| 1 | Id Abrangencia | `CODABR` | INTEIRO   | -   | PK    | __main  | -     | readOnly, auto sequence                             |
| 2 | RNC            | `CODRNC`          | INTEIRO   | -   | Sim   | __main  | -     | readOnly. FK para THGQMGREG.CODRNC              |
| 3 | O Que          | `OQUE`          | TEXTO     | 500 | Nao   | __main  | 5W2H  | O que deve ser feito — primeira pergunta do 5W2H    |
| 4 | Como           | `COMO`          | TEXTO     | 500 | Nao   | __main  | 5W2H  | Como sera feito                                     |
| 5 | Onde           | `ONDE`          | TEXTO     | 500 | Nao   | __main  | 5W2H  | Onde sera feito                                     |
| 6 | Por Que        | `PORQUE`        | TEXTO     | 500 | Nao   | __main  | 5W2H  | Por que deve ser feito                              |
| 7 | Quando         | `QUANDO`        | DATA_HORA | -   | Nao   | __main  | 5W2H  | Data prevista para execucao                         |
| 8 | Quanto         | `QUANTO`        | TEXTO     | 200 | Nao   | __main  | 5W2H  | Custo estimado ou quantidade envolvida              |
| 9 | Nao se Aplica  | `NAOSEAPLICA`   | CHECKBOX  | 1   | Nao   | __main  | -     | Permite avancar sem preencher o 5W2H                |
| 10| Aberto por     | `CODUSU`        | PESQUISA  | -   | Nao   | __main  | -     | readOnly. Auto: $ctx_usuario_logado. visivel=N      |
| 11| Criado em      | `DHCREATE`     | DATA_HORA | -   | Nao   | __main  | -     | readOnly. Auto: $ctx_dh_atual. visivel=N            |
| 12| Alterado em    | `DHALTER`       | DATA_HORA | -   | Nao   | __main  | -     | readOnly. Trigger BIU. visivel=N                    |

## Campos — THGQMGRNCQUEM (grid filho da abrangencia)

| # | Rotulo      | Coluna          | Tipo     | Tam | Obrig | Aba     | Grupo | Opcoes / Comportamento                           |
|:--|:------------|:----------------|:---------|:----|:------|:--------|:------|:-------------------------------------------------|
| 1 | Id          | `CODQUEM`        | INTEIRO  | -   | PK    | __main  | -     | readOnly, auto sequence                          |
| 2 | Abrangencia | `CODABR` | INTEIRO  | -   | Sim   | __main  | -     | readOnly. FK para THGQMGABR             |
| 3 | Parceiro    | `CODPARC`       | PESQUISA | -   | Sim   | __main  | -     | targetInstance=Parceiro, targetField=CODPARC. Responsavel que deve agir |
| 4 | Enviar Email| `ENVIAREMAIL`   | CHECKBOX | 1   | Nao   | __main  | -     | S=Envia notificacao por e-mail ao parceiro       |

---

## Tabelas Filhas (grids na tela)

| Tabela filha    | Instancia filha | Vinculo FK       | Descricao                                          |
|:----------------|:----------------|:-----------------|:---------------------------------------------------|
| `THGQMGRNCQUEM`   | `ThgRncQuem`      | `CODABR`  | Grid de responsaveis — quem deve agir na abrangencia |

---

## Action Buttons

| Botao              | Classe Java                           | Instancia          | Transacao | Descricao                                        |
|:-------------------|:--------------------------------------|:-------------------|:----------|:-------------------------------------------------|
| Mudar Fase         | `MudarFaseAbrangenciaActionButton`    | `ThgRncAbrangencia`  | AUTOMATIC | Avanca RNC para Fase 5 (Acoes Corretivas)        |
| Voltar Fase        | `VoltarFaseAbrangenciaActionButton`   | `ThgRncAbrangencia`  | AUTOMATIC | Retorna RNC para Fase 3 (Causa Raiz)             |
| Cancelar RNC       | `CancelarRncAbrangenciaActionButton`  | `ThgRncAbrangencia`  | AUTOMATIC | Cancela a RNC (STATUS='C')                       |
| Enviar Notificacao | `NotificarQuemActionButton`           | `ThgRncQuem`         | AUTOMATIC | Envia e-mail ao parceiro selecionado na grid     |

---

## Regras de Negocio

1. Tela so e acessivel quando THGQMGREG.CODFASE = 4
2. Ao menos um campo 5W2H deve ser preenchido para avancar, exceto se NAOSEAPLICA='S'
3. Grid THGQMGRNCQUEM pode ter multiplos responsaveis (N registros por CODABR)
4. ENVIAREMAIL='S' na grid aciona o botao Enviar Notificacao para aquele parceiro
5. O botao Notificar usa TGFPAR.QM_EMAILQUESTIONARIO como e-mail de destino
6. Toda mudanca registra entrada em THGQMGLOG
7. Campos 5W2H ficam todos em aba __main (diferente de Acoes Corretivas que tem aba separada)

---

## Gatilhos e Automatismos

| Evento           | O que acontece                                      | Onde implementar      |
|:-----------------|:----------------------------------------------------|:----------------------|
| INSERT           | CODABR via sequence, DHCREATE=now()         | Listener BI   |
| UPDATE           | DHALTER = SYSDATE/GETDATE()                         | Trigger BIU           |
| Mudar Fase (btn) | CODFASE=5, registra THGQMGREGFASE, audit log      | FaseRncBusinessService |
| Notificar (btn)  | Busca QM_EMAILQUESTIONARIO em TGFPAR, MSDFilaMensagem | NotificacaoRncService |

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGABR + THGQMGRNCQUEM, triggers, sequences, FK entre elas

### Dicionario de Dados
- [ ] `datadictionary/THGQMGABR.xml` — instancia ThgRncAbrangencia, grupo 5W2H em __main
- [ ] `datadictionary/THGQMGRNCQUEM.xml` — instancia ThgRncQuem, relacionamento com Parceiro

### Backend Java
- [ ] `model/.../actionButtons/MudarFaseAbrangenciaActionButton.java`
- [ ] `model/.../actionButtons/VoltarFaseAbrangenciaActionButton.java`
- [ ] `model/.../actionButtons/CancelarRncAbrangenciaActionButton.java`
- [ ] `model/.../actionButtons/NotificarQuemActionButton.java`
- [ ] `model/.../services/NotificacaoRncService.java`

### Menu
- [x] `datadictionary/menu.xml` — instancia ThgRncAbrangencia ja registrada como item 04

---

## Observacoes Tecnicas

- 5W2H fica em aba __main (diferente de Acoes Corretivas — card005 — que usa aba separada)
- THGQMGRNCQUEM referencia TGFPAR via PESQUISA — sem CREATE TABLE FK
- Notificacao usa TGFPAR.QM_EMAILQUESTIONARIO — campo adicionado na tabela nativa (Card FORN-08)
- instanceName: `ThgRncAbrangencia` e `ThgRncQuem` — copiar exatamente do XML
