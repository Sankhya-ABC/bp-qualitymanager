# usecase-guard.md

## Propósito

Arquivo ativa automático quando UseCase em `core/application/usecase/` criado ou modificado.

**Leia arquivo inteiro antes escrever linha em UseCase.**

---

## Responsabilidade do UseCase

UseCase **orquestra**. Não decide regra negócio, não chama HTTP, não sabe plataforma ativa.

```
UseCase = conectar Domain + Gateway + Repository em sequência lógica
```

Lógica negócio dentro UseCase em vez delegar pra entidade ou Domain Service = errado.

---

## O que um UseCase PODE fazer

- Chamar `gateway.operacao(...)` via interface domínio (`domain/gateway/`) ou `DynamicGateway` (`infrastructure/gateway/`)
- Chamar `repository.save(...)` / `repository.findBy...(...)` via interface domínio
- Chamar `domainService.operacao(...)` via Domain Service do domínio
- Chamar `port.execute(...)` via Port de `core/application/port/` (pipeline polling)
- Iterar listas e tratar exceções por item (batch)
- Usar `@Transactional(REQUIRES_NEW)` em métodos auxiliares batch
- Log com `@Log` Lombok + `java.util.logging`

---

## O que um UseCase NUNCA pode fazer

### Imports proibidos:

```java
// PROIBIDO — cliente HTTP direto
import retrofit2.*
import okhttp3.*

// PROIBIDO — DTOs de plataforma externa
import ...infrastructure.integration.<plataforma>.dto.*

// PROIBIDO — implementações concretas de integração
import ...infrastructure.integration.<plataforma>.*Gateway
import ...infrastructure.integration.<plataforma>.*Adapter
import ...infrastructure.integration.<plataforma>.*Client

// PROIBIDO — LocalRepositoryService ou qualquer service de infra
import ...infrastructure.service.*
import ...infrastructure.utils.*
```

### Comportamentos proibidos:

| Proibido | Correção |
|:---------|:---------|
| Nome plataforma no nome classe (`ConsultarCotacoesBionexoUseCase`) | UseCase agnóstico: `ConsultarCotacoesAbertas UseCase`. Plataforma vem como parâmetro ou resolvida por Dynamic Gateway. |
| `if (plataforma == BIONEXO) { ... } else if (plataforma == AGROSIG) { ... }` | Port + Adapters por plataforma injetados via `Map<PlataformaIntegracaoEnum, Port>` com `@Named` |
| Instanciar dependências com `new` | `@Inject` via construtor |
| Chamar `RetrofitClient` direto | Criar/usar Gateway (interface domínio, impl infra) |
| Lógica negócio inline (validações, cálculos, regras) | Mover pra método na Entidade ou Domain Service |
| Lançar `new RuntimeException(...)` | Exceção tipada de `domain/exception/` |
| Converter DTO externo pra domínio no UseCase | Conversão fica no Gateway concreto via MapStruct, antes retornar ao UseCase |

---

## Estrutura obrigatória de um UseCase

```java
@Log
@Component
public class <Verbo><Entidade>UseCase {

    // Injetar APENAS: interfaces de Gateway (domain ou Dynamic), 
    //                 interfaces de Repository, Domain Services,
    //                 Ports de application/port/
    private final <Entidade>Gateway gateway;       // interface do domínio
    private final <Entidade>Repository repository; // interface do domínio

    @Inject
    public <Verbo><Entidade>UseCase(<Entidade>Gateway gateway,
                                    <Entidade>Repository repository) {
        this.gateway = gateway;
        this.repository = repository;
    }

    public <ReturnType> execute(<Params>) throws Exception {
        // 1. Buscar/preparar dados via gateway/repository
        // 2. Delegar regra de negócio para entidade/domain service
        // 3. Persistir se necessário
        // 4. Retornar resultado
    }
}
```

---

## Padrão específico: UseCase de Polling (agnóstico de plataforma)

UseCases que disparam polling **nunca** acoplados a plataforma.

**Errado:**
```java
// VIOLAÇÃO: nome e lógica acoplados à plataforma
public class ConsultarCotacoesBionexoUseCase {
    private final BionexoGateway bionexoGateway; // impl concreta
    ...
}
```

**Correto:**
```java
// UseCase agnóstico — recebe a plataforma como parâmetro
@Log
@Component
public class ConsultarCotacoesAbertasUseCase {

    private final Map<PlataformaIntegracaoEnum, PollarCotacoesAbertasPort> polladoresAberturas;
    private final FilaCotacaoRepository filaCotacaoRepository;

    @Inject
    public ConsultarCotacoesAbertasUseCase(
            @Named("polladoresAberturas") Map<PlataformaIntegracaoEnum, PollarCotacoesAbertasPort> polladoresAberturas,
            FilaCotacaoRepository filaCotacaoRepository) {
        this.polladoresAberturas = polladoresAberturas;
        this.filaCotacaoRepository = filaCotacaoRepository;
    }

    public void execute(PlataformaIntegracaoEnum plataforma) throws Exception {
        PollarCotacoesAbertasPort pollador = polladoresAberturas.get(plataforma);
        List<FilaCotacao> itens = pollador.pollar(plataforma);
        // FilaCotacao.criar(...) já encapsula status, tentativas e dhInclusao.
        // O UseCase apenas persiste — não redefine campos de ciclo de vida.
        for (FilaCotacao item : itens) {
            filaCotacaoRepository.saveWithDedup(item);
        }
    }
}
```

**Contrato do Adapter de polling:** Adapter chama factory method da entidade (ex: `FilaCotacao.criar(idExterno, codPlataforma, pathArquivo)`) que já encapsula estado inicial completo. UseCase não redefine `status`, `tentativas` ou `dhInclusao` — campos são invariantes da entidade, não responsabilidade do UseCase.

---

## Checklist obrigatório antes de salvar um UseCase

- [ ] Nome classe sem nome plataforma
- [ ] Zero import de `infrastructure/integration/<plataforma>/`
- [ ] Zero import de `retrofit2` ou `okhttp3`
- [ ] Zero lógica negócio inline (regras, validações, cálculos)
- [ ] Toda conversão DTO→Domain no Gateway, não aqui
- [ ] Dependências injetadas via `@Inject` no construtor
- [ ] Se UseCase polling: usa `Map<PlataformaIntegracaoEnum, Port>` com `@Named`
- [ ] Exceções tipadas de `domain/exception/`