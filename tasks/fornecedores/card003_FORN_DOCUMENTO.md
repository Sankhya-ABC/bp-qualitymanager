# Card FORN-03 — Documentos do Fornecedor

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Fornecedores                             |
| Fase Roadmap    | Fase 2                                   |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 8.4.3  |
| Ordem no modulo | 003 de 009                               |
| Depende de      | FORN-01                                  |

---

## Tabela

| Atributo  | Valor                  |
|:----------|:-----------------------|
| Nome      | `THGQMGFORNDOC`   |
| Instancia | `ThgFornDocumento`      |
| Sequencia | AUTO (`CODDOCFORN`)     |
| Dual-DB   | Oracle + SQL Server    |

---

## Campos

| # | Rotulo           | Coluna           | Tipo      | Tam | Obrig | Aba    | Grupo | Opcoes / Comportamento                                      |
|:--|:-----------------|:-----------------|:----------|:----|:------|:-------|:------|:------------------------------------------------------------|
| 1 | Id Doc Forn      | `CODDOCFORN`      | INTEIRO   | -   | PK    | __main | -     | readOnly, auto sequence                                     |
| 2 | Qualificacao     | `CODQUAL` | INTEIRO   | -   | Sim   | __main | -     | readOnly. FK para THGQMGQUAL                     |
| 3 | Descricao        | `DESCRICAO`      | TEXTO     | 200 | Sim   | __main | -     | isPresentation=S. Nome/tipo do certificado                  |
| 4 | Numero           | `NUMERODOC`      | TEXTO     | 100 | Nao   | __main | -     | Numero ou codigo do documento                               |
| 5 | Arquivo          | `ARQUIVO`        | ARQUIVO   | -   | Nao   | __main | -     | dataType=ARQUIVO. Upload do documento digitalizado          |
| 6 | Validade         | `DTVALIDADE`     | DATA_HORA | -   | Nao   | __main | -     | Data de vencimento. Job monitora este campo                 |
| 7 | Status           | `STATUS`         | LISTA     | 1   | Nao   | __main | -     | readOnly. V=Valido, E=Vencido, P=Pendente renovacao         |
| 8 | Criado por       | `CODUSU`         | PESQUISA  | -   | Nao   | __main | -     | readOnly. Auto: $ctx_usuario_logado. visivel=N              |
| 9 | Criado em        | `DHCREATE`      | DATA_HORA | -   | Nao   | __main | -     | readOnly. Auto: $ctx_dh_atual. visivel=N                    |

---

## Regras de Negocio

1. STATUS='E' quando DTVALIDADE < data atual — atualizado pelo ValidacaoFornecedoresJob diario
2. Job envia notificacao via TSIAVI quando vencimento < DIASAVISO_DOC de THGQMGCFG
3. Tabela e aba filha de ThgFornQualificacao (card FORN-01)

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGFORNDOC, Listener BI, sequence

### Dicionario de Dados
- [ ] `datadictionary/THGQMGFORNDOC.xml` — campo ARQUIVO com dataType=ARQUIVO

### Backend Java
- [ ] `model/.../jobs/ValidacaoFornecedoresJob.java` — monitora DTVALIDADE
