# new-platform-checklist.md

## Propósito

Arquivo define protocolo completo e sequencial para implementar nova plataforma de integração.

**Leia arquivo inteiro antes de criar qualquer arquivo de nova plataforma.**

---

## Regra de Ouro

> Ordem importa. Nunca crie implementação concreta antes de existir interface correspondente no domínio.

```
Interface no domínio PRIMEIRO → Implementação na infra DEPOIS
```

Criou Gateway concreto em `infrastructure/` sem interface em `domain/gateway/`? Fluxo invertido. Pare e corrija.

---

## Pré-requisito: confirmar contratos existentes

Antes de criar qualquer arquivo, responda:

1. Quais `Ports` já existem em `core/application/port/`?
   - `PollarCotacoesAbertasPort`
   - `PollarConfirmacoesPort`
   - `ImportarCotacaoPort`
   - `ProcessarConfirmacaoCotacaoPort`

2. Quais interfaces de Gateway já existem em `core/domain/gateway/`?

3. Enum `PlataformaIntegracaoEnum` já tem valor para esta plataforma?

4. Interface `IntegrationPlatformAdapter` já tem métodos necessários?

**Alguma resposta não clara: pergunte ao dev antes de continuar.**

---

## Sequência obrigatória de criação de artefatos

### Etapa 1 — Contratos de domínio (se ainda não existirem)

Arquivos em `core/domain/gateway/` — **somente interfaces**:

```java
// core/domain/gateway/<Entidade>Gateway.java
public interface <Entidade>Gateway {
    // métodos com tipos exclusivamente do domínio
    // NUNCA tipos de plataforma externa aqui
}
```

- [ ] Criar/verificar todas interfaces de Gateway necessárias para plataforma
- [ ] Verificar nenhuma assinatura de método usa tipos de `infrastructure/`
- [ ] Adicionar valor da plataforma em `PlataformaIntegracaoEnum` (em `core/domain/vo/`)

---

### Etapa 2 — Estrutura de pacotes da plataforma

Criar hierarquia de diretórios em `infrastructure/integration/<plataforma>/`:

```
infrastructure/integration/<plataforma>/
├── client/                    # Interfaces Retrofit
│   └── interceptors/          # Interceptors de autenticação/log
├── dto/                       # DTOs da API externa (NUNCA saem deste pacote para o domínio)
├── mapper/                    # MapStruct mappers (DTO externo → Domain)
└── <Plataforma>Adapter.java   # Implementação de IntegrationPlatformAdapter
```

- [ ] Estrutura de pacotes criada

---

### Etapa 3 — Cliente HTTP (Retrofit)

Arquivo em `infrastructure/integration/<plataforma>/client/`:

```java
// <Plataforma>ApiClient.java
public interface <Plataforma>ApiClient {
    @GET("endpoint")
    Call<List<<Plataforma>XxxDto>> buscarXxx(@Query("param") String param);
}
```

Regras:
- Usar **apenas** tipos de `infrastructure/integration/<plataforma>/dto/` nas assinaturas
- Nenhum tipo de domínio nas assinaturas do cliente Retrofit
- Usar `Call<T>` do Retrofit (executado via `RetrofitCallExecutor`)

- [ ] Interface Retrofit criada com todos endpoints necessários
- [ ] Interceptors de autenticação criados em `client/interceptors/` se necessário

---

### Etapa 4 — DTOs da API externa

Arquivos em `infrastructure/integration/<plataforma>/dto/`:

```java
// <Plataforma>XxxDto.java
@Data
@NoArgsConstructor
public class <Plataforma>XxxDto {
    // campos que espelham a resposta da API externa
    // anotações de serialização (Moshi/Gson) ficam aqui
}
```

Regras:
- DTOs **ficam neste pacote**. Nunca importados fora de `infrastructure/integration/<plataforma>/`
- Nenhum método de negócio aqui — estruturas dumb
- Nenhum tipo de domínio referenciado aqui

- [ ] DTOs de request/response criados para todos endpoints
- [ ] Nenhum tipo de domínio referenciado nos DTOs

---

### Etapa 5 — Mappers (DTO externo → Domain)

Arquivos em `infrastructure/integration/<plataforma>/mapper/`:

```java
// <Plataforma><Entidade>Mapper.java
@Mapper
public interface <Plataforma><Entidade>Mapper {
    <EntidadeDominio> toDomain(<Plataforma>XxxDto dto);
    List<<EntidadeDominio>> toDomainList(List<<Plataforma>XxxDto> dtos);
}
```

Regras:
- Mapper converte **de** DTO externo **para** tipo de domínio
- **Nunca** contrário entrando no domínio (tipo de infra não contamina domínio)
- Campos com semântica diferente (ex: string da API → Enum do domínio): criar método explícito com `@Mapping`
- Campos de ciclo de vida (`status`, `tentativas`, `dhInclusao`) **não mapeados aqui** — invariantes inicializados pelo factory method da entidade

- [ ] Mappers criados para todas entidades necessárias
- [ ] Mapeamentos de Enum/tipo confirmados e explícitos
- [ ] Campos de ciclo de vida excluídos do mapper

---

### Etapa 6 — Gateways concretos da plataforma

Arquivos em `infrastructure/integration/<plataforma>/`:

```java
// <Plataforma><Entidade>Gateway.java
public class <Plataforma><Entidade>Gateway implements <Entidade>Gateway {

    private final <Plataforma>ApiClient client;
    private final RetrofitCallExecutor executor;
    private final <Plataforma><Entidade>Mapper mapper;

    @Inject
    public <Plataforma><Entidade>Gateway(<Plataforma>ApiClient client,
                                          RetrofitCallExecutor executor,
                                          <Plataforma><Entidade>Mapper mapper) {
        this.client = client;
        this.executor = executor;
        this.mapper = mapper;
    }

    @Override
    public List<<EntidadeDominio>> findAll() {
        try {
            <Plataforma>XxxDto root = executor.execute(client.buscarXxx());
            return mapper.toDomainList(root.getItens());
        } catch (IntegrationApiException e) {
            throw new IntegrationImportException("Erro ao importar: " + e.getMessage(), e);
        } catch (IntegrationNetworkException e) {
            throw new IntegrationImportException("Falha de conexao: " + e.getMessage(), e);
        }
    }
}
```

Regras:
- **Implementa interface de `domain/gateway/`** — verificar interface existe antes de criar
- Retorna **apenas tipos de domínio** — conversão ocorre aqui, nunca no UseCase
- Trata exceções HTTP/rede e relança como `IntegrationImportException` ou equivalente tipado
- Sem lógica de negócio — apenas: chamar API → converter → retornar

- [ ] Cada Gateway concreto implementa interface de `domain/gateway/` correspondente
- [ ] Nenhum tipo de DTO externo retornado em métodos públicos
- [ ] Exceções tratadas e relançadas com tipo semântico correto

---

### Etapa 7 — Adapters de pipeline (Polling)

Para cada Port em `core/application/port/`, criar adapter em `infrastructure/integration/<plataforma>/`:

```java
// <Plataforma>PollarCotacoesAbertasAdapter.java
public class <Plataforma>PollarCotacoesAbertasAdapter implements PollarCotacoesAbertasPort {

    // dependências da plataforma

    @Override
    public List<FilaCotacao> pollar(PlataformaIntegracaoEnum plataforma) {
        // 1. Chamar API da plataforma
        // 2. Persistir arquivo localmente via LocalRepositoryService (se aplicável)
        // 3. Retornar lista de FilaCotacao usando o factory method da entidade:
        //    FilaCotacao.criar(idExterno, codPlataforma, pathArquivo)
        //    O factory method já encapsula o estado inicial completo.
        //
        // Usar o factory method da entidade (ex: FilaCotacao.criar(...)) que já
        // encapsula o estado inicial. NÃO usar new + setters. NÃO redefinir
        // status, tentativas ou dhInclusao no UseCase — são invariantes da entidade.
    }
}
```

- [ ] `<Plataforma>PollarCotacoesAbertasAdapter implements PollarCotacoesAbertasPort`
- [ ] `<Plataforma>PollarConfirmacoesAdapter implements PollarConfirmacoesPort`
- [ ] `<Plataforma>ImportarCotacaoAdapter implements ImportarCotacaoPort`
- [ ] `<Plataforma>ProcessarConfirmacaoAdapter implements ProcessarConfirmacaoCotacaoPort`
- [ ] Adapters usam factory method da entidade — nunca `new` + setters manuais

---

### Etapa 8 — Platform Adapter (Facade)

```java
// <Plataforma>Adapter.java
public class <Plataforma>Adapter implements IntegrationPlatformAdapter {

    private final <Plataforma><Entidade1>Gateway entidade1Gateway;
    // ... demais gateways

    @Inject
    public <Plataforma>Adapter(<Plataforma><Entidade1>Gateway entidade1Gateway, ...) {
        this.entidade1Gateway = entidade1Gateway;
    }

    @Override
    public PlataformaIntegracaoEnum getTipo() {
        return PlataformaIntegracaoEnum.<PLATAFORMA>;
    }

    @Override
    public <Entidade1>Gateway entidade1() {
        return entidade1Gateway;
    }
}
```

- [ ] Adapter implementa `IntegrationPlatformAdapter`
- [ ] Todos Gateways concretos da plataforma expostos via métodos do Adapter
- [ ] `getTipo()` retorna valor correto do Enum

---

### Etapa 9 — Módulo Guice

```java
// config/integration/<Plataforma>Module.java
@CustomModule
public class <Plataforma>Module extends AbstractModule {

    @Override
    protected void configure() {
        // Registrar o Adapter no Multibinder
        Multibinder<IntegrationPlatformAdapter> adapterBinder =
            Multibinder.newSetBinder(binder(), IntegrationPlatformAdapter.class);
        adapterBinder.addBinding().to(<Plataforma>Adapter.class);

        // Registrar Adapters de pipeline nos mapas @Named
        MapBinder<PlataformaIntegracaoEnum, PollarCotacoesAbertasPort> polladoresAberturas =
            MapBinder.newMapBinder(binder(), PlataformaIntegracaoEnum.class, PollarCotacoesAbertasPort.class,
                Names.named("polladoresAberturas"));
        polladoresAberturas.addBinding(PlataformaIntegracaoEnum.<PLATAFORMA>)
            .to(<Plataforma>PollarCotacoesAbertasAdapter.class);

        // Repetir para os demais Ports do pipeline
    }

    @Provides
    @Singleton
    public <Plataforma>ApiClient provide<Plataforma>ApiClient(RetrofitClientFactory factory) {
        return factory.create(<Plataforma>ApiClient.class, "<base-url>");
    }
}
```

- [ ] Adapter registrado no `Multibinder<IntegrationPlatformAdapter>`
- [ ] Todos 4 Adapters de pipeline registrados nos `MapBinder` com `@Named` correto
- [ ] `@Provides` do cliente Retrofit criado com URL base correta

---

### Etapa 10 — Job de entrypoint

```java
// entrypoint/job/<Plataforma>PollingJob.java
@Listener(instanceNames = "...")  // ou @Scheduled conforme o framework
public class <Plataforma>PollingJob {

    private final ConsultarCotacoesAbertasUseCase useCase;

    @Inject
    public <Plataforma>PollingJob(ConsultarCotacoesAbertasUseCase useCase) {
        this.useCase = useCase;
    }

    public void executar() throws Exception {
        useCase.execute(PlataformaIntegracaoEnum.<PLATAFORMA>);
    }
}
```

- [ ] Job criado passando `PlataformaIntegracaoEnum.<PLATAFORMA>` para UseCase
- [ ] UseCase chamado é agnóstico (sem nome de plataforma)

---

## Verificação final: Zero alteração no núcleo

Ao concluir implementação da nova plataforma, confirme:

- [ ] **Nenhum** arquivo em `core/domain/` modificado
- [ ] **Nenhum** UseCase existente modificado
- [ ] **Nenhum** Listener ou Worker existente modificado
- [ ] **Apenas** adicionados: arquivos em `infrastructure/integration/<plataforma>/`, `config/integration/<Plataforma>Module.java` e `entrypoint/job/<Plataforma>PollingJob.java`
- [ ] Enum `PlataformaIntegracaoEnum` recebeu apenas novo valor

Qualquer UseCase, Listener ou Worker modificado para acomodar nova plataforma? Arquitetura violada. Revisar.