# Modulo Indicadores

ISO 9001 clausulas 9.1 (Monitoramento, medicao, analise e avaliacao) e 9.3 (Analise critica pela direcao).

## Funcionalidades

- Cadastro de indicadores com meta, formula e frequencia
- Metas por periodo (mensal, trimestral, semestral, anual)
- Medicoes com calculo automatico de atingimento
- Atas de analise critica pela direcao (entradas e saidas)
- Dashboard de maturidade ISO (cross-module)

## Tabelas

| Tabela | Instance | Descricao |
|:-------|:---------|:----------|
| THGQMGIND | QmIndIndicador | Cadastro de indicadores |
| THGQMGMETA | QmIndMeta | Metas por periodo |
| THGQMGMED | QmIndMedicao | Medicoes realizadas |
| THGQMGATA | QmAcrAta | Ata de analise critica |
| THGQMGENT | QmAcrEntrada | Entradas da analise critica |
| THGQMGSAI | QmAcrSaida | Saidas/decisoes da analise critica |

## Tipos de Indicador

| Tipo | Descricao |
|:-----|:----------|
| E | Eficacia |
| F | Eficiencia |
| P | Produtividade |

## Sentido do Indicador

| Sentido | Descricao |
|:--------|:----------|
| C | Quanto maior melhor |
| B | Quanto menor melhor |

## Frequencia de Medicao

M=Mensal, T=Trimestral, S=Semestral, A=Anual.

## Ata de Analise Critica (ACR)

Conforme ISO 9001 clausula 9.3, a analise critica inclui:
- **Entradas:** Status acoes anteriores, mudancas internas/externas, desempenho processos, NCs, resultados auditoria, satisfacao cliente
- **Saidas:** Decisoes e acoes (com responsavel e prazo)

## Artefatos
- dbscripts: V052-V057 (6 scripts)
- datadictionary: 6 XMLs
- Java: 6 entities, 6 repositories
