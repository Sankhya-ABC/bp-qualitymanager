# Card DOC-04 — Controle de Impressos

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Documentos                               |
| Fase Roadmap    | Fase 2                                   |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 7.5.3a |
| Ordem no modulo | 004 de 007                               |
| Depende de      | DOC-01                                   |

---

## Tabelas

| Tabela                 | Instancia           | Sequencia      | Descricao                           |
|:-----------------------|:--------------------|:---------------|:------------------------------------|
| `THGQMGIMP`     | `ThgDocImpresso`     | `CODIMP`   | Emissao de copia fisica controlada  |
| `THGQMGUSIMPR` | `ThgDocUsuImpresso`  | `CODIMPUSU`    | Usuarios que receberam copias       |

---

## Campos — THGQMGIMP

| # | Rotulo       | Coluna        | Tipo      | Tam | Obrig | Opcoes / Comportamento                                 |
|:--|:-------------|:--------------|:----------|:----|:------|:-------------------------------------------------------|
| 1 | Id Impresso  | `CODIMP`  | INTEIRO   | -   | PK    | readOnly, auto sequence                                |
| 2 | Documento    | `CODDOC` | INTEIRO   | -   | Sim   | readOnly. FK para THGQMGDOC                     |
| 3 | Versao       | `VERSAO`      | TEXTO     | 10  | Sim   | Versao que foi impressa. Ex: 2.1                       |
| 4 | Qtde Copias  | `QTDECOPIAS`  | INTEIRO   | -   | Sim   | Numero de copias fisicas emitidas                      |
| 5 | Finalidade   | `FINALIDADE`  | TEXTO     | 200 | Nao   | Motivo ou destino das copias                           |
| 6 | Impresso por | `CODUSU`      | PESQUISA  | -   | Nao   | readOnly. Auto: $ctx_usuario_logado                    |
| 7 | Data         | `DHCREATE`   | DATA_HORA | -   | Nao   | readOnly. Auto: $ctx_dh_atual                          |

## Campos — THGQMGUSIMPR

| # | Rotulo      | Coluna        | Tipo     | Tam | Obrig | Opcoes / Comportamento                                |
|:--|:------------|:--------------|:---------|:----|:------|:------------------------------------------------------|
| 1 | Id          | `CODIMPUSU`   | INTEIRO  | -   | PK    | readOnly, auto sequence                               |
| 2 | Impresso    | `CODIMP`  | INTEIRO  | -   | Sim   | readOnly. FK para THGQMGIMP                    |
| 3 | Parceiro    | `CODPARC`     | PESQUISA | -   | Sim   | targetInstance=Parceiro. Quem recebeu a copia         |

---

## Regras de Negocio

1. Documento com STATUS='O' (Obsoleto) nao pode gerar novo impresso — validacao no action button
2. Rastreabilidade ISO 7.5.3a — saber quantas copias existem fisicamente e com quem
3. Tela e aba filha de ThgDocControle (card DOC-01)

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGIMP + THGQMGUSIMPR

### Dicionario de Dados
- [ ] `datadictionary/THGQMGIMP.xml`
- [ ] `datadictionary/THGQMGUSIMPR.xml`
