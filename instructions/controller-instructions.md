---
applyTo: "**/*Controller.java"
---

# Controller (`@Controller`) — Addon Studio 2.0

`@Controller` marca classes = pontos entrada API interna add-on. Cada metodo publico auto-exposto como endpoint servico. Controllers **orquestram** fluxo requisicao — **nunca** contem logica negocio.

> **Referencias complementares:**
> - `architecture-instructions.md` — Camadas e design patterns
> - `dependency-injection-instructions.md` — Injecao de dependencia (Guice)
> - `mapstruct-instructions.md` — Mapeamento de objetos (MapStruct)

---

## 1. Anatomia de um Controller

```java
import br.com.sankhya.studio.annotations.Controller;
import br.com.sankhya.studio.annotations.enums.EJBTransactionType;
import br.com.sankhya.studio.persistence.Transactional;
import com.google.inject.Inject;
import javax.validation.Valid;

@Controller(
    serviceName = "MeuFeatureControllerSP",            // Obrigatorio, sufixo "SP"
    transactionType = EJBTransactionType.Supports       // Opcional (Supports e o padrao)
)
public class MeuFeatureController {

    private final MeuUseCase meuUseCase;                // Dependencias como final
    private final MeuRestMapper mapper;

    @Inject                                             // Injecao via construtor
    public MeuFeatureController(MeuUseCase meuUseCase,
                                MeuRestMapper mapper) {
        this.meuUseCase = meuUseCase;
        this.mapper = mapper;
    }

    @Transactional                                      // Metodos que alteram dados
    public MeuResponse executar(@Valid MeuRequest request) {
        MeuDomain domain = mapper.toDomain(request);    // DTO -> Domain
        MeuDomain resultado = meuUseCase.execute(domain);
        return mapper.toResponse(resultado);             // Domain -> DTO
    }
}
```

---

## 2. Atributos do `@Controller`

### `serviceName` (obrigatorio)

Nome servico registrado plataforma. **Deve** terminar sufixo `SP`.

```java
@Controller(serviceName = "PedidoControllerSP")
```

> `serviceName` define URL acesso servico. Cada metodo publico exposto como `<serviceName>.<nomeDoMetodo>`.

### `transactionType` (opcional)

Define comportamento transacional **padrao** todos metodos classe.

| `EJBTransactionType` | Descricao | Quando usar |
|:---------------------|:----------|:------------|
| `Supports` | Usa transacao se ja existir; senao, sem. | **Padrao.** Controllers mistura leitura+escrita. |
| `Required` | Sempre executa em transacao (cria se nao existir). | Controllers 100% escrita. |
| `NotSupported` | Executa fora transacao (suspende se existir). | Controllers 100% leitura. |

```java
// Padrao (leitura + escrita com @Transactional granular)
@Controller(serviceName = "MeuControllerSP", transactionType = EJBTransactionType.Supports)

// Somente escrita
@Controller(serviceName = "MeuControllerSP", transactionType = EJBTransactionType.Required)

// Somente leitura
@Controller(serviceName = "ConsultaControllerSP", transactionType = EJBTransactionType.NotSupported)
```

---

## 3. Controle Transacional com `@Transactional`

Anotacao `@Transactional` em metodo **sempre tem precedencia** sobre `transactionType` da classe.

```java
@Controller(serviceName = "MeuControllerSP", transactionType = EJBTransactionType.NotSupported)
public class MeuController {

    // Usa o padrao da classe (NotSupported) — sem transacao
    public List<MeuDTO> listar() { ... }

    // Sobrepoe o padrao — executa em transacao propria
    @Transactional
    public MeuDTO criar(@Valid MeuRequest request) { ... }

    // Sobrepoe com transacao nova (isolada)
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void processarBatch() { ... }
}
```

### Quando usar `@Transactional`

| Operacao | `@Transactional` | Motivo |
|:---------|:-----------------|:-------|
| Create / Update / Delete | Sim | Garante atomicidade |
| Leitura simples | Nao | Sem necessidade transacao |
| Leitura + escrita mesmo metodo | Sim | Garante consistencia |
| Operacao idempotente (sem side effects) | Nao | Desnecessario |

---

## 4. DTOs (Request / Response)

Controllers **nunca** expoe entidades dominio diretamente. Use DTOs = contratos entrada/saida.

### Organizacao de pacotes

```
entrypoint/rest/<feature>/
|-- dto/
|   |-- MeuFeatureRequest.java        # Request DTO (entrada)
|   |-- MeuFeatureResponse.java       # Response DTO (saida)
|   |-- OutraOperacaoRequest.java
|-- mapper/
|   |-- MeuFeatureRestMapper.java     # MapStruct mapper
|-- MeuFeatureController.java         # Controller
```

### Request DTO

Usa `@Data` (Lombok) + validacao `javax.validation`:

```java
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
public class CriarPedidoRequest {

    @NotNull(message = "O codigo do parceiro e obrigatorio.")
    private BigDecimal codParceiro;

    @NotBlank(message = "A descricao e obrigatoria.")
    private String descricao;

    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
    private BigDecimal valor;

    private String observacao;   // Opcional — sem validacao
}
```

### Response DTO

Usa `@Data` (Lombok), sem validacao:

```java
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CriarPedidoResponse {
    private BigDecimal numeroPedido;
    private BigDecimal valorTotal;
    private String status;
}
```

### Anotacoes de validacao comuns

| Anotacao | Uso | Exemplo |
|:---------|:----|:--------|
| `@NotNull` | Campo obrigatorio (qualquer tipo) | `@NotNull(message = "...")` |
| `@NotBlank` | String obrigatoria e nao vazia | `@NotBlank(message = "...")` |
| `@NotEmpty` | Collection/String nao vazia | `@NotEmpty(message = "...")` |
| `@DecimalMin` | Valor minimo (BigDecimal) | `@DecimalMin(value = "0.01", message = "...")` |
| `@Size` | Tamanho min/max de String ou Collection | `@Size(min = 1, max = 200)` |
| `@Valid` | Validacao em cascata (objetos nested) | `@Valid MeuDTO dto` |

> `@Valid` em parametro metodo controller ativa validacao automatica. Falha = framework retorna erro antes executar metodo.

---

## 5. Protocolo HTTP — Request e Response

### URL de acesso

```
<dns>/<contexto-addon>/service.sbr?serviceName=<serviceName>.<nomeMetodo>&mgeSession=<jsessionId>
```

- `contexto-addon` = valor `rootProject.name` em `settings.gradle`
- `serviceName` = atributo `@Controller`
- `nomeMetodo` = nome metodo publico controller

**Exemplo:**
```
http://localhost:8080/meu-addon/service.sbr?serviceName=PedidoControllerSP.criarPedido&mgeSession=ABC123
```

### Autenticacao

Todas requisicoes exigem auth via `mgeSession`. `jsessionId` obtido com `MobileLogin`:

```bash
curl --location 'http://localhost:8080/mge/service.sbr?serviceName=MobileLoginSP.login&outputType=json' \
--header 'Content-Type: application/json' \
--data '{
    "requestBody": {
        "NOMUSU": {"$": "USUARIO"},
        "INTERNO": {"$": "SENHA"}
    }
}'
```

> Ambientes com Gateway Sankhya: auth via `MobileLogin` nao necessaria — Gateway gerencia token.

### Formato da Request (POST)

```json
{
  "serviceName": "PedidoControllerSP.criarPedido",
  "requestBody": {
    "request": {
      "codParceiro": 12345,
      "descricao": "Pedido de teste",
      "valor": 150.50,
      "observacao": "Entrega urgente"
    }
  }
}
```

**Regras:**
- `serviceName`: `<serviceName>.<nomeMetodo>` (mesmo valor URL).
- `requestBody`: Contem argumentos metodo como propriedades nomeadas pelo nome parametro Java.

> Metodo `criarPedido(CriarPedidoRequest request)` = JSON usa `"request"` como chave. Se fosse `criarPedido(CriarPedidoRequest pedido)`, chave seria `"pedido"`.

### Formato da Response

**Sucesso (`status = "1"`):**

```json
{
  "serviceName": "PedidoControllerSP.criarPedido",
  "status": "1",
  "pendingPrinting": "false",
  "transactionId": "CB0F625A72C214CF8449F0B18E1FA81A",
  "responseBody": {
    "numeroPedido": 987654,
    "valorTotal": 551.00,
    "status": "PENDENTE"
  }
}
```

**Erro (`status != "1"`):**

```json
{
  "serviceName": "PedidoControllerSP.criarPedido",
  "status": "0",
  "pendingPrinting": "false",
  "transactionId": "CB0F625A72C214CF8449F0B18E1FA81A",
  "statusMessage": "Erro de validacao: O campo descricao e obrigatorio"
}
```

| `status` | Significado |
|:---------|:------------|
| `"1"` | Sucesso |
| `"0"` | Erro de execucao |
| `"3"` | Timeout |
| `"4"` | Cancelado por concorrencia |

> Erro: `responseBody` nao incluido. Mensagem fica em `statusMessage`.

---

## 6. Tipo de Retorno dos Metodos

Tipo retorno cada metodo definido pela regra negocio projeto:

- Metodos retornam dados = retornam **DTO resposta diretamente**.
- Metodos sem retorno dados = **`void`**.

```java
// Com retorno de dados
public PedidoResponse criarPedido(@Valid CriarPedidoRequest request) {
    ...
    return mapper.toResponse(resultado);
}

// Sem retorno de dados
@Transactional
public void cancelar(@Valid CancelarPedidoRequest request) {
    cancelarPedidoUseCase.execute(request.getNuPedido());
}
```

Framework serializa auto objeto retornado em `responseBody` da response.

---

## 7. Tratamento Global de Excecoes (`@ControllerAdvice`)

Tratamento excecoes centralizado em classe `@ControllerAdvice` no pacote `entrypoint/rest/`.

```java
import br.com.sankhya.studio.web.ControllerAdvice;
import br.com.sankhya.studio.web.ExceptionHandler;
import lombok.extern.java.Log;
import java.util.logging.Level;

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

    @ExceptionHandler(Exception.class)
    public void handleGeneric(Exception e) {
        log.log(Level.SEVERE, "Erro inesperado: {0}", e.getMessage());
        throw new RuntimeException(e);
    }
}
```

**Regras:**
- Um `@ControllerAdvice` por projeto.
- Cada `@ExceptionHandler` trata **um tipo** excecao.
- Ordenar do mais especifico ao mais generico (framework prioriza especifico).
- Logar excecao com nivel adequado (`INFO`, `WARNING`, `SEVERE`).
- **Nunca** capturar excecao negocio no controller — deixe `@ControllerAdvice` tratar.

### Mapeamento Excecao — nivel de log sugerido

| Excecao | Level de Log |
|:--------|:-------------|
| `EntityNotFoundException` | `INFO` |
| `IllegalArgumentException` | `WARNING` |
| `DomainValidationException` | `WARNING` |
| `IntegrationImportException` | `WARNING` |
| `IntegrationExportException` | `WARNING` |
| `IntegrationApiException` | `WARNING` |
| `IntegrationNetworkException` | `WARNING` |
| `RuntimeException` | `SEVERE` |
| `Exception` | `SEVERE` |

---

## 8. Fluxo Padrao de um Metodo

```
Request DTO —@Valid—> Controller —mapper—> Domain Object
                        |
                        |— UseCase.execute(domain)
                        |       |— (logica no UseCase / DomainService / Entity)
                        |
                        |— mapper.toResponse(resultado)
                        |
                        |— return response
```

### Metodo com entrada e saida (CRUD)

```java
@Transactional
public PedidoResponse criarPedido(@Valid CriarPedidoRequest request) {
    Pedido pedido = mapper.toPedido(request);
    Pedido resultado = criarPedidoUseCase.execute(pedido);
    return mapper.toCriarResponse(resultado);
}
```

### Metodo somente com entrada (acao sem retorno)

```java
@Transactional
public void cancelar(@Valid CancelarPedidoRequest request) {
    cancelarPedidoUseCase.execute(request.getNuPedido());
}
```

### Metodo somente com saida (consulta)

```java
public List<ProdutoDTO> listarProdutos() {
    List<Produto> produtos = listarProdutosUseCase.execute();
    return produtos.stream()
        .map(mapper::toDTO)
        .collect(Collectors.toList());
}
```

### Metodo sem entrada e sem saida (trigger)

```java
@Transactional
public void sincronizar() {
    sincronizarUseCase.execute();
}
```

---

## 9. Exemplos Completos

### Controller simples (CRUD)

```java
@Controller(
    serviceName = "AlvoControllerSP",
    transactionType = EJBTransactionType.Supports
)
public class AlvoController {

    private final ImportarAlvoUseCase importarAlvoUseCase;

    @Inject
    public AlvoController(ImportarAlvoUseCase importarAlvoUseCase) {
        this.importarAlvoUseCase = importarAlvoUseCase;
    }

    @Transactional
    public List<AlvoDTO> importar() {
        return importarAlvoUseCase.execute();
    }
}
```

### Controller completo (multiplas operacoes)

```java
@Controller(
    serviceName = "PedidoControllerSP",
    transactionType = EJBTransactionType.Supports
)
public class PedidoController {

    private final CriarPedidoUseCase criarPedidoUseCase;
    private final CancelarPedidoUseCase cancelarPedidoUseCase;
    private final EmitirPedidoUseCase emitirPedidoUseCase;
    private final GerarPdfUseCase gerarPdfUseCase;
    private final PedidoRestMapper mapper;

    @Inject
    public PedidoController(
        CriarPedidoUseCase criarPedidoUseCase,
        CancelarPedidoUseCase cancelarPedidoUseCase,
        EmitirPedidoUseCase emitirPedidoUseCase,
        GerarPdfUseCase gerarPdfUseCase,
        PedidoRestMapper mapper
    ) {
        this.criarPedidoUseCase = criarPedidoUseCase;
        this.cancelarPedidoUseCase = cancelarPedidoUseCase;
        this.emitirPedidoUseCase = emitirPedidoUseCase;
        this.gerarPdfUseCase = gerarPdfUseCase;
        this.mapper = mapper;
    }

    @Transactional
    public CriarPedidoResponse criar(@Valid CriarPedidoRequest request) {
        Pedido pedido = mapper.toPedido(request);
        Pedido resultado = criarPedidoUseCase.execute(pedido);
        return mapper.toCriarResponse(resultado);
    }

    @Transactional
    public EmitirPedidoResponse emitir(@Valid EmitirPedidoRequest request) {
        Resultado resultado = emitirPedidoUseCase.execute(request.getNuPedido());
        Impressao impressao = gerarPdfUseCase.execute(resultado);

        ServiceContext ctx = ServiceContext.getCurrent();
        ctx.putHttpSessionAttribute(impressao.getLabel(), impressao.getFile());

        return mapper.toEmitirResponse(resultado);
    }

    @Transactional
    public void cancelar(@Valid CancelarPedidoRequest request) {
        cancelarPedidoUseCase.execute(request.getNuPedido());
    }
}
```

---

## 10. Convencoes

| Convencao | Padrao | Exemplo |
|:----------|:-------|:--------|
| Nome da classe | `<Feature>Controller` | `PedidoController` |
| `serviceName` | `<Feature>ControllerSP` | `PedidoControllerSP` |
| Pacote | `entrypoint/rest/<feature>/` | `entrypoint/rest/pedido/` |
| DTOs | `entrypoint/rest/<feature>/dto/` | `CriarPedidoRequest.java` |
| Mapper | `entrypoint/rest/<feature>/mapper/` | `PedidoRestMapper.java` |
| Nome Request DTO | `<Acao>Request` ou `<Acao><Feature>Request` | `CriarPedidoRequest` |
| Nome Response DTO | `<Acao>Response` ou `<Acao><Feature>Response` | `EmitirPedidoResponse` |
| Nome Mapper | `<Feature>RestMapper` | `PedidoRestMapper` |

---

## 11. Checklist: Novo Controller

1. [ ] Criar classe em pacote `entrypoint/rest/<feature>/`.
2. [ ] Anotar com `@Controller(serviceName = "<Feature>ControllerSP")`.
3. [ ] Definir `transactionType` adequado (ou usar padrao `Supports`).
4. [ ] Injetar UseCases + Mapper via construtor com `@Inject`.
5. [ ] Criar Request DTOs com validacao (`@NotNull`, `@NotBlank`, etc.) em `dto/`.
6. [ ] Criar Response DTOs em `dto/`.
7. [ ] Criar MapStruct Mapper em `mapper/` (ver `mapstruct-instructions.md`).
8. [ ] Usar `@Valid` em parametro dos metodos que recebem DTOs.
9. [ ] Usar `@Transactional` em metodos que alteram dados.
10. [ ] Retornar tipo adequado conforme regra negocio (DTO resposta ou `void`).
11. [ ] **NAO** colocar logica negocio — delegar UseCases.
12. [ ] **NAO** capturar excecoes — deixar `@ControllerAdvice` tratar.
13. [ ] Verificar se `@ControllerAdvice` cobre excecoes dos UseCases.

---

## 12. Anti-Patterns (PROIBIDO)

| Anti-Pattern | Correcao |
|:-------------|:---------|
| Retornar entidade do dominio diretamente | Usar Response DTO + MapStruct |
| Logica de negocio no controller | Mover para UseCase ou Domain Service |
| `try/catch` no controller para excecoes de negocio | Deixar o `@ControllerAdvice` tratar |
| Controller acessando Repository diretamente | Usar UseCase como intermediario |
| Controller chamando Gateway diretamente | Usar UseCase como intermediario |
| `serviceName` sem sufixo `SP` | Sempre `<Nome>SP` |
| Esquecer `@Transactional` em metodo de escrita | Adicionar `@Transactional` |
| Esquecer `@Valid` no parametro | Adicionar `@Valid` para ativar validacao |
| Adicionar `@Component` no controller | `@Controller` ja e gerenciado — nao misturar |
| Capturar excecao e retornar `null` | Deixar a excecao propagar para o `@ControllerAdvice` |