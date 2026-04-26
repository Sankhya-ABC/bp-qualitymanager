# Modulo Documentos

ISO 9001 clausula 7.5 (Informacao documentada).

## Funcionalidades

- Controle de documentos com versionamento
- Workflow de status: Rascunho → Em revisao → Aprovado → Obsoleto
- Upload de arquivos por versao
- Historico de mudancas de status
- Controle de copias impressas
- Permissoes por parceiro (leitura, revisao, aprovacao, impressao)
- Alerta de documentos a vencer (ValidacaoDocumentosJob)

## Tabelas

| Tabela | Instance | Descricao |
|:-------|:---------|:----------|
| THGQMGDOC | QmDocControle | Documento principal |
| THGQMGARQ | QmDocArquivo | Arquivos por versao |
| THGQMGHIST | QmDocHistorico | Historico de status (read-only) |
| THGQMGIMP | QmDocImpresso | Copias impressas |
| THGQMGUSIMPR | QmDocUsuImpresso | Usuarios com copia impressa |
| THGQMGPERM | QmDocPermissao | Permissoes por parceiro |

## Status do Documento

| Status | Descricao |
|:-------|:----------|
| R | Rascunho (inicial) |
| V | Em revisao |
| A | Aprovado |
| O | Obsoleto |

## Tipos de Documento

PO=Procedimento Operacional, IT=Instrucao de Trabalho, MQ=Manual da Qualidade, FO=Formulario, PL=Politica, OT=Outro.

## Artefatos
- dbscripts: V029-V034 (6 scripts)
- datadictionary: 6 XMLs
- Java: 6 entities, 6 repositories
