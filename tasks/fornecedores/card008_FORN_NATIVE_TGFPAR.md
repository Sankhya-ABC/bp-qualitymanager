# Card FORN-08 — Campo Nativo em TGFPAR

## Identificacao
| Atributo        | Valor                                       |
|:----------------|:--------------------------------------------|
| Modulo          | Fornecedores                                |
| Fase Roadmap    | Fase 2                                      |
| ISO             | Transversal — usado em RNC e Fornecedores   |
| Ordem no modulo | 008 de 009                                  |
| Depende de      | —                                           |

---

## Extensao de Tabela Nativa

| Atributo           | Valor                                 |
|:-------------------|:--------------------------------------|
| Tabela nativa      | `TGFPAR` (Parceiros Sankhya)          |
| Campo adicionado   | `QM_EMAILQUESTIONARIO`                |
| Tipo               | TEXTO (100)                           |
| Aba no Sankhya Om  | Endereco                              |
| Arquivo XML        | `datadictionary/TGFPAR.xml` |

---

## Campos

| # | Rotulo                       | Coluna                  | Tipo  | Tam | Obrig | Aba      | Grupo | Opcoes / Comportamento                              |
|:--|:-----------------------------|:------------------------|:------|:----|:------|:---------|:------|:----------------------------------------------------|
| 1 | E-mail para questionarios QM | `QM_EMAILQUESTIONARIO`  | TEXTO | 100 | Nao   | Endereco | -     | E-mail especifico para envio de questionarios de qualidade. Diferente do EMAIL padrao |

---

## Regras de Negocio

1. Prefixo `QM_` identifica todos os campos adicionados pelo QualityManager em tabelas nativas
2. Campo visivel na aba Endereco do cadastro de Parceiros — nao cria tela separada
3. Usado por: QuestionarioFornService (card FORN-07) e NotificacaoNcService (card RNC-13)
4. Se vazio, o sistema usa o EMAIL padrao do parceiro como fallback

---

## Artefatos

### Dicionario de Dados
- [ ] `datadictionary/TGFPAR.xml` — extensao com campo QM_EMAILQUESTIONARIO na aba Endereco

---

## Observacoes Tecnicas

- Extensao de tabela nativa nao exige DDL — o Auto DDL do Sankhya adiciona o campo automaticamente
- Campo opcional — parceiro pode nao ter e-mail de qualidade cadastrado
- Convencao: campos em tabelas nativas Sankhya sempre com prefixo `QM_` no QualityManager
