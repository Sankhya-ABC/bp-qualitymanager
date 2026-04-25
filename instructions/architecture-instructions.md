---
applyTo: "**/*.java"
---

# Arquitetura e Design Patterns - Addon Studio 2.0

Documento define arquitetura de referencia em projetos Addon Studio 2.0. Objetivo: guiar IA e devs na criacao de codigo que respeite responsabilidades, organizacao por feature e limitacoes do framework.

> **Referencia complementar:**
> - `controller-instructions.md` - Controller REST, DTOs, protocolo HTTP, @ControllerAdvice
> - `dependency-injection-instructions.md` - DI com Guice
> - `entity-instructions.md` - Entidades Java
> - `repository-instructions.md` - Repositorios JapeRepository
> - `mapstruct-instructions.md` - Mapeamento de objetos

---

## 1. Visao Geral: MVC + Package by Feature

Projeto segue **Model-View-Controller** com organizacao **package by feature** dentro das limitacoes do framework Sankhya (Wildfly/EJB). Cada feature concentra todos os artefatos relacionados em um unico pacote.

```
-------------------------------------------------------------------
|  CONTROLLER                                                     |
|  Controller REST, Listener, Callback                            |
|    Recebem estimulos externos -> delegam para Service           |
-------------------------------------------------------------------
|  SERVICE (Model - regra de negocio)                             |
|  Service                                                        |
|    Orquestra fluxo, aplica regras, chama Repository             |
-------------------------------------------------------------------
|  REPOSITORY (Model - acesso a dados)                            |
|  Repository (JapeRepository)                                    |
|    Acesso ao banco via SDK Sankhya                              |
-------------------------------------------------------------------
|  ENTITY (Model - dados)                                         |
|  Entity (@JapeEntity), VO, Enum, PK Composta                    |
|    Mapeamento das tabelas e tipos de dominio                    |
-------------------------------------------------------------------
|  VIEW (contrato externo)                                        |
|  Request DTO, Response DTO, Mapper                              |
|    Contrato HTTP - converte entre DTO e Entity                  |
-------------------------------------------------------------------
|  CONFIG                                                         |
|  CustomModule (Guice bindings)                                  |
|    Wiring de dependencias quando auto-scan nao basta            |
-------------------------------------------------------------------
```

---

## 2. Estrutura de Pacotes (Package by Feature)

Pacote raiz: `br.com.hagious.qualitymanager`

Cada feature concentra todos os artefatos:

```
br/com/hagious/qualitymanager/
|-- config/                          # Modulos Guice globais (@CustomModule)
|-- shared/                          # Util e tipos compartilhados entre features
|   |-- exception/                   # Excecoes globais
|   |-- mapper/                      # Mappers auxiliares (@Component)
|-- <feature>/                       # 1 pacote por feature de negocio
|   |-- controller/                  # Controllers REST (@Controller)
|   |-- service/                     # Services com regra de negocio (@Component)
|   |-- repository/                  # Repositorios (@Repository, JapeRepository)
|   |-- entity/                      # Entidades (@JapeEntity)
|   |-- vo/                          # Value Objects, Enums, PKs compostas (@Embeddable)
|   |-- dto/                         # Request/Response DTOs
|   |-- mapper/                      # Mappers MapStruct da feature
|   |-- listener/                    # Listeners opcionais (PersistenceEventAdapter)
|   |-- callback/                    # Callbacks opcionais (ICustomCallBack)
|   |-- exception/                   # Excecoes da feature (opcional)
```

> **Regra de ouro:** se o artefato pertence a uma feature, fica dentro do pacote daquela feature. Compartilhamento entre features so via `shared/`.

---

## 3. Camadas em Detalhe

### 3.1 Entity (Model - Dados)

Mapeia tabela ou conceito de dominio. Detalhes em `entity-instructions.md`.

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@JapeEntity(entity = "TdcRegistro", table = "TDCQMNCREGISTRO")
public class Registro {

    @Id
    @Column(name = "CODREGISTRO")
    private Integer codRegistro;

    @Column(name = "DESCR")
    private String descricao;

    @Column(name = "ATIVO")
    private Boolean ativo;
}
```

Pode ter metodos de negocio simples relacionados ao proprio estado:

```java
public Boolean isAtivo() {
    return Boolean.TRUE.equals(this.ativo);
}

public void inativar() {
    this.ativo = Boolean.FALSE;
}
```

### 3.2 Repository (Model - Acesso a Dados)

Interface que estende `JapeRepository<ID, Entity>`. Detalhes em `repository-instructions.md`.

```java
@Repository
public interface RegistroRepository extends JapeRepository<Integer, Registro> {

    @Criteria(clause = "this.ATIVO = :ativo")
    List<Registro> findByAtivo(Boolean ativo);
}
```

### 3.3 Service (Model - Regra de Negocio)

`@Component` que concentra a regra de negocio da feature. Orquestra Repository, valida regras, lanca excecoes de dominio.

```java
@Log
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

    public Registro criar(Registro registro) throws Exception {
        if (registro.getDescricao() == null || registro.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("Descricao obrigatoria.");
        }
        return repository.save(registro);
    }
}
```

**Regras do Service:**
- Anotado com `@Component`.
- Injeta `Repository` via construtor.
- Concentra a regra de negocio - **nao** delega tudo para outras camadas.
- Pode chamar outros Services da mesma feature ou de `shared/`.
- Metodos com nome de acao de negocio (`criar`, `inativar`, `listarAtivos`).

### 3.4 Controller (View - Entrypoint REST)

Recebe requisicao, delega para Service, retorna resposta. **Nunca** contem regra de negocio. Detalhes em `controller-instructions.md`.

```java
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
    public RegistroResponse criar(@Valid CriarRegistroRequest request) throws Exception {
        Registro entity = mapper.toEntity(request);
        Registro salvo = service.criar(entity);
        return mapper.toResponse(salvo);
    }
}
```

### 3.5 DTO (View - Contrato Externo)

Request/Response com validacao e Lombok. **Nunca** expor entidades direto.

```java
@Data
public class CriarRegistroRequest {

    @NotBlank(message = "Descricao obrigatoria.")
    private String descricao;
}

@Data
public class RegistroResponse {

    private Integer codRegistro;
    private String descricao;
    private Boolean ativo;
}
```

### 3.6 Mapper (View - Conversao)

MapStruct `interface` ou `abstract class` por feature. Detalhes em `mapstruct-instructions.md`.

```java
@Mapper
public interface RegistroMapper {

    Registro toEntity(CriarRegistroRequest dto);

    RegistroResponse toResponse(Registro entity);
}
```

### 3.7 Listener (opcional)

Reage a eventos de persistencia do framework.

```java
@Listener(instanceNames = "TdcRegistro")
public class RegistroListener extends PersistenceEventAdapter {

    private final RegistroService service;

    @Inject
    public RegistroListener(RegistroService service) {
        this.service = service;
    }

    @Override
    public void afterUpdate(PersistenceEvent event) throws Exception {
        Registro entity = EntityMapper.fromVO(event.getVo(), Registro.class);
        service.processarAlteracao(entity);
    }
}
```

### 3.8 Callback (opcional)

Hook do framework para interceptar acoes do sistema.

```java
@Callback(when = CallbackWhen.BEFORE, event = CallbackEvent.PROCESS_CONFIRMATION)
public class RegistroCallback implements ICustomCallBack {

    private final RegistroService service;

    @Inject
    public RegistroCallback(RegistroService service) {
        this.service = service;
    }

    @Override
    public Object call(String id, Map<String, Object> data) {
        BigDecimal nuNota = (BigDecimal) data.get("nunota");
        service.validarConfirmacao(nuNota);
        return null;
    }
}
```

---

## 4. Design Patterns Aplicados

| Pattern | Onde | Implementacao |
|:--------|:-----|:--------------|
| **MVC** | Toda feature | Controller (entrada) -> Service (regra) -> Repository (dados) |
| **Package by Feature** | Estrutura raiz | Tudo de uma feature em `<feature>/...` |
| **Repository** | Acesso a dados via interface | `JapeRepository<PK, Entity>` |
| **DTO** | Contrato HTTP | Request/Response separados da Entity |
| **Mapper** | Conversao DTO <-> Entity | MapStruct |
| **Service Layer** | Regra de negocio | `@Component` por feature |
| **Observer** | Listeners de persistencia | `@Listener` + `PersistenceEventAdapter` |
| **Template Method** | Callbacks do framework | `ICustomCallBack.call()` |
| **Guard Clause** | Filtro em listeners/callbacks | `if (!entity.deveProcessar()) return;` |

---

## 5. Regra de Dependencias

```
Controller -> Service -> Repository -> Entity
Controller -> Mapper  -> DTO + Entity
Listener   -> Service
Callback   -> Service
```

**Regras:**
- Controller **nao** acessa Repository direto - sempre via Service.
- Service **nao** depende de Controller, DTO ou Mapper.
- Entity **nao** depende de Service, Controller ou DTO.
- Mapper conhece DTO e Entity, nada mais.
- DTO nao tem logica - so dados + validacao.

---

## 6. Fluxo de uma Requisicao (Exemplo)

```
[Frontend] -> Controller.criar(Request)
               |
               |-- Mapper.toEntity(Request) -> Entity
               |
               |-- Service.criar(Entity)
               |      |
               |      |-- valida regras de negocio
               |      |-- Repository.save(entity)
               |      |-- return entity persistida
               |
               |-- Mapper.toResponse(entity) -> Response DTO
               |
               |-- return Response
```

---

## 7. Convencoes de Nomenclatura

| Tipo | Padrao | Exemplo |
|:-----|:-------|:--------|
| Feature (pacote) | substantivo no singular | `registro`, `fornecedor`, `rnc` |
| Entity | `<Nome>` | `Registro`, `Fornecedor` |
| Repository | `<Entity>Repository` | `RegistroRepository` |
| Service | `<Entity>Service` ou `<Feature>Service` | `RegistroService` |
| Controller | `<Feature>Controller` | `RegistroController` |
| serviceName Controller | `<Feature>ControllerSP` | `RegistroControllerSP` |
| Listener | `<Entity>Listener` | `RegistroListener` |
| Callback | `<Quando><Acao>Callback` | `BeforeConfirmacaoCallback` |
| Mapper | `<Feature>Mapper` | `RegistroMapper` |
| Request DTO | `<Acao><Feature>Request` ou `<Acao>Request` | `CriarRegistroRequest` |
| Response DTO | `<Feature>Response` ou `<Acao>Response` | `RegistroResponse` |
| Exception | `<Semantica>Exception` | `RegistroNotFoundException` |
| Enum | `<Conceito>Enum` | `StatusEnum` |
| PK Composta | `<Entity>Id` | `RegistroItemId` |

---

## 8. Checklist: Nova Feature

1. [ ] Criar XML do dicionario de dados (`datadictionary/`)
2. [ ] Criar script de banco (`dbscripts/`)
3. [ ] Criar pacote da feature em `br/com/hagious/qualitymanager/<feature>/`
4. [ ] Criar Entity (`@JapeEntity`) em `<feature>/entity/`
5. [ ] Criar Repository (`@Repository`) em `<feature>/repository/`
6. [ ] Criar Service (`@Component`) em `<feature>/service/`
7. [ ] Criar Controller (`@Controller`) em `<feature>/controller/`
8. [ ] Criar Request/Response DTOs em `<feature>/dto/`
9. [ ] Criar Mapper MapStruct em `<feature>/mapper/`
10. [ ] (Opcional) Criar Listener em `<feature>/listener/`
11. [ ] (Opcional) Criar Callback em `<feature>/callback/`
12. [ ] (Opcional) Criar Exception em `<feature>/exception/`
13. [ ] Escrever testes em `src/test/java/.../<feature>/service/`

---

## 9. Anti-Patterns (PROIBIDO)

| Anti-Pattern | Correcao |
|:-------------|:---------|
| Regra de negocio no Controller | Mover para Service |
| Regra de negocio no Listener | Mover para Service - listener so filtra e delega |
| Controller acessando Repository direto | Sempre via Service |
| Service acessando DTO ou Mapper | Service so trabalha com Entity |
| Entity com anotacao Jackson/Gson | Conversao via Mapper, nao serializacao direta |
| Entity anemica (so getters/setters quando ha logica natural) | Adicionar metodos de dominio simples na Entity |
| Excecao generica (`throw new RuntimeException(...)`) | Criar excecao tipada (em `<feature>/exception/` ou `shared/exception/`) |
| Mapper manual (`new DTO(); dto.setX(...)`) | Usar MapStruct |
| `new` para criar dependencias | Usar `@Inject` via construtor |
| Imports cruzados entre features sem passar por `shared/` | Extrair codigo comum para `shared/` |
| Subpacote diferente do padrao MVC dentro da feature | Seguir `controller/`, `service/`, `repository/`, `entity/`, `vo/`, `dto/`, `mapper/` |
