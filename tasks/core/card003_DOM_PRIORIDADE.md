# Card CORE-03 — Dominio Prioridades

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Core                                     |
| Fase Roadmap    | Fase 1                                   |
| ISO             | Transversal — usado em NC e Auditoria    |
| Ordem no modulo | 003 de 004                               |
| Depende de      | —                                        |

---

## Tabela

| Atributo  | Valor                   |
|:----------|:------------------------|
| Nome      | `THGQMGPRIO`    |
| Instancia | `ThgDomPrioridade`       |
| Sequencia | MANUAL (`CODPRIO`) |
| Dual-DB   | Oracle + SQL Server     |

### Instancias da tela
| Instancia          | Tipo       | Descricao                                  |
|:-------------------|:-----------|:-------------------------------------------|
| `ThgDomPrioridade`  | Formulario | Cadastro de prioridades com prazo em dias  |

---

## Campos

| # | Rotulo       | Coluna          | Tipo     | Tam | Obrig | Aba    | Grupo | Opcoes / Comportamento                                   |
|:--|:-------------|:----------------|:---------|:----|:------|:-------|:------|:---------------------------------------------------------|
| 1 | Id           | `CODPRIO`  | INTEIRO  | -   | PK    | __main | -     | readOnly. Informado manualmente nos dados iniciais        |
| 2 | Descricao    | `DESCRICAO`     | TEXTO    | 50  | Sim   | __main | -     | isPresentation=S. Ex: Simples, Prioritario, Critico      |
| 3 | Prazo (dias) | `PRAZODIAS`    | INTEIRO  | -   | Nao   | __main | -     | Prazo padrao em dias. Usado para calcular DTPREVENCERRAR |
| 4 | Ativo        | `ATIVO`         | CHECKBOX | 1   | Nao   | __main | -     | Default S                                                |

---

## Dados Iniciais (dbscripts/V1.xml — executar=SEMPRE com verificacao)

| CODPRIO | DESCRICAO    | PRAZODIAS |
|:-------------|:-------------|:-----------|
| 1            | Simples      | 30         |
| 2            | Prioritario  | 15         |
| 3            | Critico      | 5          |

---

## Regras de Negocio

1. Dados iniciais inseridos via V1.xml com verificacao: `IF COUNT(*) = 0 THEN INSERT`
2. PRAZODIAS usado pelo Listener BI de THGQMGREG para calcular DTPREVENCERRAR
3. Prioridade 3 (Critico) = NC de auditoria NC Maior — tratamento urgente
4. Empresa pode ajustar PRAZODIAS conforme sua realidade operacional
5. Tabela de dominio — nao deve ter muitos registros adicionados pelo usuario

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGPRIO + INSERT dados iniciais (executar=SEMPRE)

### Dicionario de Dados
- [ ] `datadictionary/THGQMGPRIO.xml` — instancia ThgDomPrioridade

### Menu
- [ ] `datadictionary/menu.xml` — adicionar ThgDomPrioridade em pasta Configuracao

---

## Observacoes Tecnicas

- sequenceType=M (manual) — PK definida nos dados iniciais, nao gerada automaticamente
- Referenciada em THGQMGREG via campo PRIORIDADE (LISTA com opcoes 1/2/3)
- instanceName: `ThgDomPrioridade` — copiar exatamente do XML
