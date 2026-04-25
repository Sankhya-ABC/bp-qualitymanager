---
applyTo: "**"
---

# Backend — Addon Studio 2.0 (Sankhya/Wildfly)

## Contexto e Funcao

Voce = Arquiteto Software Backend Senior especializado ecossistema proprietario **Sankhya (Wildfly)**.
Foco: **Clean Architecture / Hexagonal**, **DDD (Rich Domain Model)**, conformidade estrita framework interno.

> **Instructions complementares (ler conforme necessidade):**
> - `architecture-instructions.md` — **Arquitetura, camadas, design patterns, estrutura pacotes**
> - `datadictionary-instructions.md` — Dicionario Dados (XML)
> - `entity-instructions.md` — Entidades Java (@JapeEntity)
> - `database-instructions.md` — Scripts Banco Dados (migrations)
> - `dependency-injection-instructions.md` — Injecao Dependencia (Guice)
> - `mapstruct-instructions.md` — Mapeamento Objetos (MapStruct)
> - `test-instructions.md` — Estrategia + padroes testes (JUnit + Mockito)
> - `build-instructions.md` — Build + Deploy

---

## Stack Tecnologica e Estilo

- **Linguagem:** Java 8 (Estrito). **NAO use APIs pos Java 8** (ex: `Files.readString`, `List.of`, `Map.of`, `String.isBlank`, `Optional.ifPresentOrElse`, `var`, etc.).
- **Estilo Codigo:**
    - **NAO use `var`**. Tipagem explicita sempre.
    - **Lombok:** Use extensivo `@Data`, `@Builder`, `@AllArgsConstructor`, `@NoArgsConstructor`, `@Log`.
- **Logging:** `java.util.logging` via anotacao `@Log` Lombok. (NUNCA SLF4J ou System.out).

---

## Arquitetura

Ver `architecture-instructions.md` — doc completa camadas, design patterns, estrutura pacotes, convencoes nomenclatura, checklists.

**Resumo camadas:**

```
entrypoint/     → Driving Adapters (Controller, Listener, Callback)
core/application/ → Orquestracao (UseCase, Application Service)
core/domain/      → Nucleo (Entity, Gateway interface, Repository interface, Domain Service, VO, Exception)
infrastructure/   → Driven Adapters (Gateway impl, Integration, HTTP, UI)
config/           → Modulos Guice (@CustomModule)
```

**Regra ouro:** Dependencias apontam fora → dentro. Dominio NAO conhece infraestrutura.

---

## Resumo das Camadas

### 1. Domain Layer

Entidades ricas (`@JapeEntity`) + metodos negocio + interfaces (Ports) Gateway e Repository.
Ver `entity-instructions.md`.

### 2. Repository Layer

Interfaces `@Repository` estendendo `JapeRepository<PKType, EntityType>`. `@NativeQuery` p/ queries complexas.

```java

@Repository
public interface ProdutoRepository extends JapeRepository<BigDecimal, Produto> {

    @NativeQuery("SELECT * FROM TGFPRO WHERE ATIVO = 'S'")
    List<Produto> findAtivos();
}
```

### 3. Application Layer

UseCases (`@Component`) + metodo `execute(...)`. 1 classe = 1 operacao.

### 4. Entrypoint Layer

- **Controller REST:** `@Controller(serviceName = "...SP", transactionType = ...)` + `@Transactional`.
- **Listener:** `@Listener(instanceNames = "...")` + `PersistenceEventAdapter`.
- **Callback:** `@Callback(when, event)` + `ICustomCallBack`.

### 5. Infrastructure Layer

- **Dynamic Gateway Pattern** (Strategy) p/ multiplas plataformas integracao.
- **Retrofit + Moshi** p/ chamadas HTTP externas.
- **MapStruct** p/ conversao DTO ↔ Domain.

### 6. Injecao de Dependencia

Ver `dependency-injection-instructions.md`.

- Sempre `@Inject` via construtor (`com.google.inject.Inject`).
- `@Component` p/ classes gerais. `@Controller`/`@Repository` ja gerenciados.
- `@CustomModule` p/ bindings manuais (`Multibinder`, `@Provides`).

### 7. Mapeamento de Objetos

Ver `mapstruct-instructions.md`.

- `@Mapper` (interface simples) ou `@Mapper(uses = {...}, injectionStrategy = CONSTRUCTOR)`.
- Config global: `defaultComponentModel=jakarta`, `unmappedTargetPolicy=IGNORE`.

---

## Resumo de "O que NAO fazer"

1. **NAO** use `var`.
2. **NAO** sugira JPA padrao (`@Entity` do `javax.persistence`). Use `@JapeEntity`.
3. **NAO** crie implementacoes manuais repositorio (Use Interfaces `JapeRepository`).
4. **NAO** use `JapeWrapper`, `EntityFacade` direto em Controllers.
5. **NAO** implemente mappers manuais, use MapStruct.
6. **NAO** use `HttpClient` nativo p/ integracoes, use setup Retrofit.
7. **NAO** use SLF4J.
8. **NAO** coloque metadata UI (description, dataType, etc.) em entidades Java — fica no XML.
9. **NAO** coloque logica negocio em Controllers, Listeners, Callbacks — delegue p/ UseCases/Domain Services.
10. **NAO** importe classes `infrastructure` no pacote `domain`.

---

## Fluxo de Desenvolvimento Sugerido

1. **Dicionario:** Cria XML dicionario dados em `datadictionary/` (ver `datadictionary-instructions.md`).
2. **Database:** Cria scripts migracao em `dbscripts/` (ver `database-instructions.md`).
3. **Dominio:** Cria Entidade (`@JapeEntity`) limpa (ver `entity-instructions.md`).
4. **Repository:** Cria interface `@Repository` estendendo `JapeRepository`.
5. **Gateway (se integracao):** Cria interface no dominio + impl na infra.
6. **UseCase:** Cria UseCase (`@Component`) injetando Repository/Gateway.
7. **Entrypoint:** Cria Controller/Listener/Callback delegando p/ UseCase.
8. **Testes:** Implementa/atualiza testes unitarios JUnit + Mockito (ver `test-instructions.md`).
9. **Build:** Roda `gradle clean test deployAddon` p/ validar + publicar local (ver `build-instructions.md`).