# Card 007 — Tela Riscos e Oportunidades (Fase 7)

## Identificacao
| Atributo        | Valor                                         |
|:----------------|:----------------------------------------------|
| Modulo          | RNC — Nao Conformidades                       |
| Fase Roadmap    | Fase 1 — Core + Modulo NC/RNC                 |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 6.1         |
| Ordem no modulo | 007 de 014                                    |
| Depende de      | Card 001 (THGQMGREG)                    |

---

## Tabela Principal

| Atributo  | Valor               |
|:----------|:--------------------|
| Nome      | `THGQMGRISCO`      |
| Instancia | `QmNcRisco`         |
| Sequencia | AUTO (`CODRISCO`)    |
| Dual-DB   | Oracle + SQL Server |

---

## Campos

| # | Rotulo           | Coluna       | Tipo      | Tam | Obrig | Aba    | Grupo | Opcoes / Comportamento                                                  |
|:--|:-----------------|:-------------|:----------|:----|:------|:-------|:------|:------------------------------------------------------------------------|
| 1 | Id Risco         | `CODRISCO`    | INTEIRO   | -   | PK    | __main | -     | readOnly, auto sequence                                                 |
| 2 | RNC              | `CODRNC`       | INTEIRO   | -   | Sim   | __main | -     | readOnly. FK para THGQMGREG.CODRNC                                  |
| 3 | Tipo             | `TIPORISCO`  | LISTA     | 1   | Nao   | __main | -     | R=Risco, O=Oportunidade                                                 |
| 4 | O Que            | `OQUE`       | TEXTO     | 500 | Nao   | __main | 5W2H  | Risco ou oportunidade identificado                                      |
| 5 | Como             | `COMO`       | TEXTO     | 500 | Nao   | __main | 5W2H  | Como abordar o risco ou capitalizar a oportunidade                      |
| 6 | Onde             | `ONDE`       | TEXTO     | 500 | Nao   | __main | 5W2H  | Onde o risco ou oportunidade se aplica                                  |
| 7 | Por Que          | `PORQUE`     | TEXTO     | 500 | Nao   | __main | 5W2H  | Por que e um risco ou oportunidade relevante                            |
| 8 | Quando           | `QUANDO`     | DATA_HORA | -   | Nao   | __main | 5W2H  | Prazo para tratar                                                       |
| 9 | Quanto           | `QUANTO`     | TEXTO     | 200 | Nao   | __main | 5W2H  | Impacto estimado                                                        |
| 10| Nao se Aplica    | `NAOSEAPLICA`| CHECKBOX  | 1   | Nao   | __main | -     | Permite avancar sem preencher — risco/oportunidade nao identificados    |
| 11| Aberto por       | `CODUSU`     | PESQUISA  | -   | Nao   | __main | -     | readOnly. Auto: $ctx_usuario_logado. visivel=N                          |
| 12| Criado em        | `DHCREATE`  | DATA_HORA | -   | Nao   | __main | -     | readOnly. Auto: $ctx_dh_atual. visivel=N                                |
| 13| Alterado em      | `DHALTER`    | DATA_HORA | -   | Nao   | __main | -     | readOnly. Trigger BIU. visivel=N                                        |

---

## Action Buttons

| Botao        | Classe Java                         | Instancia   | Transacao | Descricao                                |
|:-------------|:------------------------------------|:------------|:----------|:-----------------------------------------|
| Mudar Fase   | `MudarFaseRiscoActionButton`        | `QmNcRisco` | AUTOMATIC | Avanca RNC para Fase 8 (Implementacao)   |
| Voltar Fase  | `VoltarFaseRiscoActionButton`       | `QmNcRisco` | AUTOMATIC | Retorna RNC para Fase 6 (Revisao Doc)    |
| Cancelar RNC | `CancelarRncRiscoActionButton`      | `QmNcRisco` | AUTOMATIC | Cancela a RNC (STATUS='C')               |

---

## Regras de Negocio

1. Tela so e acessivel quando THGQMGREG.CODFASE = 7
2. Podem existir multiplos registros por RNC (multiplos riscos/oportunidades)
3. Se NAOSEAPLICA='S', avanca sem preencher
4. ATENCAO — Desvio de fluxo inverso: se ORIGEM da RNC for do tipo Riscos/Oportunidades, a RNC NAO passa pela Fase 7 — ela foi desviada da Fase 3 direto para a Fase 5. A Fase 7 so e visitada por RNCs com origem normal.
5. Toda mudanca registra entrada em THGQMGLOG

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGRISCO, triggers, sequence

### Dicionario de Dados
- [ ] `datadictionary/THGQMGRISCO.xml` — 5W2H em __main com grupo "5W2H"

### Backend Java
- [ ] `model/.../actionButtons/MudarFaseRiscoActionButton.java`
- [ ] `model/.../actionButtons/VoltarFaseRiscoActionButton.java`
- [ ] `model/.../actionButtons/CancelarRncRiscoActionButton.java`

### Menu
- [x] `datadictionary/menu.xml` — instancia QmNcRisco ja registrada como item 07
