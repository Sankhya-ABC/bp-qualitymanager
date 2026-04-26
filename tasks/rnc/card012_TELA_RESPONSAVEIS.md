# Card 012 — Tela Responsaveis da RNC

## Identificacao
| Atributo        | Valor                                         |
|:----------------|:----------------------------------------------|
| Modulo          | RNC — Registro de Nao Conformidades                       |
| Fase Roadmap    | Fase 1 — Core + Modulo NC/RNC                 |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 10.2        |
| Ordem no modulo | 012 de 014                                    |
| Depende de      | Card 001 (THGQMGREG)                    |

---

## Tabela Principal

| Atributo  | Valor                   |
|:----------|:------------------------|
| Nome      | `THGQMGRESP`    |
| Instancia | `ThgRncResponsavel`       |
| Sequencia | AUTO (`CODRESP`)  |
| Dual-DB   | Oracle + SQL Server     |

---

## Campos

| # | Rotulo        | Coluna           | Tipo     | Tam | Obrig | Aba    | Grupo | Opcoes / Comportamento                                              |
|:--|:--------------|:-----------------|:---------|:----|:------|:-------|:------|:--------------------------------------------------------------------|
| 1 | Id            | `CODRESP`  | INTEIRO  | -   | PK    | __main | -     | readOnly, auto sequence                                             |
| 2 | RNC           | `CODRNC`           | INTEIRO  | -   | Sim   | __main | -     | readOnly. FK para THGQMGREG.CODRNC                              |
| 3 | Fase          | `ORIGEM`         | LISTA    | 1   | Sim   | __main | -     | A=Registro, B=Acoes Imediatas, C=Causa Raiz, D=Acoes Corretivas, E=Implementacao, F=Verificacao Eficacia, G=Validada, H=Riscos, I=Liberacao |
| 4 | Parceiro      | `CODPARC`        | PESQUISA | -   | Sim   | __main | -     | targetInstance=Parceiro. Responsavel pela fase indicada             |
| 5 | Enviar Email  | `ENVIAREMAIL`    | CHECKBOX | 1   | Nao   | __main | -     | S=Envia notificacao por e-mail. Usa TGFPAR.EMAIL                    |

---

## Action Buttons

| Botao              | Classe Java                    | Instancia          | Transacao | Descricao                                         |
|:-------------------|:-------------------------------|:-------------------|:----------|:--------------------------------------------------|
| Enviar Notificacao | `NotificarRncActionButton`     | `ThgRncResponsavel`  | AUTOMATIC | Envia e-mail ao parceiro. Usa EMAIL de TGFPAR     |

---

## Regras de Negocio

1. Grid filha da tela de Registro de RNC (Card 001) — aparece como aba na tela principal
2. Multiplos responsaveis por RNC — um por fase, ou varios por fase se necessario
3. ORIGEM diferencia a fase de responsabilidade do parceiro
4. ENVIAREMAIL='S' aciona o envio de notificacao via MSDFilaMensagem para o EMAIL de TGFPAR
5. Botao Enviar Notificacao gera e-mail com assunto e mensagem especificos por ORIGEM (fase)

---

## Logica de notificacao por ORIGEM

| ORIGEM | Assunto do e-mail                             |
|:-------|:----------------------------------------------|
| A      | Responsavel pelo Registro da RNC {NURNC}       |
| B      | Responsavel pelas Acoes Imediatas da RNC {N}   |
| C      | Responsavel pela Causa Raiz da RNC {N}         |
| D      | Responsavel pelas Acoes Corretivas da RNC {N}  |
| E      | Responsavel pela Implementacao da RNC {N}      |
| F      | Responsavel pela Verificacao de Eficacia {N}   |
| H      | Responsavel por Riscos e Oportunidades {N}     |
| I      | Responsavel pela Liberacao de Produto {N}      |

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGRESP, Listener BI, sequence

### Dicionario de Dados
- [ ] `datadictionary/THGQMGRESP.xml` — instancia ThgRncResponsavel, relacao com Parceiro

### Backend Java
- [ ] `model/.../actionButtons/NotificarRncActionButton.java`
- [ ] `model/.../services/NotificacaoRncService.java` (metodo enviaNotificacaoPorFase)

### Menu
- Nao aparece no menu — acessada como grid filha do Card 001 (Registro de RNC)
