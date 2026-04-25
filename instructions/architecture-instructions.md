---
applyTo: "**/*.java"
---

# Arquitetura e Design Patterns ? Addon Studio 2.0

Documento define arquitetura referencia e design patterns em projetos Addon Studio 2.0. Objetivo: guiar IA e devs na criacao de codigo que respeite camadas, responsabilidades e limitacoes do framework.

> **Referencia complementar:**
> - `controller-instructions.md` ? Controller REST, DTOs, protocolo HTTP, @ControllerAdvice
> - `dependency-injection-instructions.md` ? DI com Guice
> - `entity-instructions.md` ? Entidades Java
> - `mapstruct-instructions.md` ? Mapeamento de objetos

---

## 1. Visao Geral: Clean Architecture Adaptada

Projeto segue adaptacao de **Clean Architecture / Hexagonal Architecture** dentro das limitacoes do framework Sankhya (Wildfly/EJB). Dependencias apontam **de fora para dentro** ? camada dominio nao conhece infraestrutura.

```
-------------------------------------------------------------------
|  ENTRYPOINT (Driving Adapters)                                  ?
|  Controller, Listener, Callback                                 ?
|    Recebem est�mulos externos ? delegam para Application        ?
-------------------------------------------------------------------
|  APPLICATION (Orquestracao)                                     ?
|  UseCase, Application Service                                   ?
|    Orquestram fluxo ? chamam Domain e Infrastructure            ?
-------------------------------------------------------------------
|  DOMAIN (Nucleo)                                                ?
|  Entity, Value Object, Domain Service, Gateway (interface),     ?
|  Repository (interface), Exception                              ?
|    Regras de negocio puras ? sem dependencia de infra           ?
-------------------------------------------------------------------
|  INFRASTRUCTURE (Driven Adapters)                               ?
|  Gateway impl, Integration, HTTP client, UI utils               ?
|    Implementa interfaces do dominio ? acessa mundo externo      ?
-------------------------------------------------------------------
|  CONFIG                                                         ?
|  CustomModule (Guice bindings)                                  ?
|    Wiring de dependencias                                       ?
-------------------------------------------------------------------
```

---

## 2. Estrutura de Pacotes

```
<addon>/
|-- config/                          # Modulos Guice (@CustomModule)
|   |-- integration/
|-- core/
|   |-- application/                 # Camada de aplicacao
|   |   |-- service/                 # Application Services (interfaces + orquestracao)
|   |   |-- usecase/                 # UseCases (1 classe = 1 operacao)
|   |   |   |-- <feature>/
|   |   |   |-- <feature>/
|   |   |-- utils/                   # Utilitarios de aplicacao
|   |-- domain/                      # Nucleo do dominio
|       |-- entity/                  # Entidades (@JapeEntity) e objetos de dominio puro
|       |-- exception/               # Excecoes de dominio
|       |-- gateway/                 # Interfaces de integracao externa (Ports)
|       |-- repository/              # Interfaces de repositorio (@Repository)
|       |-- service/                 # Domain Services (logica complexa entre entidades)
|       |-- vo/                      # Value Objects, Enums, @Embeddable, DTOs internos
|-- entrypoint/                      # Driving Adapters (entrada)
|   |-- callback/                    # Callbacks do framework (ICustomCallBack)
|   |   |-- confirmation/
|   |-- listener/                    # Listeners de persistencia (PersistenceEventAdapter)
|   |-- rest/                        # Controllers REST (@Controller)
|       |-- <feature>/
|       |   |-- dto/                 # Request/Response DTOs
|       |   |-- mapper/             # MapStruct mappers REST
|       |-- RestExceptionHandler.java
|-- infrastructure/                  # Driven Adapters (saida)
    |-- gateway/                     # Dynamic Gateways (delegam para plataforma ativa)
    |-- http/                        # ResponseEntity, HttpStatus, serializers
    |-- integration/                 # Integracoes externas
    |   |-- shared/                  # Compartilhado entre plataformas
    |   |   |-- auth/
    |   |   |-- client/              # RetrofitClientFactory, RetrofitCallExecutor
    |   |   |-- mapper/              # Mappers compartilhados (StringNormalizer, etc)
    |   |-- <plataforma>/            # Implementacao especifica
    |       |-- client/              # Interfaces Retrofit
    |       |   |-- interceptors/
    |       |-- dto/                 # DTOs da API externa
    |       |-- mapper/              # MapStruct mappers da plataforma
    |       |-- <Plataforma>*.java   # Gateways concretos + Adapter
    |-- service/                     # Services de infraestrutura
    |-- ui/                          # Injecao de UI, popups, HTML utils
    |-- utils/                       # Utilitarios de infraestrutura
```

---

## 3. Camadas em Detalhe

### 3.1 Domain Layer ? O Nucleo

Camada mais interna. **Nao depende de nenhuma outra camada.** Contem:

#### Entidades (`entity/`)

Dois tipos coexistem:

| Tipo | Descricao | Anotacoes |
|:-----|:----------|:----------|
| **Entidade persistida** | Mapeia tabela do banco | `@JapeEntity`, `@Id`, `@Column` |
| **Objeto de dominio puro** | Conceito de negocio sem tabela | Apenas `@Data` |

**Entidades devem ser ricas (Rich Domain Model).** Metodos de negocio vivem na entidade:

```java
// Consulta de estado
public Boolean isVenda() {
    return this.tipoMovimento == TipoMovimento.PEDIDO_VENDA
        || this.tipoMovimento == TipoMovimento.VENDA;
}

// Mutacao de estado
public void cancelar(Timestamp data) {
    this.dhCancelamento = data;
    this.status = Status.CANCELADO;
}

// Delegacao para entidade relacionada
public Boolean deveProcessar() {
    return this.getTipoOperacao() != null
        | this.getTipoOperacao().deveProcessar()
        | false;
}
```

**Objetos de dominio puro** com factory method e validacao:

```java
@Data
public class MeuAgregado {

    private MeuCabecalho cabecalho;
    private List<MeuItem> itens;

    private MeuAgregado(MeuCabecalho cabecalho, List<MeuItem> itens) {
        this.cabecalho = cabecalho;
        this.itens = itens;
    }

    public static MeuAgregado create(MeuCabecalho cabecalho, List<MeuItem> itens) {
        if (itens == null || itens.isEmpty()) {
            throw new CreateIssueException("Deve conter ao menos um item.");
        }
        return new MeuAgregado(cabecalho, itens);
    }
}
```

#### Interfaces de Gateway (`gateway/`)

**Ports de saida** ? contratos que dominio espera. Implementacao fica na infraestrutura.

```java
public interface ProdutoGateway {
    List<MeuProduto> findAll();
}
```

> Dominio **nunca** sabe qual plataforma externa ativa. So conhece interface.

#### Interfaces de Repository (`repository/`)

```java
@Repository
public interface MeuProdutoRepository extends JapeRepository<Integer, MeuProduto> { }
```

#### Domain Service (`service/`)

Logica de negocio complexa que **cruza multiplas entidades** e nao pertence a nenhuma delas.

```java
@Component
public class MeuDomainService {

    private final MeuRepository repository;
    private final OutroRepository outroRepository;

    @Inject
    public MeuDomainService(MeuRepository repository, OutroRepository outroRepository) { ... }

    public MeuAgregado create(EntidadePai pai) {
        // Logica complexa que agrega dados de multiplas entidades
        // Validacoes de negocio cross-entity
        return MeuAgregado.create(cabecalho, itens);
    }
}
```

#### Excecoes (`exception/`)

Hierarquia tipada com semantica de negocio:

```
RuntimeException
|-- IntegrationException                # Base para erros de integracao
    |-- IntegrationApiException         # Erro de API (HTTP 4xx/5xx)
    |-- IntegrationNetworkException     # Falha de conexao
    |-- IntegrationImportException      # Erro ao importar dados
    |-- IntegrationExportException      # Erro ao exportar dados
    |-- IntegrationNoResultsException   # Sem resultados
DomainValidationException               # Validacao de dominio
EntityNotFoundException                 # Entidade nao encontrada
CreateIssueException                    # Erro ao criar agregado
CancelationIssueException              # Erro ao cancelar
```

> Excecoes de dominio **nunca** expoe detalhes de infra. Mensagens voltadas ao usuario de negocio.

#### Value Objects (`vo/`)

Enums, `@Embeddable` (PKs compostas) e DTOs internos do dominio:

```java
// Enum com valor persistido
@AllArgsConstructor @Getter
public enum StatusProcesso {
    PENDENTE("P"), EMITIDO("E"), CANCELADO("C");
    private final String value;
}

// PK composta
@Data @AllArgsConstructor @NoArgsConstructor @Embeddable
public class MeuItemId {
    @Column(name = "NUNOTA") private BigDecimal nuNota;
    @Column(name = "SEQUENCIA") private BigDecimal sequencia;
}

// DTO de dominio (retorno de operacao)
@Data @AllArgsConstructor
public class ResultadoOperacao {
    private String label;
    private byte[] file;
}
```

---

### 3.2 Application Layer ? Orquestracao

#### UseCase (`usecase/`)

**1 classe = 1 operacao de negocio.** UseCase orquestra fluxo chamando Domain Services, Repositories e Gateways.

```java
@Log
@Component
public class ImportarProdutoUseCase {

    private final DynamicProdutoGateway gateway;
    private final MeuProdutoRepository repository;

    @Inject
    public ImportarProdutoUseCase(DynamicProdutoGateway gateway,
                                  MeuProdutoRepository repository) {
        this.gateway = gateway;
        this.repository = repository;
    }

    public List<MeuProduto> execute() throws Exception {
        List<MeuProduto> items = gateway.findAll();

        List<MeuProduto> persisted = new ArrayList<>();
        for (MeuProduto item : items) {
            try {
                persisted.add(salvar(item));
            } catch (Exception e) {
                log.log(Level.SEVERE, "Erro ao sincronizar: " + e.getMessage(), e);
            }
        }
        return persisted;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    protected MeuProduto salvar(MeuProduto item) throws Exception {
        return repository.save(item);
    }
}
```

**Regras do UseCase:**
- Anotado com `@Component`.
- Metodo principal: `execute(...)`.
- **NAO** contem logica de negocio ? delega para entidades e domain services.
- Pode ter `@Transactional(REQUIRES_NEW)` em metodos auxiliares para batch (item a item).
- Organizado em subpacotes por feature.

#### Application Service (`service/`)

Quando interface precisa impl na infra (Port de saida para servicos de aplicacao):

```java
// Interface (application)
public interface MessageService {
    void showInfo(String message);
}

// Implementacao (infrastructure)
@Component
public class ContextMessageService implements MessageService {
    public void showInfo(String message) {
        ServiceContext.getCurrent().setStatusMessage(message);
    }
}
```

---

### 3.3 Entrypoint Layer ? Driving Adapters

Recebem estimulos externos e delegam para camada aplicacao. **Nunca contem logica de negocio.**

#### Controller REST (`rest/`)

```java
@Controller(
    serviceName = "MeuControllerSP",
    transactionType = EJBTransactionType.Supports
)
public class MeuController {

    private final MeuUseCase useCase;
    private final MeuRestMapper mapper;

    @Inject
    public MeuController(MeuUseCase useCase, MeuRestMapper mapper) { ... }

    @Transactional
    public ResponseEntity<MeuResponse> executar(@Valid MeuRequest request) {
        MeuDomain domain = mapper.toDomain(request);
        MeuDomain resultado = useCase.execute(domain);
        return ResponseEntity.ok(mapper.toResponse(resultado));
    }
}
```

**Regras:**
- `serviceName` com sufixo `"SP"`.
- Cada metodo publico = endpoint.
- Usa `@Transactional` em metodos que alteram dados.
- Converte Request ? Domain via MapStruct, delega para UseCase, converte Domain ? Response.
- DTOs (`dto/`) e mappers (`mapper/`) ficam em subpacote do controller.

#### Listener (`listener/`)

Reage a eventos de persistencia do framework (insert, update, delete em entidades).

```java
@Listener(instanceNames = "CabecalhoNota")
public class CabecalhoNotaListener extends PersistenceEventAdapter {

    private final MeuUseCase useCase;

    @Inject
    public CabecalhoNotaListener(MeuUseCase useCase) { ... }

    @Override
    public void afterUpdate(PersistenceEvent event) throws Exception {
        MeuEntidade entidade = EntityMapper.fromVO(event.getVo(), MeuEntidadeJapePojoEntity.class);

        if (!entidade.deveProcessar()) {
            return;  // Guard clause ? sai cedo se nao e relevante
        }
        useCase.execute(entidade);
    }
}
```

**Regras:**
- `@Listener(instanceNames = "NomeDaInstancia")` ? vincula ao dicionario de dados.
- Estende `PersistenceEventAdapter`.
- Override: `beforeInsert`, `afterInsert`, `beforeUpdate`, `afterUpdate`, `beforeDelete`, `afterDelete`.
- Converte `DynamicVO` para entidade via `EntityMapper.fromVO(...)`.
- Usa guard clauses para filtrar eventos irrelevantes.
- Delega logica para UseCases ? **nao** contem regras de negocio.

#### Callback (`callback/`)

Hook do framework para interceptar acoes do sistema (ex: confirmacao de nota).

```java
@Callback(
    when = CallbackWhen.BEFORE,
    event = CallbackEvent.PROCESS_CONFIRMATION,
    description = "Validacao antes da confirmacao"
)
public class MeuCallback implements ICustomCallBack {

    private final MeuDomainService service;
    private final MeuRepository repository;

    @Inject
    public MeuCallback(MeuDomainService service, MeuRepository repository) { ... }

    @Override
    public Object call(String id, Map<String, Object> data) {
        BigDecimal nuNota = (BigDecimal) data.get("nunota");
        MeuEntidade entidade = repository.findByPK(nuNota);

        if (!entidade.isVenda()) {
            return null;  // Nao se aplica
        }

        service.validar(entidade);
        return null;
    }
}
```

**Regras:**
- `@Callback(when, event, description)` ? define momento do hook.
- Implementa `ICustomCallBack`.
- Metodo `call(String id, Map<String, Object> data)` recebe dados do contexto.
- Guard clauses para filtrar quando nao se aplica.

---

### 3.4 Infrastructure Layer ? Driven Adapters

#### Dynamic Gateway Pattern

Projeto implementa **Strategy Pattern** para suportar multiplas plataformas de integracao. Camada aplicacao nao sabe qual plataforma ativa ? Dynamic Gateway resolve em runtime.

```
UseCase ? DynamicGateway ? Provider ? Resolver ? PlatformAdapter ? PlatformGateway
```

**1. Interface de dominio (Port):**

```java
// core/domain/gateway/
public interface ProdutoGateway {
    List<MeuProduto> findAll();
}
```

**2. Dynamic Gateway (Infrastructure ? delega para plataforma ativa):**

```java
// infrastructure/gateway/
@Component
public class DynamicProdutoGateway implements ProdutoGateway {

    private final IntegrationPlatformProvider provider;

    @Inject
    public DynamicProdutoGateway(IntegrationPlatformProvider provider) {
        this.provider = provider;
    }

    @Override
    public List<MeuProduto> findAll() {
        return provider.get().produto().findAll();
    }
}
```

**3. Provider (resolve plataforma da configuracao):**

```java
@Component
public class IntegrationPlatformProvider {

    private final IntegrationConfigurationService configService;
    private final IntegrationPlatformResolver resolver;

    @Inject
    public IntegrationPlatformProvider(IntegrationConfigurationService configService,
                                       IntegrationPlatformResolver resolver) { ... }

    public IntegrationPlatformAdapter get() {
        MeuConfiguracao config = configService.getActiveConfiguration();
        return resolver.resolve(config.getTipoPlataforma());
    }
}
```

**4. Resolver (Strategy Pattern via Multibinder):**

```java
@Singleton
public class IntegrationPlatformResolver {

    private final Map<TipoPlataforma, IntegrationPlatformAdapter> adapters;

    @Inject
    public IntegrationPlatformResolver(Set<IntegrationPlatformAdapter> adapters) {
        this.adapters = adapters.stream()
            .collect(Collectors.toMap(IntegrationPlatformAdapter::getTipo, Function.identity()));
    }

    public IntegrationPlatformAdapter resolve(TipoPlataforma tipo) {
        IntegrationPlatformAdapter adapter = adapters.get(tipo);
        if (adapter == null) {
            throw new IllegalArgumentException("Plataforma nao implementada: " + tipo);
        }
        return adapter;
    }
}
```

**5. Platform Adapter (Facade para gateways da plataforma):**

```java
public interface IntegrationPlatformAdapter {
    TipoPlataforma getTipo();
    ProdutoGateway produto();
    CulturaGateway cultura();
    AlvoGateway alvo();
    // ... demais gateways
}
```

**6. Implementacao concreta:**

```java
public class MeuPlatformAdapter implements IntegrationPlatformAdapter {

    private final MeuPlatformProdutoGateway produtoGateway;
    // ... demais gateways

    @Inject
    public MeuPlatformAdapter(MeuPlatformProdutoGateway produtoGateway, ...) { ... }

    @Override
    public TipoPlataforma getTipo() { return TipoPlataforma.MINHA_PLATAFORMA; }

    @Override
    public ProdutoGateway produto() { return produtoGateway; }
}
```

**7. Registro via Multibinder (`@CustomModule`):**

```java
@CustomModule
public class IntegrationPlatformConfig extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<IntegrationPlatformAdapter> binder =
            Multibinder.newSetBinder(binder(), IntegrationPlatformAdapter.class);
        binder.addBinding().to(MeuPlatformAdapter.class);
    }
}
```

> Adicionar nova plataforma: criar gateways, Adapter, registrar no Multibinder. Zero alteracao em dominio/aplicacao.

#### Gateway Concreto da Plataforma

Cada gateway concreto faz chamada HTTP e converte DTO externo ? dominio via MapStruct.

```java
public class MeuPlatformProdutoGateway implements ProdutoGateway {

    private final MeuApiClient client;
    private final RetrofitCallExecutor executor;
    private final MeuPlatformProdutoMapper mapper;

    @Inject
    public MeuPlatformProdutoGateway(MeuApiClient client,
                                      RetrofitCallExecutor executor,
                                      MeuPlatformProdutoMapper mapper) { ... }

    @Override
    public List<MeuProduto> findAll() {
        try {
            MeuApiResponse root = executor.execute(client.buscarProdutos());
            return root.getProdutos().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
        } catch (IntegrationApiException e) {
            throw new IntegrationImportException("Erro ao importar: " + e.getMessage(), e);
        } catch (IntegrationNetworkException e) {
            throw new IntegrationImportException("Falha de conexao: " + e.getMessage(), e);
        }
    }
}
```

---

## 4. Design Patterns Aplicados

| Pattern | Onde | Implementacao |
|:--------|:-----|:--------------|
| **Strategy** | Multiplas plataformas de integracao | `IntegrationPlatformAdapter` + `Multibinder` + `Resolver` |
| **Facade** | Adapter da plataforma | `IntegrationPlatformAdapter` agrupa todos os gateways |
| **Provider** | Resolucao lazy da plataforma ativa | `IntegrationPlatformProvider.get()` |
| **Factory Method** | Criacao de agregados de dominio | `MeuAgregado.create(...)` com validacao |
| **Repository** | Acesso a dados via interface | `JapeRepository<PK, Entity>` |
| **Port/Adapter (Hexagonal)** | Interfaces de Gateway/Repository no dominio, implementacao na infra | `ProdutoGateway` (port) ? `DynamicProdutoGateway` (adapter) |
| **UseCase (Clean Arch)** | 1 classe = 1 operacao | `ImportarProdutoUseCase.execute()` |
| **Observer** | Listeners de persistencia | `@Listener` + `PersistenceEventAdapter` |
| **Template Method** | Callbacks do framework | `ICustomCallBack.call()` |
| **Rich Domain Model (DDD)** | Logica de negocio nas entidades | `CabecalhoNota.isVenda()`, `.cancelar()` |
| **Value Object (DDD)** | Enums com semantica de dominio | `StatusProcesso`, `TipoMovimento` |
| **Guard Clause** | Filtro em listeners/callbacks | `if (!entidade.deveProcessar()) return;` |

---

## 5. Regra de Dependencias

```
    ------------------------
    |  config/             -> conhece: tudo (wiring)
    ------------------------
    ------------------------
    |  entrypoint/         -> conhece: application, domain
    ------------------------
    ------------------------
    |  infrastructure/     -> conhece: domain (implementa interfaces)
    ------------------------
    ------------------------
    |  core/application/   -> conhece: domain, infrastructure (via interfaces)
    ------------------------
    ------------------------
    |  core/domain/        -> conhece: NADA externo (nucleo puro)
    ------------------------
```

**Regras estritas:**
- `domain` **NUNCA** importa classes de `infrastructure`, `entrypoint` ou `config`.
- `domain` contem **interfaces** (ports) -> impls ficam em `infrastructure`.
- `entrypoint` **NUNCA** acessa repositorios ou gateways direto ? sempre via UseCases.
- `application` pode usar interfaces de `infrastructure` (injetadas pelo Guice).

> **Excecao pragmatica:** UseCases podem referenciar `DynamicGateway` (infra) direto, ja que Dynamic Gateway e o adapter que implementa interface de dominio. Ideal seria injetar interface `ProdutoGateway`, mas como Guice resolve `DynamicProdutoGateway` como `@Component`, ambas abordagens funcionam.

---

## 6. Fluxo de uma Requisicao (Exemplo)

```
[Frontend] -> Controller.executar(Request)
               |
               |-- Mapper.toDomain(Request) -> Domain Object
               |
               |-- UseCase.execute(Domain Object)
               |      |
               |      |-- DomainService.criar(...)
               |      |      |-- Entity.validar() / Agregado.create(...)
               |      |
               |      |-- DynamicGateway.operacao(...)
               |      |      |-- Provider -> Resolver -> PlatformGateway
               |      |            |-- RetrofitClient -> API Externa
               |      |            |-- Mapper.toDomain(ExternalDTO) -> Domain
               |      |
               |      |-- Repository.save(entity)
               |      |
               |      |-- return resultado
               |
               |-- Mapper.toResponse(resultado) ? Response DTO
               |
               |-- return ResponseEntity.ok(Response)
```

---

## 7. Convencoes de Nomenclatura

| Tipo | Padrao | Exemplo |
|:-----|:-------|:--------|
| UseCase | `<Verbo><Entidade>UseCase` | `ImportarProdutoUseCase`, `EmitirReceituarioUseCase` |
| Domain Service | `<Entidade><Acao>Service` | `ReceituarioCreateService` |
| Application Service (interface) | `<Conceito>Service` | `MessageService` |
| Controller | `<Feature>Controller` | `ProdutoController` |
| Listener | `<Entidade>Listener` | `CabecalhoNotaListener` |
| Callback | `Before/After<Acao>Callback` | `BeforeCentralConfirmationCallback` |
| Gateway (interface) | `<Entidade>Gateway` | `ProdutoGateway` |
| Dynamic Gateway | `Dynamic<Entidade>Gateway` | `DynamicProdutoGateway` |
| Platform Gateway | `<Plataforma><Entidade>Gateway` | `WebReceitaProdutoGateway` |
| Platform Adapter | `<Plataforma>Adapter` | `WebReceitaAdapter` |
| Repository | `<Entidade>Repository` | `RecProdutoRepository` |
| Mapper REST | `<Feature>RestMapper` | `ReceituarioRestMapper` |
| Mapper Integration | `<Plataforma><Entidade>Mapper` | `WebReceitaProdutoMapper` |
| Request DTO | `<Acao>Request` | `EmitirReceituarioRequest` |
| Response DTO | `<Acao>Response` | `EmitirReceituarioResponse` |
| Exception | `<Semantica>Exception` | `CreateIssueException`, `IntegrationImportException` |
| Enum | `<Conceito>` | `StatusReceituario`, `TipoMovimento` |
| PK Composta | `<Entidade>Id` | `ItemNotaId`, `RecRelacaoProdutoId` |

---

## 8. Checklist: Nova Feature

### Somente CRUD (sem integracao)

1. [ ] Criar XML do dicionario de dados (`datadictionary/`)
2. [ ] Criar script de banco (`dbscripts/`)
3. [ ] Criar Entidade (`@JapeEntity`) em `domain/entity/`
4. [ ] Criar Repository (`@Repository`) em `domain/repository/`
5. [ ] Criar UseCase (`@Component`) em `application/usecase/<feature>/`
6. [ ] Criar Controller (`@Controller`) em `entrypoint/rest/<feature>/`
7. [ ] Criar Request/Response DTOs em `entrypoint/rest/<feature>/dto/`
8. [ ] Criar MapStruct Mapper em `entrypoint/rest/<feature>/mapper/`

### Feature com integracao externa

1. [ ] Todos os passos do CRUD acima
2. [ ] Criar interface Gateway em `domain/gateway/`
3. [ ] Criar Dynamic Gateway em `infrastructure/gateway/`
4. [ ] Criar DTOs da API externa em `infrastructure/integration/<plataforma>/dto/`
5. [ ] Criar MapStruct Mapper em `infrastructure/integration/<plataforma>/mapper/`
6. [ ] Criar Gateway concreto em `infrastructure/integration/<plataforma>/`
7. [ ] Adicionar metodo na interface `IntegrationPlatformAdapter`
8. [ ] Implementar no Adapter da plataforma

### Nova plataforma de integracao

1. [ ] Criar pacote `infrastructure/integration/<novaplataforma>/`
2. [ ] Criar subpacotes: `client/`, `dto/`, `mapper/`
3. [ ] Criar interfaces Retrofit (`client/`)
4. [ ] Criar DTOs da API (`dto/`)
5. [ ] Criar MapStruct Mappers (`mapper/`)
6. [ ] Criar Gateways concretos (implementam interfaces de `domain/gateway/`)
7. [ ] Criar Adapter que implementa `IntegrationPlatformAdapter`
8. [ ] Criar `@CustomModule` para `@Provides` dos clientes Retrofit
9. [ ] Registrar Adapter no `Multibinder` do `IntegrationPlatformConfig`
10. [ ] Adicionar valor no Enum `TipoPlataforma`

---

## 9. Padrão de Polling com Fila (especifico deste projeto)

Projeto implementa tres pipelines assincronos via fila + worker + listener. Cada pipeline segue mesmo contrato hexagonal:

```
Job (entrypoint)
  └─ ConsultarXxxUseCase.execute(PlataformaIntegracaoEnum)   ← agnostico de plataforma
       └─ PollarXxxPort (interface em core/application/port/)
            └─ PlataformaPollarXxxAdapter (impl em infrastructure/integration/<plataforma>/)
                 ├─ <Plataforma>Gateway → chamada SOAP/HTTP
                 └─ LocalRepositoryService.salvarXxx() → persiste XML no disco
       └─ FilaXxxRepository.save() com dedup
            └─ FilaXxxListener (afterInsert)
                 └─ FilaXxxWorkerPool → ProcessarFilaXxxUseCase
                      └─ ProcessarXxxPort (interface em core/application/port/)
                           └─ PlataformaProcessarXxxAdapter (impl em infrastructure/...)
```

### Ports do pipeline

| Port (core/application/port/) | Retorno | Named Guice |
|:------------------------------|:--------|:------------|
| `PollarCotacoesAbertasPort` | `List<FilaCotacao>` parcial | `"polladoresAberturas"` |
| `PollarConfirmacoesPort` | `List<FilaConfirmacaoCotacao>` parcial | `"polladoresConfirmacoes"` |
| `ImportarCotacaoPort` | void | `"importadores"` |
| `ProcessarConfirmacaoCotacaoPort` | void | `"processadoresConfirmacao"` |

**Contrato do adapter de polling:** preencher apenas `idExterno`, `codPlataforma` e `pathArquivo` ao construir entidade de fila. Estado inicial completo (`status=PENDENTE`, `tentativas=0`, `dhInclusao`) é responsabilidade do factory method estatico da entidade (ex: `FilaCotacao.criar(...)`). UseCase nao deve redefinir esses campos — entidade nao pode nascer em estado invalido.

### Para adicionar nova plataforma ao pipeline

1. Criar `<Plataforma>PollarCotacoesAbertasAdapter implements PollarCotacoesAbertasPort`
2. Criar `<Plataforma>PollarConfirmacoesAdapter implements PollarConfirmacoesPort`
3. Criar `<Plataforma>ImportarCotacaoAdapter implements ImportarCotacaoPort`
4. Criar `<Plataforma>ProcessarConfirmacaoAdapter implements ProcessarConfirmacaoCotacaoPort`
5. Registrar os quatro adapters nos mapas `@Named` do modulo Guice da plataforma
6. Criar Job de entrypoint passando `PlataformaIntegracaoEnum.<PLATAFORMA>`
7. **Zero alteracao** em use cases, listeners, workers ou dominio.

---

## 10. Anti-Patterns (PROIBIDO)

| Anti-Pattern | Correcao |
|:-------------|:---------|
| Logica de negocio no Controller | Mover para UseCase ou Domain Service |
| Logica de negocio no Listener | Mover para UseCase ? listener so filtra e delega |
| UseCase chamando `RetrofitClient` direto | Usar Gateway (interface no dominio, impl na infra) |
| UseCase importando classes de `infrastructure` (Gateway, DTO, LocalRepositoryService) | Extrair para Port (interface) e mover toda logica para Adapter em infra |
| UseCase de polling acoplado a plataforma (`ConsultarXxxBionexoUseCase`) | Use case deve ser agnostico; injetar `Map<PlataformaIntegracaoEnum, Port>` via `@Named` |
| Controller acessando Repository direto | Usar UseCase como intermediario |
| Entidade anemica (so getters/setters) | Adicionar metodos de negocio na entidade |
| Excecao generica (`throw new RuntimeException(...)`) | Criar excecao tipada em `domain/exception/` |
| DTO do Retrofit vazando para dominio | Converter via MapStruct no Gateway concreto |
| Import de classes de `infrastructure` em `domain` | Domain so conhece interfaces (ports) |
| `new` para criar dependencias | Usar `@Inject` via construtor |
| Duplicar logica entre plataformas | Extrair para `shared/` ou para dominio |