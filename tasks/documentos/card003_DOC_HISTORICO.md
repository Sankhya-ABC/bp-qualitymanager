# Card DOC-03 — Historico de Status

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Documentos                               |
| Fase Roadmap    | Fase 2                                   |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 7.5.3  |
| Ordem no modulo | 003 de 007                               |
| Depende de      | DOC-01                                   |

---

## Tabela

| Atributo  | Valor                  |
|:----------|:-----------------------|
| Nome      | `THGQMGHIST`    |
| Instancia | `QmDocHistorico`       |
| Sequencia | AUTO (`CODHIST`)   |
| Dual-DB   | Oracle + SQL Server    |

---

## Campos

| # | Rotulo          | Coluna           | Tipo      | Tam | Obrig | Opcoes / Comportamento                                 |
|:--|:----------------|:-----------------|:----------|:----|:------|:-------------------------------------------------------|
| 1 | Id Historico    | `CODHIST`    | INTEIRO   | -   | PK    | readOnly, auto sequence                                |
| 2 | Documento       | `CODDOC`    | INTEIRO   | -   | Sim   | readOnly. FK para THGQMGDOC                     |
| 3 | Status Anterior | `STATUSANT`      | LISTA     | 1   | Sim   | readOnly. P=Pendente, R=Revisado, A=Aprovado, V=Vencido, O=Obsoleto |
| 4 | Status Novo     | `STATUSNOVO`     | LISTA     | 1   | Sim   | readOnly. P/R/A/V/O                                    |
| 5 | Justificativa   | `JUSTIFICATIVA`  | TEXTO     | 300 | Nao   | Motivo da mudanca de status                            |
| 6 | Alterado por    | `CODUSU`         | PESQUISA  | -   | Sim   | readOnly. Auto: $ctx_usuario_logado                    |
| 7 | Data            | `DHCREATE`      | DATA_HORA | -   | Sim   | readOnly. Auto: $ctx_dh_atual                          |

---

## Regras de Negocio

1. Registro criado automaticamente pelo AtualizaDocumentosService a cada mudanca de STATUS
2. Tela e READ ONLY — nenhum campo editavel, nenhum action button
3. Evidencia ISO 7.5.3 — trilha completa de quem aprovou e quando
4. Tela e aba filha de QmDocControle (card DOC-01)

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGHIST, Listener BI, sequence

### Dicionario de Dados
- [ ] `datadictionary/THGQMGHIST.xml` — todos campos readOnly, sem action buttons
