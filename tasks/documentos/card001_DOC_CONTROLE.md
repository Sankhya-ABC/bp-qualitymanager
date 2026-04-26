# Card DOC-01 — Controle de Documentos

## Identificacao
| Atributo        | Valor                                         |
|:----------------|:----------------------------------------------|
| Modulo          | Documentos                                    |
| Fase Roadmap    | Fase 2                                        |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 7.5.2       |
|                 | ABNT NBR ISO 14001:2015 — Clausula 7.5 (identica) |
|                 | ABNT NBR ISO/IEC 17025:2017 — Clausula 8.3    |
| Ordem no modulo | 001 de 007                                    |
| Depende de      | CORE-01                                       |

---

## Tabela

| Atributo  | Valor                  |
|:----------|:-----------------------|
| Nome      | `THGQMGDOC`     |
| Instancia | `QmDocControle`        |
| Sequencia | AUTO (`CODDOC`)   |
| Dual-DB   | Oracle + SQL Server    |

### Instancias da tela
| Instancia       | Tipo       | Descricao                                  |
|:----------------|:-----------|:-------------------------------------------|
| `QmDocConsulta` | Lista/Grid | Visao de todos os documentos com filtros   |
| `QmDocControle` | Formulario | Formulario completo de cadastro e gestao   |

---

## Campos

| # | Rotulo            | Coluna          | Tipo      | Tam | Obrig | Aba    | Grupo        | Opcoes / Comportamento                                           |
|:--|:------------------|:----------------|:----------|:----|:------|:-------|:-------------|:-----------------------------------------------------------------|
| 1 | Id Documento      | `CODDOC`   | INTEIRO   | -   | PK    | __main | -            | readOnly, auto sequence                                          |
| 2 | Codigo            | `CODIGODOC`     | TEXTO     | 50  | Sim   | __main | -            | isPresentation=S. Codigo unico. UNIQUE                           |
| 3 | Titulo            | `TITULODOC`     | TEXTO     | 300 | Sim   | __main | -            | Titulo completo do documento                                     |
| 4 | Tipo              | `TIPODOC`       | LISTA     | 2   | Sim   | __main | Identificacao| PR=Procedimento, IN=Instrucao de Trabalho, RE=Registro, PO=Politica, MA=Manual, FO=Formulario, NO=Norma |
| 5 | Status            | `STATUS`        | LISTA     | 1   | Nao   | __main | -            | readOnly. P=Pendente, R=Revisado, A=Aprovado, V=Vencido, O=Obsoleto |
| 6 | Versao Atual      | `VERSAOATUAL`   | TEXTO     | 10  | Nao   | __main | -            | readOnly. Preenchido ao aprovar ultimo arquivo. Ex: 1.0, 2.3     |
| 7 | Responsavel       | `CODPARC`       | PESQUISA  | -   | Nao   | __main | Responsaveis | targetInstance=Parceiro. Dono do documento                       |
| 8 | Aprovador         | `CODPARC_APROV` | PESQUISA  | -   | Nao   | __main | Responsaveis | targetInstance=Parceiro. Quem aprova                             |
| 9 | Processo          | `PROCESSO`      | TEXTO     | 100 | Nao   | __main | Identificacao| Processo ao qual o documento pertence                            |
| 10| Norma ISO         | `NORMAISO`      | LISTA     | 4   | Nao   | __main | Identificacao| 9001=ISO 9001, 1400=ISO 14001, 1702=ISO 17025, NA=Nao aplicavel  |
| 11| Clausula ISO      | `CLAUSULAISO`   | TEXTO     | 20  | Nao   | __main | Identificacao| Ex: 7.5.2, 8.4.3. Vincula ao painel de maturidade               |
| 12| Data Validade     | `DTVALIDADE`    | DATA_HORA | -   | Nao   | __main | Datas        | Vencimento. Job monitora e cria alerta em TSIAVI                 |
| 13| Criado por        | `CODUSU`        | PESQUISA  | -   | Nao   | __main | -            | readOnly. Auto: $ctx_usuario_logado. visivel=N                   |
| 14| Criado em         | `DHCREATE`     | DATA_HORA | -   | Nao   | __main | -            | readOnly. Auto: $ctx_dh_atual. visivel=N                         |

---

## Tabelas Filhas (grids na tela)

| Tabela filha         | Instancia filha     | Vinculo FK    | Descricao                              |
|:---------------------|:--------------------|:--------------|:---------------------------------------|
| `THGQMGARQ`    | `QmDocArquivo`      | `CODDOC` | Versoes fisicas — card DOC-02          |
| `THGQMGHIST`  | `QmDocHistorico`    | `CODDOC` | Trilha de aprovacao — card DOC-03      |
| `THGQMGIMP`   | `QmDocImpresso`     | `CODDOC` | Copias fisicas — card DOC-04           |

---

## Action Buttons

| Botao               | Classe Java                        | Instancia       | Transacao | Descricao                               |
|:--------------------|:-----------------------------------|:----------------|:----------|:----------------------------------------|
| Marcar como Revisado| `MarcarRevisadoDocActionButton`    | `QmDocControle` | AUTOMATIC | STATUS P->R. Valida se tem arquivo      |
| Aprovar Documento   | `AprovarDocumentoActionButton`     | `QmDocControle` | AUTOMATIC | STATUS R->A. Apenas documentos revisados|
| Marcar Obsoleto     | `ObsoletoDocumentoActionButton`    | `QmDocControle` | AUTOMATIC | STATUS A->O. Bloqueia impressao         |

---

## Regras de Negocio

1. Fluxo de STATUS: P (Pendente) -> R (Revisado) -> A (Aprovado) -> O (Obsoleto) ou V (Vencido)
2. Apenas STATUS='P' pode ir para 'R' — demais fluxos bloqueados com mensagem ao usuario
3. Apenas STATUS='R' pode ir para 'A' — action button Aprovar valida antes de executar
4. VERSAOATUAL atualizada automaticamente ao aprovar (pega versao do ultimo THGQMGARQ)
5. Toda mudanca de STATUS registra em THGQMGHIST e THGQMGLOG
6. NORMAISO e CLAUSULAISO alimentam o Dashboard de Maturidade ISO (card IND-05)

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGDOC, Listener BI, sequence, indices

### Dicionario de Dados
- [ ] `datadictionary/THGQMGDOC.xml` — 2 instancias, grupos Identificacao, Responsaveis, Datas

### Backend Java
- [ ] `model/.../actionButtons/MarcarRevisadoDocActionButton.java`
- [ ] `model/.../actionButtons/AprovarDocumentoActionButton.java`
- [ ] `model/.../actionButtons/ObsoletoDocumentoActionButton.java`
- [ ] `model/.../services/AtualizaDocumentosService.java`

### Menu
- [ ] `datadictionary/menu.xml` — instancias QmDocConsulta e QmDocControle em pasta Documentos
