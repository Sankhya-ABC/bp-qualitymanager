# Card DOC-02 — Versoes e Arquivos

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Documentos                               |
| Fase Roadmap    | Fase 2                                   |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 7.5.3b |
| Ordem no modulo | 002 de 007                               |
| Depende de      | DOC-01                                   |

---

## Tabela

| Atributo  | Valor                |
|:----------|:---------------------|
| Nome      | `THGQMGARQ`    |
| Instancia | `QmDocArquivo`       |
| Sequencia | AUTO (`CODARQ`)   |
| Dual-DB   | Oracle + SQL Server  |

---

## Campos

| # | Rotulo      | Coluna        | Tipo      | Tam | Obrig | Aba    | Grupo | Opcoes / Comportamento                                       |
|:--|:------------|:--------------|:----------|:----|:------|:-------|:------|:-------------------------------------------------------------|
| 1 | Id Arquivo  | `CODARQ`   | INTEIRO   | -   | PK    | __main | -     | readOnly, auto sequence                                      |
| 2 | Documento   | `CODDOC` | INTEIRO   | -   | Sim   | __main | -     | readOnly. FK para THGQMGDOC                           |
| 3 | Versao      | `VERSAO`      | TEXTO     | 10  | Sim   | __main | -     | isPresentation=S. Ex: 1.0, 2.3                               |
| 4 | Arquivo     | `ARQUIVO`     | ARQUIVO   | -   | Nao   | __main | -     | dataType=ARQUIVO. Upload do arquivo fisico (PDF, DOCX, etc.) |
| 5 | Validade    | `DTVALIDADE`  | DATA_HORA | -   | Nao   | __main | -     | Validade especifica deste arquivo                            |
| 6 | Comentario  | `COMENTARIO`  | HTML      | -   | Nao   | __main | -     | Descricao das alteracoes nesta versao (changelog)            |
| 7 | Criado por  | `CODUSU`      | PESQUISA  | -   | Nao   | __main | -     | readOnly. Auto: $ctx_usuario_logado. visivel=N               |
| 8 | Criado em   | `DHCREATE`   | DATA_HORA | -   | Nao   | __main | -     | readOnly. Auto: $ctx_dh_atual. visivel=N                     |

---

## Regras de Negocio

1. Pode existir multiplas versoes por documento — historico de versoes fica nesta tabela
2. Versao mais recente define o VERSAOATUAL em THGQMGDOC ao ser aprovada
3. Campo ARQUIVO usa dataType=ARQUIVO — armazenado no servidor de arquivos Sankhya
4. Tela e aba filha de QmDocControle (card DOC-01)

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGARQ, Listener BI, sequence

### Dicionario de Dados
- [ ] `datadictionary/THGQMGARQ.xml` — campo ARQUIVO com dataType=ARQUIVO
