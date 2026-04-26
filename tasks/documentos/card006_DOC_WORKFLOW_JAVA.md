# Card DOC-06 — Workflow de Aprovacao (Java)

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Documentos                               |
| Fase Roadmap    | Fase 2                                   |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 7.5.2  |
| Ordem no modulo | 006 de 007                               |
| Depende de      | DOC-01, DOC-02, DOC-03                   |

---

## Classes Java

### AtualizaDocumentosService.java

| Metodo              | Parametros  | O que faz                                                              |
|:--------------------|:------------|:-----------------------------------------------------------------------|
| `marcarRevisado`    | idDocumento | STATUS P->R. Valida existencia de arquivo. Cria registro em THGQMGHIST. Audit log |
| `aprovarDocumento`  | idDocumento | STATUS R->A. Valida STATUS='R'. Atualiza VERSAOATUAL. Cria THGQMGHIST. Audit log |
| `marcarObsoleto`    | idDocumento | STATUS A->O. Impede novas impressoes. Cria THGQMGHIST. Audit log |
| `atualizarVencidos` | —           | Chamado pelo job. Atualiza STATUS='V' para docs com DTVALIDADE < hoje  |

### ValidacaoDocumentosJob.java

```
Frequencia: diario (configurado no Sankhya)
Logica:
  1. Busca documentos com DTVALIDADE < hoje e STATUS != 'V' e STATUS != 'O'
  2. Atualiza STATUS='V'
  3. Cria aviso em TSIAVI para o CODPARC responsavel
  4. Log: [QM-DOC] {qtde} documentos atualizados para vencido
```

---

## Regras Criticas

1. Fluxo de STATUS e obrigatorio: P->R->A — outros fluxos bloqueados com RncValidacaoException
2. Toda mudanca de STATUS registra em THGQMGHIST e THGQMGLOG
3. instanceName dos @ActionButton deve ser `QmDocControle` — copiar do XML

---

## Artefatos

### Backend Java
- [ ] `model/.../services/AtualizaDocumentosService.java`
- [ ] `model/.../jobs/ValidacaoDocumentosJob.java`
