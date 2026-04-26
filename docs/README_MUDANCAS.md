# Modulo Gestao de Mudancas

ISO 9001 clausulas 6.3 (Planejamento de mudancas) e 8.5.6 (Controle de mudancas).

## Funcionalidades

- Cadastro de mudancas com workflow de 5 fases
- Acoes com analise 5W2H
- Questionarios de avaliacao de impacto
- Avaliacao de riscos (probabilidade x impacto)
- Fluxo de aprovacao multi-responsavel

## Workflow de 5 Fases

```
1. Cadastro → 2. Questionario → 3. Acoes (5W2H)
→ 4. Avaliacao de Riscos → 5. Aprovacao
```

## Tabelas

| Tabela | Instance | Descricao |
|:-------|:---------|:----------|
| THGQMGGM | ThgGmCadastro | Registro principal da mudanca |
| THGQMGGMFASE | ThgGmFase | Fases do workflow (dominio, 5 fases) |
| THGQMGGMACAO | ThgGmAcao | Acoes 5W2H |
| THGQMGGMQUEM | ThgGmQuem | Responsaveis por acao |
| THGQMGGMQUEST | ThgGmQuestionario | Questionario vinculado |
| THGQMGGMPERG | ThgGmPergunta | Respostas do questionario |
| THGQMGGMAVAL | ThgGmAvaliacao | Avaliacao de riscos |
| THGQMGGMRESP | ThgGmAprovacao | Aprovacao por responsavel |

## Tipos de Mudanca

PR=Processo, PD=Produto, SI=Sistema, EQ=Equipamento.

## Matriz de Risco

| Probabilidade x Impacto | Baixo | Medio | Alto |
|:-------------------------|:------|:------|:-----|
| Baixa | Baixo | Baixo | Medio |
| Media | Baixo | Medio | Alto |
| Alta | Medio | Alto | Critico |

## Artefatos
- dbscripts: V035-V043 (9 scripts, incluindo INSERT fases)
- datadictionary: 8 XMLs
- Java: 8 entities, 8 repositories
