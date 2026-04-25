---
applyTo: "**/*.java"
---

# Injeção de Dependência (Guice) - Addon Studio 2.0

Addon Studio 2.0 usa **Google Guice** como container DI. Anotações estereótipo customizadas fazem auto-scan. Este doc descreve regras, padrões e boas práticas DI no contexto MVC + package by feature.

---

## 1. Regra de Ouro

**Sempre injeção via construtor** com `@Inject` de `com.google.inject.Inject`.

```java
import com.google.inject.Inject;

@Component
public class RegistroService {

    private final RegistroRepository repository;

    @Inject
    public RegistroService(RegistroRepository repository) {
        this.repository = repository;
    }
}
```

> **NUNCA** `@Inject` de `javax.inject`. Sempre `com.google.inject.Inject`.

---

## 2. Estereótipos (Anotações de Auto-Scanning)

Framework escaneia e registra classes com:

| Anotação | Pacote | Gerenciamento | Uso |
|:---------|:-------|:--------------|:----|
| `@Controller` | `br.com.sankhya.studio.annotations` | **Automático** - NÃO adicionar `@Component` | Entrypoints REST (camada Controller) |
| `@Repository` | `br.com.sankhya.studio.stereotypes` | **Automático** - NÃO adicionar `@Component` | Interfaces de acesso a dados (`JapeRepository`) |
| `@Component` | `br.com.sankhya.studio.stereotypes` | **Automático** | Services, Listeners, Callbacks, mappers auxiliares |
| `@ControllerAdvice` | `br.com.sankhya.studio.web` | **Automático** | Tratamento global de exceções |
| `@CustomModule` | `br.com.sankhya.studio.stereotypes` | **Automático** | Módulos Guice customizados (equivale a `AbstractModule`) |

### Quando usar cada estereótipo

```
@Controller       -> Controllers REST em <feature>/controller/ (serviceName obrigatório com sufixo "SP")
@Repository       -> Interfaces JapeRepository em <feature>/repository/ (NÃO crie implementação manual)
@Component        -> Services em <feature>/service/, Listeners, Callbacks, mappers auxiliares
@CustomModule     -> Módulos de configuração Guice em config/
@ControllerAdvice -> Handler global de exceções
```

---

## 3. Classes por Estereótipo

### 3.1 `@Controller` - Entrypoints REST

```java
import br.com.sankhya.studio.annotations.Controller;
import br.com.sankhya.studio.annotations.enums.EJBTransactionType;
import br.com.sankhya.studio.persistence.Transactional;
import com.google.inject.Inject;

@Controller(
    serviceName = "RegistroControllerSP",
    transactionType = EJBTransactionType.Supports
)
public class RegistroController {

    private final RegistroService service;
    private final RegistroMapper mapper;

    @Inject
    public RegistroController(RegistroService service, RegistroMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Transactional
    public RegistroResponse criar(CriarRegistroRequest request) throws Exception {
        Registro entity = mapper.toEntity(request);
        Registro salvo = service.criar(entity);
        return mapper.toResponse(salvo);
    }
}
```

**Regras:**
- `serviceName` obrigatório, sufixo `"SP"`.
- `transactionType` define tipo de transação EJB (`Supports`, `Required`, etc.).
- Métodos que alteram dados precisam `@Transactional`.
- **NÃO** adicionar `@Component` - `@Controller` já é gerenciado pelo framework.

### 3.2 `@Repository` - Interfaces de Acesso a Dados

```java
import br.com.sankhya.sdk.data.repository.JapeRepository;
import br.com.sankhya.studio.stereotypes.Repository;

@Repository
public interface RegistroRepository extends JapeRepository<Integer, Registro> {
    // métodos declarativos - o framework gera a implementação
}
```

**Regras:**
- Sempre **interface** (nunca classe concreta).
- Estende `JapeRepository<PKType, EntityType>`.
- **NÃO** adicionar `@Component` - framework gera implementação e registra no Guice.
- Injetável direto em qualquer `@Component` ou `@Controller`.

### 3.3 `@Component` - Services e demais classes injetáveis

Para qualquer classe injetável que não encaixa em `@Controller` ou `@Repository`.

**Exemplos `@Component`:**

| Tipo | Exemplo |
|:-----|:--------|
| Service da feature | `RegistroService` |
| Listener | `RegistroListener` |
| Callback | `RegistroCallback` |
| Mapper auxiliar (usado por MapStruct `uses={}`) | `StringMappingNormalizer` |

```java
import br.com.sankhya.studio.stereotypes.Component;
import com.google.inject.Inject;

@Component
public class RegistroService {

    private final RegistroRepository repository;

    @Inject
    public RegistroService(RegistroRepository repository) {
        this.repository = repository;
    }

    public List<Registro> listarAtivos() {
        return repository.findByAtivo(Boolean.TRUE);
    }
}
```

### 3.4 `@ControllerAdvice` - Tratamento Global de Exceções

```java
import br.com.sankhya.studio.web.ControllerAdvice;
import br.com.sankhya.studio.web.ExceptionHandler;
import lombok.extern.java.Log;

@Log
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public void handleNotFound(EntityNotFoundException e) {
        log.log(Level.INFO, "Entidade nao encontrada: {0}", e.getMessage());
        throw e;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleBadRequest(IllegalArgumentException e) {
        log.log(Level.WARNING, "Argumento invalido: {0}", e.getMessage());
        throw e;
    }
}
```

**Regras:**
- Um por projeto (centraliza tratamento de exceções).
- Cada método trata exceção específica com `@ExceptionHandler`.
- Dispensa `@Component`.

---

## 4. Módulos Customizados (`@CustomModule`)

Quando o auto-scan não basta (bindings manuais, `@Provides` para clientes externos), crie módulo Guice com `@CustomModule` em `config/`.

### 4.1 `@Provides` - Factory Methods

Para criar instâncias com configuração especial (ex: clientes HTTP, properties).

```java
import br.com.sankhya.studio.stereotypes.CustomModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

@CustomModule
public class IntegracaoConfig extends AbstractModule {

    @Provides
    @Singleton
    public MeuClient provideMeuClient() {
        return new MeuClient("https://api.exemplo.com/");
    }
}
```

**Regras:**
- `@Provides` marca método como factory.
- `@Singleton` garante instância única.
- Parâmetros resolvidos automaticamente pelo Guice.
- Classe **deve** estender `AbstractModule` e ter `@CustomModule`.
- Coloque módulos em `br/com/hagious/qualitymanager/config/`.

---

## 5. Escopo: `@Singleton`

Por padrão Guice cria **nova instância** a cada injeção. Use `@Singleton` para instância única.

```java
import com.google.inject.Singleton;

@Component
@Singleton
public class CacheService {
    // Uma única instância reutilizada em toda a aplicação
}
```

### Quando usar `@Singleton`

| Usar `@Singleton` | Não usar (padrão) |
|:------------------|:------------------|
| Clientes HTTP, executors, factories | Services de feature |
| Caches e pools | Controllers |
| Resolvers e registries | Mappers auxiliares |

> `@Singleton` **sem** `@Component` não é auto-scaneado. Precisa estar em `@CustomModule` ou ser injetado por classe que o Guice conhece.

---

## 6. `Provider<T>` - Injeção Lazy / Circular

Em dependência circular ou resolução lazy, injete `Provider<T>` em vez de `T`.

```java
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class MeuComponente {

    private final Provider<DependenciaPesada> dependenciaProvider;

    @Inject
    public MeuComponente(Provider<DependenciaPesada> dependenciaProvider) {
        this.dependenciaProvider = dependenciaProvider;
    }

    public void executar() {
        // .get() resolve a dependência no momento da chamada (lazy)
        DependenciaPesada dep = dependenciaProvider.get();
        dep.fazerAlgo();
    }
}
```

### Quando usar `Provider<T>`

- **Dependência circular:** A depende de B que depende de A.
- **Singleton com dep request-scoped:** ex: classe singleton que precisa de service com contexto.
- **Lazy init:** adiar criação de objeto custoso até primeiro uso.

---

## 7. MapStruct e o Container Guice

Mappers MapStruct são registrados automaticamente no Guice (config global `defaultComponentModel = "jakarta"`).

### Mapper simples (sem dependências)

```java
@Mapper
public interface RegistroMapper {
    RegistroResponse toResponse(Registro entity);
}
```

Injetável direto:

```java
@Inject
public RegistroController(RegistroMapper mapper) { ... }
```

### Mapper com `uses` (dependência de classe `@Component`)

```java
@Mapper(
    uses = {StringMappingNormalizer.class},
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface RegistroMapper {
    Registro toEntity(CriarRegistroRequest dto);
}
```

**Regras:**
- Classe em `uses` **deve** ser `@Component`.
- Use `injectionStrategy = InjectionStrategy.CONSTRUCTOR` para garantir que CDI injete via construtor.

---

## 8. Padrão de Camadas e DI

```
------------------------------------------------------------
|  @Controller (em <feature>/controller/)                  |
|    |-- @Inject Service (@Component)                       |
|    |-- @Inject Mapper (MapStruct - auto-registrado)       |
------------------------------------------------------------
|  @Component Service (em <feature>/service/)              |
|    |-- @Inject Repository (@Repository - interface)       |
|    |-- @Inject Outro Service (mesma feature ou shared/)  |
------------------------------------------------------------
|  @Repository Repository (em <feature>/repository/)       |
|    (Framework gera implementação)                         |
------------------------------------------------------------
|  @Component Listener / Callback                           |
|    |-- @Inject Service                                    |
------------------------------------------------------------
|  @CustomModule (em config/)                               |
|    |-- @Provides para factories de clientes externos      |
------------------------------------------------------------
```

---

## 9. Checklist

### Nova classe injetável

1. [ ] Identificar estereótipo correto (`@Controller`, `@Repository`, `@Component`).
2. [ ] Usar `@Inject` de `com.google.inject.Inject` no construtor.
3. [ ] Declarar deps `private final`, inicializar no construtor.
4. [ ] **NÃO** usar `new` para criar deps - sempre injetar.
5. [ ] **NÃO** misturar estereótipos (ex: `@Component` + `@Controller`).
6. [ ] Colocar a classe no pacote correto da feature.

### Novo módulo customizado

1. [ ] Criar classe que estende `AbstractModule`.
2. [ ] Anotar com `@CustomModule`.
3. [ ] Usar `@Provides @Singleton` para factories.
4. [ ] Colocar em `config/`.

### Novo singleton com dep circular

1. [ ] Anotar com `@Singleton`.
2. [ ] Dep circular? Usar `Provider<T>`.
3. [ ] Não é `@Component`? Garantir registro em algum `@CustomModule`.

---

## 10. Erros Comuns

| Erro | Correção |
|:-----|:---------|
| Usar `javax.inject.Inject` | Sempre `com.google.inject.Inject`. |
| Adicionar `@Component` em `@Controller` | `@Controller` já é gerenciado. Remover `@Component`. |
| Adicionar `@Component` em `@Repository` | `@Repository` já é gerenciado. Remover `@Component`. |
| Criar implementação manual de Repository | Use interface `JapeRepository` - framework gera implementação. |
| Usar `new` para instanciar dep | Injete via construtor. Guice resolve automático. |
| Dep circular com `@Singleton` | Use `Provider<T>` para quebrar o ciclo. |
| Classe sem estereótipo que precisa injeção | Adicione `@Component` ou registre em `@CustomModule`. |
| `@Singleton` sem `@Component` e sem módulo | Guice não encontra. Adicione um dos dois. |
| Mapper MapStruct com `uses` sem `injectionStrategy` | Adicione `injectionStrategy = InjectionStrategy.CONSTRUCTOR`. |
| `@Provides` em classe sem `@CustomModule` | Guice não encontra o provider. Adicione `@CustomModule`. |
| Service em pacote fora da feature | Mover para `<feature>/service/`. |
