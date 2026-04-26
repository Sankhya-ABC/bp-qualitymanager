# Card AUD-05 — Constatacoes da Auditoria

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Auditoria                                |
| Fase Roadmap    | Fase 3                                   |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 9.2.2e |
| Ordem no modulo | 005 de 009                               |
| Depende de      | AUD-02, RNC Card-001 (THGQMGREG)   |

## Tabela
| Atributo  | Valor                    |
|:----------|:-------------------------|
| Nome      | `THGQMGAUDCON`    |
| Instancia | `QmAudConstatacao`       |
| Sequencia | AUTO (`CODCONST`)   |
| Dual-DB   | Oracle + SQL Server      |

## Campos
| # | Rotulo        | Coluna           | Tipo      | Tam | Obrig | Opcoes                                                                          |
|:--|:--------------|:-----------------|:----------|:----|:------|:--------------------------------------------------------------------------------|
| 1 | Id            | `CODCONST`  | INTEIRO   | -   | PK    | readOnly, auto sequence                                                         |
| 2 | Auditoria     | `CODAUD`    | INTEIRO   | -   | Sim   | readOnly. FK para THGQMGAUD                                             |
| 3 | Tipo          | `TIPO`           | LISTA     | 2   | Sim   | CF=Conforme, NM=NC Menor, MA=NC Maior, OB=Observacao, OM=Oportunidade Melhoria |
| 4 | Clausula ISO  | `CLAUSULAISO`    | TEXTO     | 20  | Sim   | Clausula de referencia. Ex: 8.7.1                                               |
| 5 | Descricao     | `DESCRICAO`      | HTML      | -   | Sim   | Descricao detalhada da constatacao                                              |
| 6 | Evidencia     | `EVIDENCIA`      | TEXTO     | 500 | Nao   | Evidencia que suporta a constatacao                                             |
| 7 | RNC Vinculada | `CODRNC`           | INTEIRO   | -   | Nao   | readOnly. FK para THGQMGREG. Preenchido ao abrir NC                       |
| 8 | Auditado por  | `CODUSU`         | PESQUISA  | -   | Nao   | readOnly. Auto: $ctx_usuario_logado. visivel=N                                  |
| 9 | Data          | `DHCREATE`      | DATA_HORA | -   | Nao   | readOnly. Auto: $ctx_dh_atual. visivel=N                                        |

## Action Buttons
| Botao     | Classe Java                       | Instancia          | Descricao                                                       |
|:----------|:----------------------------------|:-------------------|:----------------------------------------------------------------|
| Abrir NC  | `AbrirNcDeAuditoriaActionButton`  | `QmAudConstatacao` | TIPO=NM/MA: cria RNC com ORIGEM='A', CODAUD, prioridade auto |

## Regras de Negocio
1. TIPO=CF ou OB: sem NC automatica
2. TIPO=NM: action button abre RNC com PRIORIDADE=2 (Prioritario)
3. TIPO=MA: action button abre RNC com PRIORIDADE=3 (Critico)
4. RNC aberta tem ORIGEM='A' e CODAUD preenchido — rastreabilidade bidirecional
5. NC Maior de certificacao: tratamento em 30-90 dias — risco de suspensao do certificado

## Artefatos
- [ ] `datadictionary/THGQMGAUDCON.xml`
- [ ] `dbscripts/V1.xml` — DDL THGQMGAUDCON
- [ ] `model/.../actionButtons/AbrirNcDeAuditoriaActionButton.java`
