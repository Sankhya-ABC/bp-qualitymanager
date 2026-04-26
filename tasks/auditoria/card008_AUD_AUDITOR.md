# Card AUD-08 — Cadastro de Auditores

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Auditoria                                |
| Fase Roadmap    | Fase 3                                   |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 9.2.2b |
| Ordem no modulo | 008 de 009                               |
| Depende de      | CORE-01                                  |

## Tabela
| Atributo  | Valor               |
|:----------|:--------------------|
| Nome      | `THGQMGAUDTOR`   |
| Instancia | `QmAudAuditor`      |
| Sequencia | AUTO (`CODAUDTOR`)  |
| Dual-DB   | Oracle + SQL Server |

## Campos
| # | Rotulo               | Coluna            | Tipo      | Tam | Obrig | Opcoes                                                    |
|:--|:---------------------|:------------------|:----------|:----|:------|:----------------------------------------------------------|
| 1 | Id Auditor           | `CODAUDTOR`       | INTEIRO   | -   | PK    | readOnly, auto sequence                                   |
| 2 | Parceiro             | `CODPARC`         | PESQUISA  | -   | Sim   | targetInstance=Parceiro. isPresentation=S                 |
| 3 | Tipo                 | `TIPO`            | LISTA     | 1   | Sim   | I=Interno, E=Externo (certificadora)                      |
| 4 | Normas habilitado    | `NORMASAUDIT`     | TEXTO     | 100 | Nao   | Normas em que o auditor esta habilitado. Ex: 9001, 14001  |
| 5 | Formacao             | `FORMACAO`        | TEXTO     | 300 | Nao   | Formacao tecnica e treinamentos ISO                       |
| 6 | Nr Certificado       | `NRCERTIFICADO`   | TEXTO     | 100 | Nao   | Numero do certificado de auditor (IRCA, etc.)             |
| 7 | Validade Certificado | `DTVALCERTIF`     | DATA_HORA | -   | Nao   | Validade do certificado de auditor                        |
| 8 | Ativo                | `ATIVO`           | CHECKBOX  | 1   | Nao   | Default S. Inativo nao aparece para selecao               |
| 9 | Criado por           | `CODUSU`          | PESQUISA  | -   | Nao   | readOnly. Auto: $ctx_usuario_logado. visivel=N            |

## Regras de Negocio
1. Controle de independencia: ao vincular auditor a auditoria, alerta se auditor e responsavel pelo processo auditado
2. Auditores internos devem ter treinamento em auditoria ISO documentado
3. Auditores externos sao independentes por natureza — sem restricao de processo

## Artefatos
- [ ] `datadictionary/THGQMGAUDTOR.xml`
- [ ] `dbscripts/V1.xml` — DDL THGQMGAUDTOR
- [ ] `datadictionary/menu.xml` — QmAudAuditor em pasta Auditoria
