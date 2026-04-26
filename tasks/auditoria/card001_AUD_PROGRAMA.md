# Card AUD-01 — Programa de Auditoria

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Auditoria                                |
| Fase Roadmap    | Fase 3                                   |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 9.2.2a |
| Ordem no modulo | 001 de 009                               |
| Depende de      | CORE-01, AUD-08 (cadastro de auditores)  |

---

## Tabela
| Atributo  | Valor                  |
|:----------|:-----------------------|
| Nome      | `THGQMGAUDPROG`     |
| Instancia | `ThgAudPrograma`        |
| Sequencia | AUTO (`CODPROG`)    |
| Dual-DB   | Oracle + SQL Server    |

---

## Campos

| # | Rotulo              | Coluna         | Tipo      | Tam | Obrig | Aba    | Grupo | Opcoes / Comportamento                                               |
|:--|:--------------------|:---------------|:----------|:----|:------|:-------|:------|:---------------------------------------------------------------------|
| 1 | Id Programa         | `CODPROG`   | INTEIRO   | -   | PK    | __main | -     | readOnly, auto sequence                                              |
| 2 | Ano                 | `ANO`          | INTEIRO   | -   | Sim   | __main | -     | isPresentation=S. Ano de referencia do programa                      |
| 3 | Processo Auditado   | `PROCESSO`     | TEXTO     | 200 | Sim   | __main | -     | Nome do processo ou area que sera auditada                           |
| 4 | Norma ISO           | `NORMAISO`     | LISTA     | 4   | Sim   | __main | -     | 9001=ISO 9001, 1400=ISO 14001, 1702=ISO 17025                        |
| 5 | Clausulas           | `CLAUSULAS`    | TEXTO     | 100 | Nao   | __main | -     | Clausulas a cobrir. Ex: 8.7, 10.2                                    |
| 6 | Periodo Previsto    | `DTPREVISTA`   | DATA_HORA | -   | Sim   | __main | Datas | Data prevista para a auditoria                                       |
| 7 | Status              | `STATUS`       | LISTA     | 1   | Nao   | __main | -     | readOnly. P=Planejada, A=Em andamento, C=Concluida, X=Cancelada      |
| 8 | Auditor             | `CODAUDTOR`    | PESQUISA  | -   | Nao   | __main | -     | targetInstance=ThgAudAuditor. Auditor responsavel                     |
| 9 | Auditoria Gerada    | `CODAUD`  | INTEIRO   | -   | Nao   | __main | -     | readOnly. FK para THGQMGAUD quando iniciada                  |
| 10| Observacoes         | `OBSERVACOES`  | TEXTO     | 300 | Nao   | __main | -     | Notas sobre o planejamento                                           |
| 11| Criado por          | `CODUSU`       | PESQUISA  | -   | Nao   | __main | -     | readOnly. Auto: $ctx_usuario_logado. visivel=N                       |
| 12| Criado em           | `DHCREATE`    | DATA_HORA | -   | Nao   | __main | -     | readOnly. Auto: $ctx_dh_atual. visivel=N                             |

---

## Regras de Negocio

1. Uma entrada por processo planejado por ano — programa anual completo
2. STATUS='P' -> 'A' ao iniciar auditoria (CODAUD preenchido)
3. STATUS='C' ao concluir a auditoria vinculada
4. Dashboard alerta processos sem auditoria ha mais de 12 meses
5. Controle de independencia: auditor nao pode auditar processo que e de sua responsabilidade

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGAUDPROG, Listener BI, sequence

### Dicionario de Dados
- [ ] `datadictionary/THGQMGAUDPROG.xml`

### Menu
- [ ] `datadictionary/menu.xml` — ThgAudPrograma em pasta Auditoria
