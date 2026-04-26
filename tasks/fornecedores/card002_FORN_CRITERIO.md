# Card FORN-02 — Criterios de Qualificacao

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Fornecedores                             |
| Fase Roadmap    | Fase 2                                   |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 8.4.1c |
| Ordem no modulo | 002 de 009                               |
| Depende de      | FORN-01                                  |

---

## Tabela

| Atributo  | Valor                |
|:----------|:---------------------|
| Nome      | `THGQMGCRIT`  |
| Instancia | `QmFornCriterio`     |
| Sequencia | AUTO (`CODCRIT`)  |
| Dual-DB   | Oracle + SQL Server  |

---

## Campos

| # | Rotulo      | Coluna       | Tipo     | Tam | Obrig | Aba    | Grupo | Opcoes / Comportamento                                  |
|:--|:------------|:-------------|:---------|:----|:------|:-------|:------|:--------------------------------------------------------|
| 1 | Id          | `CODCRIT` | INTEIRO  | -   | PK    | __main | -     | readOnly, auto sequence                                 |
| 2 | Descricao   | `DESCRICAO`  | TEXTO    | 100 | Sim   | __main | -     | isPresentation=S. Ex: Qualidade, Prazo, Tecnico         |
| 3 | Peso (%)    | `PESO`       | DECIMAL  | -   | Sim   | __main | -     | Soma de todos os pesos ativos deve ser = 100            |
| 4 | Ativo       | `ATIVO`      | CHECKBOX | 1   | Nao   | __main | -     | Default S                                               |

---

## Dados Iniciais (dbscripts/V1.xml)

| DESCRICAO | PESO |
|:----------|:-----|
| Qualidade | 40   |
| Tecnico   | 30   |
| Prazo     | 20   |
| Seguranca | 10   |

---

## Regras de Negocio

1. Soma dos pesos de criterios ATIVO='S' deve ser 100 — validacao no ScoreFornBusinessService
2. Listener valida soma antes de calcular score
3. Dados iniciais inseridos via V1.xml com verificacao de existencia

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGCRIT + INSERT dados iniciais

### Dicionario de Dados
- [ ] `datadictionary/THGQMGCRIT.xml`

### Menu
- [ ] `datadictionary/menu.xml` — QmFornCriterio em pasta Fornecedores > Configuracao
