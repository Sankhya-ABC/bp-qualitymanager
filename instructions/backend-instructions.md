---
applyTo: "**"
---

# Backend - Addon Studio 2.0 (Sankhya/Wildfly)

## Contexto e Funcao

Voce = Arquiteto Software Backend Senior especializado no ecossistema proprietario **Sankhya (Wildfly)**.
Foco: **MVC + Package by Feature**, conformidade estrita ao framework interno.

> **Instructions complementares (ler conforme necessidade):**
> - `architecture-instructions.md` - **Arquitetura, camadas, organizacao por feature**
> - `controller-instructions.md` - Controllers REST
> - `repository-instructions.md` - Repositorios JapeRepository
> - `entity-instructions.md` - Entidades Java (@JapeEntity)
> - `datadictionary-instructions.md` - Dicionario de Dados (XML)
> - `database-instructions.md` - Scripts Banco (migrations)
> - `dependency-injection-instructions.md` - Injecao de Dependencia (Guice)
> - `mapstruct-instructions.md` - Mapeamento de Objetos (MapStruct)
> - `test-instructions.md` - Estrategia + padroes de testes (JUnit + Mockito)
> - `build-instructions.md` - Build + Deploy

---

## Stack Tecnologica e Estilo

- **Linguagem:** Java 8 (estrito). **NAO use APIs pos Java 8** (ex: `Files.readString`, `List.of`, `Map.of`, `String.isBlank`, `Optional.ifPresentOrElse`, `var`).
- **Estilo de codigo:**
    - **NAO use `var`**. Tipagem explicita sempre.
    - **Lombok:** uso extensivo de `@Data`, `@Builder`, `@AllArgsConstructor`, `@NoArgsConstructor`, `@Log`.
- **Logging:** `java.util.logging` via anotacao `@Log` do Lombok. (NUNCA SLF4J ou `System.out`.)

---

## Arquitetura

Ver `architecture-instructions.md` - documento completo sobre camadas, organizacao por feature, convencoes e checklists.

**Resumo:**

```
br/com/hagious/qualitymanager/<feature>/
|-- controller/   -> Controllers REST (@Controller)
|-- service/      -> Services com regra de negocio (@Component)
|-- repository/   -> Repositories (@Repository, JapeRepository)
|-- entity/       -> Entidades (@JapeEntity)
|-- vo/           -> Value Objects, Enums, PKs compostas
|-- dto/          -> Request/Response DTOs
|-- mapper/       -> Mappers MapStruct
|-- listener/     -> (opcional) Listeners
|-- callback/     -> (opcional) Callbacks
|-- exception/    -> (opcional) Excecoes da feature
```

**Regra de ouro:** Controller delega para Service. Service concentra regra. Repository so faz acesso a dados. Entity carrega o estado.

---

## Resumo das Camadas

### 1. Entity

Entidade `@JapeEntity` com mapeamento minimo. Pode ter metodos de dominio simples sobre o proprio estado. Ver `entity-instructions.md`.

### 2. Repository

Interface `@Repository` estendendo `JapeRepository<PKType, EntityType>`. Use `@Criteria` para filtros simples e `@NativeQuery` para queries complexas. Ver `repository-instructions.md`.

```java
@Repository
public interface RegistroRepository extends JapeRepository<Integer, Registro> {

    @Criteria(clause = "this.ATIVO = :ativo")
    List<Registro> findByAtivo(Boolean ativo);
}
```

### 3. Service

`@Component` que centraliza regra de negocio. Injeta Repositories. Lanca excecoes tipadas em vez de `RuntimeException` generica.

```java
@Log
@Component
public class RegistroService {

    private final RegistroRepository repository;

    @Inject
    public RegistroService(RegistroRepository repository) {
        this.repository = repository;
    }

    public Registro criar(Registro registro) throws Exception {
        // valida regra de negocio
        return repository.save(registro);
    }
}
```

### 4. Controller

`@Controller(serviceName = "...SP", transactionType = ...)` + `@Transactional` em metodos de escrita. Recebe Request DTO, delega ao Service, retorna Response DTO. Ver `controller-instructions.md`.

### 5. DTO + Mapper

Request com `@Valid` + `@NotNull/@NotBlank/...`. Response sem validacao. Conversao Entity <-> DTO sempre via MapStruct. Ver `mapstruct-instructions.md`.

### 6. Listener / Callback (opcional)

- **Listener:** `@Listener(instanceNames = "...")` + `PersistenceEventAdapter` -> reage a eventos de persistencia, delega ao Service.
- **Callback:** `@Callback(when, event)` + `ICustomCallBack` -> hook do framework, delega ao Service.

### 7. Injecao de Dependencia

Ver `dependency-injection-instructions.md`.

- Sempre `@Inject` via construtor (`com.google.inject.Inject`).
- `@Component` para classes gerais. `@Controller`/`@Repository` ja gerenciados.
- `@CustomModule` para bindings manuais (`@Provides`).

### 8. Mapeamento de Objetos

Ver `mapstruct-instructions.md`.

- `@Mapper` (interface simples) ou `@Mapper(uses = {...}, injectionStrategy = CONSTRUCTOR)`.
- Config global: `defaultComponentModel=jakarta`, `unmappedTargetPolicy=IGNORE`.

---

## Resumo de "O que NAO fazer"

1. **NAO** use `var`.
2. **NAO** sugira JPA padrao (`@Entity` do `javax.persistence`). Use `@JapeEntity`.
3. **NAO** crie implementacoes manuais de Repository (use interfaces `JapeRepository`).
4. **NAO** use `JapeWrapper`/`EntityFacade` direto em Controllers.
5. **NAO** implemente mappers manuais - use MapStruct.
6. **NAO** use SLF4J.
7. **NAO** coloque metadata UI (description, dataType, etc.) em entidades Java - fica no XML.
8. **NAO** coloque regra de negocio em Controllers, Listeners ou Callbacks - delegue ao Service.
9. **NAO** acesse Repository direto a partir de Controller - sempre via Service.
10. **NAO** misture artefatos de features diferentes; compartilhamento somente via `shared/`.

---

## Fluxo de Desenvolvimento Sugerido

1. **Dicionario:** crie XML do dicionario em `datadictionary/` (ver `datadictionary-instructions.md`).
2. **Database:** crie scripts de migracao em `dbscripts/` (ver `database-instructions.md`).
3. **Entity:** crie a entidade `@JapeEntity` em `<feature>/entity/` (ver `entity-instructions.md`).
4. **Repository:** crie a interface `@Repository` em `<feature>/repository/`.
5. **Service:** crie o Service `@Component` em `<feature>/service/` injetando o Repository.
6. **DTO + Mapper:** crie Request/Response em `<feature>/dto/` e Mapper em `<feature>/mapper/`.
7. **Controller:** crie o Controller em `<feature>/controller/` delegando ao Service.
8. **Listener/Callback (opcional):** crie em `<feature>/listener/` ou `<feature>/callback/` quando necessario.
9. **Testes:** implemente/atualize testes unitarios JUnit + Mockito (ver `test-instructions.md`).
10. **Build:** rode `gradle clean test deployAddon` para validar e publicar local (ver `build-instructions.md`).
