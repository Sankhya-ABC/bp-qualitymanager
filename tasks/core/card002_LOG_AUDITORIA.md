# Card CORE-02 — Audit Log (INSERT ONLY)

## Identificacao
| Atributo        | Valor                                         |
|:----------------|:----------------------------------------------|
| Modulo          | Core                                          |
| Fase Roadmap    | Fase 1                                        |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 7.5.3       |
| Ordem no modulo | 002 de 004                                    |
| Depende de      | —                                             |

---

## Tabela

| Atributo  | Valor                  |
|:----------|:-----------------------|
| Nome      | `THGQMGLOG`    |
| Instancia | `QmLogAuditoria`       |
| Sequencia | AUTO (`CODLOG`)         |
| Dual-DB   | Oracle + SQL Server    |

### Instancias da tela
| Instancia        | Tipo       | Descricao                                        |
|:-----------------|:-----------|:-------------------------------------------------|
| `QmLogAuditoria` | Lista/Grid | Consulta de eventos — somente leitura, sem edicao |

---

## Campos

| # | Rotulo          | Coluna           | Tipo      | Tam  | Obrig | Aba     | Grupo  | Opcoes / Comportamento                                              |
|:--|:----------------|:-----------------|:----------|:-----|:------|:--------|:-------|:--------------------------------------------------------------------|
| 1 | Id Log          | `CODLOG`          | INTEIRO   | -    | PK    | __main  | -      | readOnly, auto sequence                                             |
| 2 | Data/Hora       | `DTREGISTRO`     | DATA_HORA | -    | Sim   | __main  | -      | readOnly. Auto: $ctx_dh_atual                                       |
| 3 | Modulo          | `MODULO`         | LISTA     | 10   | Sim   | __main  | -      | readOnly. NC=Registro de Nao Conformidades, FORN=Fornecedores, DOC=Documentos, GM=Gestao Mudancas, AUD=Auditoria, CORE=Core |
| 4 | Entidade        | `ENTIDADE`       | TEXTO     | 50   | Sim   | __main  | -      | readOnly. Nome da tabela afetada. Ex: THGQMGREG               |
| 5 | ID do registro  | `IDENTIDADE`     | INTEIRO   | -    | Sim   | __main  | -      | readOnly. PK do registro afetado                                    |
| 6 | Acao            | `ACAO`           | TEXTO     | 40   | Sim   | __main  | -      | readOnly. isPresentation=S. Ex: INSERT, UPDATE_STATUS, AVANCAR_FASE, CANCELAR, ENCERRAR |
| 7 | Campo alterado  | `CAMPO_ALTERADO` | TEXTO     | 60   | Nao   | __main  | -      | readOnly. Nome da coluna que mudou                                  |
| 8 | Valor anterior  | `VALOR_ANTERIOR` | TEXTO     | 2000 | Nao   | Detalhe | -      | readOnly. Valor antes da alteracao                                  |
| 9 | Valor novo      | `VALOR_NOVO`     | TEXTO     | 2000 | Nao   | Detalhe | -      | readOnly. Valor apos a alteracao                                    |
| 10| Descricao       | `DESCRICAO`      | TEXTO     | 500  | Nao   | __main  | -      | readOnly. Contexto legivel da operacao em portugues                 |
| 11| Usuario         | `CODUSU`         | PESQUISA  | -    | Sim   | __main  | -      | readOnly. Auto: $ctx_usuario_logado                                 |

---

## Tabelas Filhas

Nao possui — tabela de log nao tem filhos.

---

## Action Buttons

Nenhum — tabela INSERT ONLY. Nenhuma acao do usuario e permitida.

---

## Regras de Negocio

1. INSERT ONLY — nenhum UPDATE ou DELETE permitido, bloqueados pelo trigger TRG_THGQMGLOG_READONLY
2. Todos os campos sao readOnly — usuario nao edita nenhum dado de log
3. Registros inseridos EXCLUSIVAMENTE via AuditLogService.java (nunca SQL direto no codigo)
4. Todo modulo do produto usa AuditLogService ao mudar STATUS ou FASE de qualquer entidade
5. Tela de consulta visivel apenas para perfil Gestor de Qualidade ou Auditor

---

## Gatilhos e Automatismos

| Evento          | O que acontece                                         | Onde implementar               |
|:----------------|:-------------------------------------------------------|:-------------------------------|
| INSERT          | CODLOG via sequence, DTREGISTRO=now()                   | Listener BI  |
| UPDATE/DELETE   | RAISE_APPLICATION_ERROR — operacao bloqueada           | Trigger TRG_THGQMGLOG_READONLY  |

---

## Artefatos

### Banco de Dados
- [x] `dbscripts/V1.xml` — DDL THGQMGLOG, Listener BI, trigger TRG_THGQMGLOG_READONLY (bloqueia UPDATE/DELETE), sequence

### Dicionario de Dados
- [x] `datadictionary/THGQMGLOG.xml` — instancia QmLogAuditoria, todos campos readOnly, aba Detalhe para valores

### Backend Java
- [ ] `model/.../services/AuditLogService.java` — service central: metodo registrar(modulo, entidade, idEntidade, acao, campoAlt, valAnt, valNovo, descricao)

### Menu
- [ ] `datadictionary/menu.xml` — adicionar entrada QmLogAuditoria em pasta Configuracao (acesso restrito)

---

## Observacoes Tecnicas

- Trigger TRG_THGQMGLOG_READONLY bloqueia UPDATE e DELETE com mensagem: `THGQMGLOG e INSERT ONLY`
- AuditLogService deve ser injetado (@Inject) em todos os BusinessService do produto
- instanceName: `QmLogAuditoria` — copiar exatamente do XML
- Evidencia direta da ISO 9001 clausula 7.5.3 — rastreabilidade de informacao documentada
