# Card DOC-05 — Permissoes de Documento

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Documentos                               |
| Fase Roadmap    | Fase 2                                   |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 7.5.3  |
| Ordem no modulo | 005 de 007                               |
| Depende de      | DOC-01                                   |

---

## Tabela

| Atributo  | Valor                  |
|:----------|:-----------------------|
| Nome      | `THGQMGPERM`    |
| Instancia | `QmDocPermissao`       |
| Sequencia | AUTO (`CODPERM`)   |
| Dual-DB   | Oracle + SQL Server    |

---

## Campos

| # | Rotulo       | Coluna          | Tipo     | Tam | Obrig | Opcoes / Comportamento                                          |
|:--|:-------------|:----------------|:---------|:----|:------|:----------------------------------------------------------------|
| 1 | Id           | `CODPERM`   | INTEIRO  | -   | PK    | readOnly, auto sequence                                         |
| 2 | Documento    | `CODDOC`   | INTEIRO  | -   | Sim   | readOnly. FK para THGQMGDOC                              |
| 3 | Parceiro     | `CODPARC`       | PESQUISA | -   | Sim   | targetInstance=Parceiro. Usuario com permissao                  |
| 4 | Leitura      | `PERM_LEITURA`  | CHECKBOX | 1   | Nao   | S=Pode visualizar o documento                                   |
| 5 | Revisao      | `PERM_REVISAO`  | CHECKBOX | 1   | Nao   | S=Pode marcar como revisado                                     |
| 6 | Aprovacao    | `PERM_APROVA`   | CHECKBOX | 1   | Nao   | S=Pode aprovar o documento                                      |
| 7 | Impressao    | `PERM_IMPRIME`  | CHECKBOX | 1   | Nao   | S=Pode gerar copias fisicas                                     |

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGPERM, Listener BI, sequence

### Dicionario de Dados
- [ ] `datadictionary/THGQMGPERM.xml`
