# Card IND-02 — Metas por Indicador

## Identificacao
| Atributo        | Valor                                   |
|:----------------|:----------------------------------------|
| Modulo          | Indicadores                             |
| Fase Roadmap    | Fase 4                                  |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 6.2   |
| Ordem no modulo | 002 de 005                              |
| Depende de      | IND-01                                  |

## Tabela
| Atributo  | Valor               |
|:----------|:--------------------|
| Nome      | `THGQMGMETA`      |
| Instancia | `ThgIndMeta`         |
| Sequencia | AUTO (`CODMETA`)     |
| Dual-DB   | Oracle + SQL Server |

## Campos
| # | Rotulo       | Coluna         | Tipo      | Tam | Obrig | Opcoes                                                     |
|:--|:-------------|:---------------|:----------|:----|:------|:-----------------------------------------------------------|
| 1 | Id Meta      | `CODMETA`       | INTEIRO   | -   | PK    | readOnly, auto sequence                                    |
| 2 | Indicador    | `CODIND`  | INTEIRO   | -   | Sim   | readOnly. FK para THGQMGIND                        |
| 3 | Periodo      | `PERIODO`      | TEXTO     | 10  | Sim   | isPresentation=S. Formato MM/YYYY. Ex: 04/2025             |
| 4 | Meta         | `META`         | DECIMAL   | -   | Sim   | Valor alvo para o periodo                                  |
| 5 | Realizado    | `REALIZADO`    | DECIMAL   | -   | Nao   | readOnly. Atualizado pelo MedicaoIndicadorListener         |
| 6 | Atingimento  | `ATINGIMENTO`  | DECIMAL   | -   | Nao   | readOnly. % atingimento = (REALIZADO/META)*100 HALF_UP     |
| 7 | Status       | `STATUS`       | LISTA     | 1   | Nao   | readOnly. A=Atingido, P=Parcial (80-99%), N=Nao atingido  |
| 8 | Observacoes  | `OBSERVACOES`  | TEXTO     | 300 | Nao   | Comentarios sobre o desempenho                             |
| 9 | Criado por   | `CODUSU`       | PESQUISA  | -   | Nao   | readOnly. Auto: $ctx_usuario_logado. visivel=N             |

## Regras de Negocio
1. REALIZADO e ATINGIMENTO calculados pelo MedicaoIndicadorListener apos INSERT em THGQMGMED
2. Status A: realizado >= meta (SENTIDO='M') ou realizado <= meta (SENTIDO='N')
3. Status P: atingimento entre 80% e 99% da meta
4. Status N: atingimento abaixo de 80%

## Artefatos
- [ ] `datadictionary/THGQMGMETA.xml`
- [ ] `dbscripts/V1.xml` — DDL THGQMGMETA
- [ ] `model/.../listeners/MedicaoIndicadorListener.java` — instanceName: `ThgIndMedicao`
