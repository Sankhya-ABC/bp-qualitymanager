# Card FORN-01 — Qualificacao de Fornecedor

## Identificacao
| Atributo        | Valor                                          |
|:----------------|:-----------------------------------------------|
| Modulo          | Fornecedores                                   |
| Fase Roadmap    | Fase 2                                         |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 8.4.1        |
| Ordem no modulo | 001 de 009                                     |
| Depende de      | CORE-01 (THGQMGCFG)                  |

---

## Tabela

| Atributo  | Valor                     |
|:----------|:--------------------------|
| Nome      | `THGQMGQUAL`   |
| Instancia | `QmFornQualificacao`      |
| Sequencia | AUTO (`CODQUAL`)   |
| Dual-DB   | Oracle + SQL Server       |

### Instancias da tela
| Instancia            | Tipo       | Descricao                                      |
|:---------------------|:-----------|:-----------------------------------------------|
| `QmFornConsulta`     | Lista/Grid | Visao de todos os fornecedores qualificados    |
| `QmFornQualificacao` | Formulario | Formulario completo de qualificacao            |

---

## Campos

| # | Rotulo              | Coluna           | Tipo      | Tam | Obrig | Aba    | Grupo        | Opcoes / Comportamento                                            |
|:--|:--------------------|:-----------------|:----------|:----|:------|:-------|:-------------|:------------------------------------------------------------------|
| 1 | Id Qualificacao     | `CODQUAL` | INTEIRO   | -   | PK    | __main | -            | readOnly, auto sequence                                           |
| 2 | Parceiro            | `CODPARC`        | PESQUISA  | -   | Sim   | __main | -            | targetInstance=Parceiro, targetField=CODPARC. isPresentation=S   |
| 3 | Tipo Fornecimento   | `TIPOFORN`       | LISTA     | 2   | Sim   | __main | Identificacao| MT=Material, SV=Servico, EG=Engenharia, IN=Insumo de Laboratorio |
| 4 | Status              | `STATUS`         | LISTA     | 1   | Nao   | __main | -            | readOnly. A=Aprovado, B=Condicional, T=Reprovado, E=Em avaliacao, X=Bloqueado |
| 5 | Pontuacao IQF       | `PONTUACAO`      | DECIMAL   | -   | Nao   | __main | Resultado    | readOnly. Calculado automaticamente pelo QualificacaoScoreListener |
| 6 | Resultado IQF       | `RESULTADOIQF`   | LISTA     | 1   | Nao   | __main | Resultado    | readOnly. A=Aprovado (>=80%), B=Condicional (50-79%), T=Reprovado (<50%) |
| 7 | Data Qualificacao   | `DTQUALIFICACAO` | DATA_HORA | -   | Nao   | __main | Datas        | readOnly. Auto: $ctx_dh_atual                                     |
| 8 | Validade            | `DTVALIDADE`     | DATA_HORA | -   | Nao   | __main | Datas        | Vencimento da qualificacao. Job de validacao monitora este campo  |
| 9 | Proxima Reavaliacao | `DTREAV`         | DATA_HORA | -   | Nao   | __main | Datas        | Calculada: DTVALIDADE menos dias de aviso de THGQMGCFG  |
| 10| Bloqueado           | `BLOQUEADO`      | CHECKBOX  | 1   | Nao   | __main | -            | readOnly. S=Bloqueado por reincidencia. Impede compra em TGFCAB   |
| 11| Motivo Bloqueio     | `MOTIVOBLOQUEIO` | TEXTO     | 300 | Nao   | __main | -            | Preenchido automaticamente ao bloquear                            |
| 12| Aberto por          | `CODUSU`         | PESQUISA  | -   | Nao   | __main | -            | readOnly. Auto: $ctx_usuario_logado. visivel=N                    |
| 13| Criado em           | `DHCREATE`      | DATA_HORA | -   | Nao   | __main | -            | readOnly. Auto: $ctx_dh_atual. visivel=N                          |

---

## Tabelas Filhas (grids na tela)

| Tabela filha          | Instancia filha       | Vinculo FK       | Descricao                        |
|:----------------------|:----------------------|:-----------------|:---------------------------------|
| `THGQMGFORNDOC`  | `QmFornDocumento`     | `CODQUAL` | Certificados e docs do fornecedor|
| `THGQMGFAVAL`  | `QmFornAvaliacao`     | `CODQUAL` | Avaliacoes periodicas            |
| `THGQMGREG`     | `QmFornNc`            | `CODPARC`        | NCs abertas vinculadas           |
| `THGQMGFRESP`   | `QmFornResposta`      | `CODQUAL` | Respostas ao questionario        |

---

## Action Buttons

| Botao                  | Classe Java                           | Instancia            | Transacao | Descricao                                           |
|:-----------------------|:--------------------------------------|:---------------------|:----------|:----------------------------------------------------|
| Enviar Questionario    | `EnviarQuestionarioFornActionButton`  | `QmFornQualificacao` | AUTOMATIC | Gera URL tokenizada Base64 e envia ao QM_EMAILQUESTIONARIO |
| Bloquear Fornecedor    | `BloquearFornecedorActionButton`      | `QmFornQualificacao` | AUTOMATIC | BLOQUEADO='S', STATUS='X', registra motivo, audit log |
| Desbloquear Fornecedor | `DesbloquearFornecedorActionButton`   | `QmFornQualificacao` | AUTOMATIC | BLOQUEADO='N', STATUS recalculado, audit log         |

---

## Regras de Negocio

1. STATUS calculado automaticamente pelo QualificacaoScoreListener com base em PONTUACAO
2. A=80-100%, B=50-79%, T<50% — limites configurados via THGQMGCRIT (card FORN-02)
3. BLOQUEADO='S' impede o fornecedor de ser selecionado em pedidos de compra (integracao TGFCAB)
4. Bloqueio automatico por reincidencia: 2 NCs de prioridade Critica no mesmo fornecedor em 90 dias
5. DTVALIDADE vencida dispara alerta no job ValidacaoFornecedoresJob
6. PONTUACAO nunca e inserido manualmente — sempre calculado pelo listener
7. Toda mudanca de STATUS registra em THGQMGLOG

---

## Gatilhos e Automatismos

| Evento                  | O que acontece                                           | Onde implementar                |
|:------------------------|:---------------------------------------------------------|:--------------------------------|
| INSERT                  | CODQUAL via sequence, DHCREATE=now()             | Listener BI   |
| INSERT em THGQMGFRESP | Listener recalcula PONTUACAO e RESULTADOIQF          | QualificacaoScoreListener       |
| DTVALIDADE < hoje        | STATUS='V' (Vencido), cria aviso em TSIAVI              | ValidacaoFornecedoresJob (diario)|
| 2 NCs Criticas em 90d    | BLOQUEADO='S', MOTIVOBLOQUEIO preenchido, audit log     | ScoreFornBusinessService        |

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGQUAL, Listener BI, sequence, indice IDX_THGQMGQUAL_CODPARC

### Dicionario de Dados
- [ ] `datadictionary/THGQMGQUAL.xml` — 2 instancias, grupos Identificacao, Resultado, Datas

### Backend Java
- [ ] `model/.../actionButtons/EnviarQuestionarioFornActionButton.java`
- [ ] `model/.../actionButtons/BloquearFornecedorActionButton.java`
- [ ] `model/.../actionButtons/DesbloquearFornecedorActionButton.java`
- [ ] `model/.../listeners/QualificacaoScoreListener.java` — instanceName: `QmFornResposta`
- [ ] `model/.../services/ScoreFornBusinessService.java`

### Menu
- [ ] `datadictionary/menu.xml` — instancias QmFornConsulta e QmFornQualificacao em pasta Fornecedores

---

## Observacoes Tecnicas

- CODPARC referencia TGFPAR via PESQUISA — sem FK no DDL
- BLOQUEADO e PONTUACAO sao readOnly — nunca editados pelo usuario, apenas pelo sistema
- instanceName do listener: `QmFornResposta` — precisa ser copiado EXATAMENTE do XML da tabela THGQMGFRESP
- BigDecimal.divide() no calculo de score SEMPRE com escala 2 e RoundingMode.HALF_UP
