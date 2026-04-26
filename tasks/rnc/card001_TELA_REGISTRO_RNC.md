# Card 001 — Tela Registro de RNC

## Identificacao
| Atributo        | Valor                                          |
|:----------------|:-----------------------------------------------|
| Modulo          | RNC — Nao Conformidades                        |
| Fase Roadmap    | Fase 1 — Core + Modulo NC/RNC                  |
| ISO             | ABNT NBR ISO 9001:2015 — Clausulas 8.7 + 10.2  |
| Ordem no modulo | 001 de 014                                     |
| Depende de      | CORE-03 (THGQMGPRIO), CORE-04 (THGQMGNCFASE) |

---

## Tabela Principal

| Atributo  | Valor                |
|:----------|:---------------------|
| Nome      | `THGQMGREG`    |
| Instancia | `QmNcRegistro`       |
| Sequencia | AUTO (`CODRNC`)        |
| Dual-DB   | Oracle + SQL Server  |

### Instancias da tela
| Instancia      | Tipo       | Descricao                                              |
|:---------------|:-----------|:-------------------------------------------------------|
| `QmNcConsulta` | Lista/Grid | Visao de consulta com filtros por status, prioridade, responsavel, periodo |
| `QmNcRegistro` | Formulario | Formulario completo de abertura da RNC                 |

---

## Campos

| # | Rotulo               | Coluna          | Tipo       | Tam | Obrig | Aba           | Grupo          | Opcoes / Comportamento                                             |
|:--|:---------------------|:----------------|:-----------|:----|:------|:--------------|:---------------|:-------------------------------------------------------------------|
| 1 | N RNC               | `CODRNC`          | INTEIRO    | -   | PK    | __main        | -              | readOnly, auto sequence SEQ_THGQMGREG                      |
| 2 | Numero RNC           | `NURNC`         | TEXTO      | 20  | Nao   | __main        | -              | readOnly, gerado pelo trigger: NC-YYYY-NNNN. isPresentation=S     |
| 3 | Status               | `STATUS`        | LISTA      | 1   | Nao   | __main        | -              | readOnly. A=Aberta, P=Em andamento, E=Encerrada, C=Cancelada, X=Nao procedente |
| 4 | Fase Atual           | `CODFASE`        | INTEIRO    | -   | Nao   | __main        | -              | readOnly. Default 1. FK para THGQMGNCFASE                          |
| 5 | Origem               | `ORIGEM`        | LISTA      | 2   | Sim   | __main        | Identificacao  | C=Cliente, F=Fornecedor, I=Interno, A=Auditoria, O=Outro          |
| 6 | Prioridade           | `PRIORIDADE`    | LISTA      | 1   | Sim   | __main        | Identificacao  | 1=Simples (prazo 30d), 2=Prioritario (prazo 15d), 3=Critico (prazo 5d) |
| 7 | Tipo                 | `TIPONC`        | LISTA      | 2   | Sim   | __main        | Identificacao  | P=Produto, S=Servico, PR=Processo, SG=Sistema de Gestao           |
| 8 | Parceiro/Fornecedor  | `CODPARC`       | PESQUISA   | -   | Nao   | __main        | Identificacao  | targetInstance=Parceiro, targetField=CODPARC. Nullable             |
| 9 | Nota Fiscal/Pedido   | `NUNOTA`        | INTEIRO    | -   | Nao   | __main        | Identificacao  | FK opcional para TGFCAB.NUNOTA. Nullable                          |
| 10 | Processo             | `PROCESSO`      | TEXTO      | 100 | Nao   | __main        | Identificacao  | Nome do processo interno afetado                                  |
| 11 | Data de Abertura     | `DTREGISTRO`    | DATA_HORA  | -   | Nao   | __main        | Datas          | readOnly. Auto: $ctx_dh_atual                                     |
| 12 | Previsao Encerramento| `DTPREVENCERRAR`| DATA_HORA  | -   | Nao   | __main        | Datas          | Calculado automaticamente com base na Prioridade + prazo          |
| 13 | Data Encerramento    | `DTENCERRAMENTO`| DATA_HORA  | -   | Nao   | __main        | Datas          | readOnly. Preenchido automaticamente ao encerrar (Fase 10 eficaz) |
| 14 | Detalhamento         | `DETALHAMENTO`  | HTML       | -   | Sim   | Detalhamento  | -              | Editor HTML rico. Descricao completa da nao conformidade          |
| 15 | Reincidente          | `REINCIDENTE`   | CHECKBOX   | 1   | Nao   | __main        | -              | readOnly. S=Sim, N=Nao. Preenchido automaticamente pelo sistema    |
| 16 | Nao Procedente       | `NAOPROCEDENTE` | CHECKBOX   | 1   | Nao   | __main        | -              | Marcado manualmente para encerrar como X (nao procedente)         |
| 17 | RNC Vinculada        | `NCVINCULADA`   | INTEIRO    | -   | Nao   | __main        | -              | FK opcional para outra THGQMGREG.CODRNC (reincidencia)        |
| 18 | Auditoria Origem     | `CODAUD`   | INTEIRO    | -   | Nao   | __main        | -              | FK opcional para THGQMGAUD.CODAUD. Nullable          |
| 19 | Aberto por           | `CODUSU`        | PESQUISA   | -   | Nao   | __main        | -              | readOnly. Auto: $ctx_usuario_logado. targetInstance=Usuario        |
| 20 | Criado em            | `DHCREATE`     | DATA_HORA  | -   | Nao   | __main        | -              | readOnly. Auto: $ctx_dh_atual. visivel=N                          |
| 21 | Alterado em          | `DHALTER`       | DATA_HORA  | -   | Nao   | __main        | -              | readOnly. Trigger BIU. visivel=N                                  |

---

## Tabelas Filhas (grids na tela)

| Tabela filha           | Instancia filha     | Vinculo FK | Descricao                                        |
|:-----------------------|:--------------------|:-----------|:-------------------------------------------------|
| `THGQMGRESP`   | `QmNcResponsavel`   | `CODRNC`     | Responsaveis da RNC por fase — aparece como grid |
| `THGQMGEVID`     | `QmNcEvidencia`     | `CODRNC`     | Arquivos/fotos vinculados a RNC                  |

---

## Action Buttons

| Botao              | Classe Java                       | Instancia           | Transacao | Descricao                                          |
|:-------------------|:----------------------------------|:--------------------|:----------|:---------------------------------------------------|
| Mudar Fase         | `MudarFaseRegistroActionButton`   | `QmNcRegistro`      | AUTOMATIC | Avanca para Fase 2 (Acoes Imediatas)               |
| Cancelar RNC       | `CancelarRncRegistroActionButton` | `QmNcRegistro`      | AUTOMATIC | Marca STATUS='C', encerra workflow                 |
| Enviar Notificacao | `NotificarRncActionButton`        | `QmNcResponsavel`   | AUTOMATIC | Envia e-mail ao responsavel via MSDFilaMensagem    |

---

## Regras de Negocio

1. STATUS inicia como `A` (Aberta) — definido pelo Listener BI, nao pelo usuario
2. CODFASE inicia como `1` (Registro) — definido pelo Listener BI
3. NURNC gerado automaticamente pelo Listener BI: formato `NC-YYYY-NNNN`
4. DTPREVENCERRAR calculada automaticamente: DTREGISTRO + prazo da Prioridade (30/15/5 dias)
5. Se NAOPROCEDENTE marcado como S, STATUS muda para `X` e DTENCERRAMENTO e preenchida
6. REINCIDENTE e marcado automaticamente pelo sistema ao detectar NC anterior com mesma origem+processo nao encerrada
7. Campo CODAUD so e visivel se ORIGEM = 'A' (Auditoria) — visibilidade condicional
8. Ao atingir a Fase 10 com verificacao eficaz, STATUS muda para `E` e DTENCERRAMENTO e preenchida automaticamente
9. RNC cancelada (STATUS='C') nao pode voltar para fase anterior — FaseNcBusinessService bloqueia
10. Toda mudanca de STATUS ou CODFASE gera registro em THGQMGLOG

---

## Gatilhos e Automatismos

| Evento                  | O que acontece                                                    | Onde implementar               |
|:------------------------|:------------------------------------------------------------------|:-------------------------------|
| INSERT                  | CODRNC via sequence, NURNC=NC-YYYY-NNNN, CODFASE=1, STATUS='A'     | Listener BI (V1.xml) |
| INSERT                  | DTPREVENCERRAR calculada pela prioridade                          | Listener BI |
| UPDATE                  | DHALTER = SYSDATE/GETDATE()                                       | Listener BIU |
| Mudar Fase (btn)        | CODFASE+1, registra em THGQMGREGFASE, grava THGQMGLOG | FaseNcBusinessService.avancarFase() |
| Voltar Fase (btn)       | CODFASE-1, registra em THGQMGREGFASE, bloqueia se STATUS='C'   | FaseNcBusinessService.retornarFase() |
| Cancelar RNC (btn)      | STATUS='C', DTENCERRAMENTO=now(), grava audit log                 | FaseNcBusinessService.cancelarRnc() |
| Fase 10 + Eficaz        | STATUS='E', DTENCERRAMENTO=now(), grava audit log                 | FaseNcBusinessService.encerrarRnc() |
| Reincidencia detectada  | REINCIDENTE='S', NCVINCULADA=CODRNC da NC anterior                  | NcReincidenciaListener         |

---

## Artefatos

### Banco de Dados
- [x] `dbscripts/V1.xml` — DDL THGQMGREG Oracle+SQL Server, Listener BI/BIU, indices, sequence

### Dicionario de Dados
- [x] `datadictionary/THGQMGREG.xml` — instancias QmNcConsulta e QmNcRegistro, todos os campos, relacionamentos

### Backend Java
- [ ] `model/.../actionButtons/MudarFaseRegistroActionButton.java`
- [ ] `model/.../actionButtons/CancelarRncRegistroActionButton.java`
- [ ] `model/.../actionButtons/NotificarRncActionButton.java`
- [ ] `model/.../services/FaseNcBusinessService.java`
- [ ] `model/.../listeners/NcReincidenciaListener.java`

### Menu
- [x] `datadictionary/menu.xml` — instancias QmNcConsulta e QmNcRegistro ja registradas

---

## Observacoes Tecnicas

- Prefixo THGQMG obrigatorio em todos os objetos de banco
- autoDDL = true — tabela criada pelo datadictionary automaticamente
- DDL manual no dbscripts apenas para: Listener BI/BIU, indices de performance, sequence
- FKs para TGFPAR e TGFCAB definidas apenas no datadictionary via PESQUISA (sem ALTER TABLE)
- instanceName dos @ActionButton e @Listener deve ser copiado EXATAMENTE do XML
- CODPARC, NUNOTA e CODAUD sao opcionais — RNC pode existir sem vinculo com ERP
- Audit log obrigatorio: THGQMGLOG registra toda mudanca de STATUS ou CODFASE
- Campo DETALHAMENTO usa dataType=HTML — editor rico de texto no Sankhya Om
