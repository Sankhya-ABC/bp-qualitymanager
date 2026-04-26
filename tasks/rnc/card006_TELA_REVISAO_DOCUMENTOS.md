# Card 006 — Tela Revisao de Documentos (Fase 6)

## Identificacao
| Atributo        | Valor                                         |
|:----------------|:----------------------------------------------|
| Modulo          | RNC — Nao Conformidades                       |
| Fase Roadmap    | Fase 1 — Core + Modulo NC/RNC                 |
| ISO             | ABNT NBR ISO 9001:2015 — Clausulas 7.5 + 10.2 |
| Ordem no modulo | 006 de 014                                    |
| Depende de      | Card 001 (THGQMGREG)                    |

---

## Tabela Principal

| Atributo  | Valor                  |
|:----------|:-----------------------|
| Nome      | `THGQMGREVDOC`   |
| Instancia | `QmNcRevisaoDoc`       |
| Sequencia | AUTO (`CODREVDOC`)  |
| Dual-DB   | Oracle + SQL Server    |

---

## Campos

| # | Rotulo              | Coluna          | Tipo      | Tam | Obrig | Aba    | Grupo | Opcoes / Comportamento                                              |
|:--|:--------------------|:----------------|:----------|:----|:------|:-------|:------|:--------------------------------------------------------------------|
| 1 | Id Revisao          | `CODREVDOC`  | INTEIRO   | -   | PK    | __main | -     | readOnly, auto sequence                                             |
| 2 | RNC                 | `CODRNC`          | INTEIRO   | -   | Sim   | __main | -     | readOnly. FK para THGQMGREG.CODRNC                              |
| 3 | Documento Afetado   | `DOCUMENTO`     | TEXTO     | 200 | Nao   | __main | -     | Nome ou codigo do documento que precisa ser revisado                |
| 4 | Motivo da Revisao   | `MOTIVO`        | HTML      | -   | Nao   | __main | -     | Descricao do motivo que gerou a necessidade de revisao              |
| 5 | Responsavel Revisao | `CODPARC`       | PESQUISA  | -   | Nao   | __main | -     | targetInstance=Parceiro. Quem fara a revisao do documento           |
| 6 | Prazo               | `DTPRAZO`       | DATA_HORA | -   | Nao   | __main | -     | Prazo para conclusao da revisao                                     |
| 7 | Nao se Aplica       | `NAOSEAPLICA`   | CHECKBOX  | 1   | Nao   | __main | -     | S=Nenhum documento precisa ser revisado. Avanca sem preencher       |
| 8 | Aberto por          | `CODUSU`        | PESQUISA  | -   | Nao   | __main | -     | readOnly. Auto: $ctx_usuario_logado. visivel=N                      |
| 9 | Criado em           | `DHCREATE`     | DATA_HORA | -   | Nao   | __main | -     | readOnly. Auto: $ctx_dh_atual. visivel=N                            |
| 10| Alterado em         | `DHALTER`       | DATA_HORA | -   | Nao   | __main | -     | readOnly. Trigger BIU. visivel=N                                    |

---

## Action Buttons

| Botao        | Classe Java                          | Instancia        | Transacao | Descricao                              |
|:-------------|:-------------------------------------|:-----------------|:----------|:---------------------------------------|
| Mudar Fase   | `MudarFaseRevisaoDocActionButton`    | `QmNcRevisaoDoc` | AUTOMATIC | Avanca RNC para Fase 7 (Riscos)        |
| Voltar Fase  | `VoltarFaseRevisaoDocActionButton`   | `QmNcRevisaoDoc` | AUTOMATIC | Retorna RNC para Fase 5 (Acoes Corret.)|
| Cancelar RNC | `CancelarRncRevisaoDocActionButton`  | `QmNcRevisaoDoc` | AUTOMATIC | Cancela a RNC (STATUS='C')             |

---

## Regras de Negocio

1. Tela so e acessivel quando THGQMGREG.CODFASE = 6
2. Se NAOSEAPLICA='S', avanca sem obrigatoriedade de preencher documento
3. Podem existir multiplos registros por RNC (multiplos documentos afetados)
4. Toda mudanca registra entrada em THGQMGLOG

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGREVDOC, triggers, sequence

### Dicionario de Dados
- [ ] `datadictionary/THGQMGREVDOC.xml`

### Backend Java
- [ ] `model/.../actionButtons/MudarFaseRevisaoDocActionButton.java`
- [ ] `model/.../actionButtons/VoltarFaseRevisaoDocActionButton.java`
- [ ] `model/.../actionButtons/CancelarRncRevisaoDocActionButton.java`

### Menu
- [x] `datadictionary/menu.xml` — instancia QmNcRevisaoDoc ja registrada como item 06
