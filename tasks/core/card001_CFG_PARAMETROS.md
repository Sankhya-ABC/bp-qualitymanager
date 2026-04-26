# Card CORE-01 — Configuracao do Add-on

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Core                                     |
| Fase Roadmap    | Fase 1 — desenvolvido junto com RNC      |
| ISO             | Transversal — suporta todos os modulos   |
| Ordem no modulo | 001 de 004                               |
| Depende de      | —                                        |

---

## Tabela

| Atributo  | Valor                  |
|:----------|:-----------------------|
| Nome      | `THGQMGCFG`   |
| Instancia | `ThgConfig`             |
| Sequencia | AUTO (`CODCFG`)      |
| Dual-DB   | Oracle + SQL Server    |

### Instancias da tela
| Instancia   | Tipo       | Descricao                                 |
|:------------|:-----------|:------------------------------------------|
| `ThgConfig`  | Formulario | Configuracao e ativacao de modulos por empresa |

---

## Campos

| # | Rotulo                       | Coluna           | Tipo     | Tam | Obrig | Aba    | Grupo      | Opcoes / Comportamento                                |
|:--|:-----------------------------|:-----------------|:---------|:----|:------|:-------|:-----------|:------------------------------------------------------|
| 1 | Id Config                    | `CODCFG`       | INTEIRO  | -   | PK    | __main | -          | readOnly, auto sequence                               |
| 2 | Empresa                      | `CODEMP`         | PESQUISA | -   | Sim   | __main | -          | targetInstance=Empresa. UNIQUE — uma config por empresa |
| 3 | Modulo RNC ativo              | `MODNCATIVO`   | CHECKBOX | 1   | Nao   | __main | Modulos    | Default S. Liga/desliga modulo RNC inteiro            |
| 4 | Modulo Fornecedores ativo    | `MODFORNATIVO` | CHECKBOX | 1   | Nao   | __main | Modulos    | Default N                                             |
| 5 | Modulo Documentos ativo      | `MODDOCATIVO`  | CHECKBOX | 1   | Nao   | __main | Modulos    | Default N                                             |
| 6 | Modulo Gestao Mudancas ativo | `MODGMATIVO`   | CHECKBOX | 1   | Nao   | __main | Modulos    | Default N                                             |
| 7 | Modulo Auditoria ativo       | `MODAUDATIVO`  | CHECKBOX | 1   | Nao   | __main | Modulos    | Default N                                             |
| 8 | Modulo Indicadores ativo     | `MODINDATIVO`  | CHECKBOX | 1   | Nao   | __main | Modulos    | Default N                                             |
| 9 | E-mail de notificacao        | `EMAILNOTIF`     | TEXTO    | 100 | Nao   | __main | Parametros | E-mail para alertas gerais do sistema                 |
| 10| Dias aviso doc a vencer      | `DIASAVISO_DOC`  | INTEIRO  | -   | Nao   | __main | Parametros | Default 30. Usado pelo ValidacaoDocumentosJob         |
| 11| Prazo resposta fornecedor    | `DIASRESP_FORN`  | INTEIRO  | -   | Nao   | __main | Parametros | Default 10 dias uteis                                 |
| 12| Criado por                   | `CODUSU`         | PESQUISA | -   | Nao   | __main | -          | readOnly. Auto: $ctx_usuario_logado. visivel=N        |
| 13| Criado em                    | `DHCREATE`      | DATA_HORA| -   | Nao   | __main | -          | readOnly. Auto: $ctx_dh_atual. visivel=N              |

---

## Regras de Negocio

1. Uma unica configuracao por empresa — constraint UNIQUE em CODEMP
2. MODNCATIVO default 'S' — RNC e o modulo base, sempre ativo
3. Modulos inativos ocultam seus itens de menu automaticamente via condicao no menu.xml
4. DIASAVISO_DOC lido pelo ValidacaoDocumentosJob para calcular alertas de vencimento
5. DIASRESP_FORN lido pelo ValidacaoFornecedoresJob para alertas de prazo

---

## Gatilhos e Automatismos

| Evento  | O que acontece                              | Onde implementar        |
|:--------|:--------------------------------------------|:------------------------|
| INSERT  | CODCFG via sequence, DHCREATE=now()      | Listener BI |

---

## Artefatos

### Banco de Dados
- [x] `dbscripts/V1.xml` — DDL THGQMGCFG, Listener BI, sequence

### Dicionario de Dados
- [x] `datadictionary/THGQMGCFG.xml` — instancia ThgConfig, grupos Modulos e Parametros

### Menu
- [x] `datadictionary/menu.xml` — instancia ThgConfig registrada como item Configuracao

---

## Observacoes Tecnicas

- Tabela criada pelo Auto DDL a partir do datadictionary — nao precisa de DDL manual
- CODEMP referencia TSIEMP via PESQUISA — sem CREATE TABLE FK
- Campos de auditoria (CODUSU, DHCREATE) preenchidos automaticamente via expression
- instanceName: `ThgConfig` — copiar exatamente do XML
