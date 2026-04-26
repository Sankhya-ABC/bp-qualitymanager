# BP - QualityManager

Sistema de Gestao da Qualidade para Sankhya Om, desenvolvido como Addon Studio 2.0.
Atende aos requisitos da **ABNT NBR ISO 9001:2015**.

## Modulos

| Modulo | ISO 9001 | Descricao | Documentacao |
|:-------|:---------|:----------|:-------------|
| **Core** | Transversal | Configuracao, audit log, prioridades, fases | [README_CORE.md](docs/README_CORE.md) |
| **RNC** | 8.7 + 10.2 | Nao conformidades com workflow 10 fases | [README_RNC.md](docs/README_RNC.md) |
| **Fornecedores** | 8.4 | Qualificacao, score IQF, questionarios | [README_FORNECEDOR.md](docs/README_FORNECEDOR.md) |
| **Documentos** | 7.5 | Controle de documentos, versoes, permissoes | [README_DOCUMENTOS.md](docs/README_DOCUMENTOS.md) |
| **Mudancas** | 6.3 + 8.5.6 | Gestao de mudancas com avaliacao de riscos | [README_MUDANCAS.md](docs/README_MUDANCAS.md) |
| **Auditoria** | 9.2 | Programa, registro, checklist, constatacoes | [README_AUDITORIA.md](docs/README_AUDITORIA.md) |
| **Indicadores** | 9.1 + 9.3 | KPIs, metas, medicoes, analise critica | [README_INDICADORES.md](docs/README_INDICADORES.md) |

## Arquitetura

- **Plataforma:** Sankhya Om (Addon Studio 2.0)
- **Java:** 8 (LTS)
- **Build:** Gradle com plugin `br.com.sankhya.addonstudio`
- **Padrao:** MVC + Package by Feature
- **Dual-DB:** Oracle + SQL Server
- **Prefixo tabelas:** `THGQMG` (Tabelas Hagious - QualityManager)

## Estrutura do Projeto

```
bp-qualitymanager/
  build.gradle                    # Config raiz
  settings.gradle                 # Modulos: model + vc
  model/
    build.gradle
    src/main/java/br/com/hagious/qualitymanager/
      core/                       # Configuracao, log, dominio
        entity/
        repository/
        service/
      rnc/                        # Nao conformidades
        entity/
        repository/
        service/
        listener/
        exception/
      fornecedor/                 # Qualificacao fornecedores
        entity/
        repository/
        service/
        listener/
      documentos/                 # Controle de documentos
        entity/
        repository/
      mudancas/                   # Gestao de mudancas
        entity/
        repository/
      auditoria/                  # Auditoria interna
        entity/
        repository/
      indicadores/                # Indicadores e KPIs
        entity/
        repository/
  vc/                             # Web layer
  dbscripts/                      # Migracoes banco (V001..V057)
  datadictionary/                 # Dicionario de dados XML
  tasks/                          # Cards de especificacao
  instructions/                   # Governanca e padroes
  docs/                           # Documentacao por modulo
```

## Convencoes

| Item | Padrao |
|:-----|:-------|
| Tabelas | `THGQMG<CONTEXTO>` (sem underscores) |
| PK cadastros | `COD<ENTIDADE>` |
| PK movimentos | `NU<ENTIDADE>` |
| Colunas nativas | `QM_<CAMPO>` |
| Instance names | `Qm<Modulo><Nome>` |
| Entities | `@JapeEntity(entity, table)` + `@Column(name)` somente |
| Repositories | `@Repository` + `JapeRepository<BigDecimal, Entity>` |
| Services | `@Component` + `@Inject` construtor |
| Listeners | `PersistenceEventAdapter` + `getInstanceName()` exato do XML |

## Build e Deploy

```bash
# Deploy local (WildFly configurado)
./gradlew clean deployAddon

# Variavel de ambiente necessaria
export WILDFLY_HOME=/caminho/wildfly
```

## Banco de Dados

| Banco | Porta | SID/DB | Usuario | Senha |
|:------|:------|:-------|:--------|:------|
| Oracle | 1521 | XE | SANKHYA | developer |
| SQL Server | 1433 | jiva | SANKHYA | developer |

## Numeros do Projeto

| Artefato | Quantidade |
|:---------|:-----------|
| Modulos | 7 |
| Cards de especificacao | 56 |
| DB Scripts | 57 |
| Data Dictionary XMLs | 55 |
| Java Files | 115 |
| Tabelas addon | ~50 |
