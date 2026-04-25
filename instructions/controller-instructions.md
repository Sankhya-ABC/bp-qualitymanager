---
applyTo: "**/*Controller.java"
---

# Controller (`@Controller`) - Addon Studio 2.0

`@Controller` marca classes = pontos de entrada da API interna do add-on. Cada metodo publico e auto-exposto como endpoint de servico. Controllers **orquestram** o fluxo da requisicao - **nunca** contem regra de negocio.

> **Referencias complementares:**
> - `architecture-instructions.md` - Camadas e organizacao por feature
> - `dependency-injection-instructions.md` - Injecao de dependencia (Guice)
> - `mapstruct-instructions.md` - Mapeamento de objetos (MapStruct)

---

## 1. Anatomia de um Controller

```java
import br.com.sankhya.studio.annotations.Controller;
import br.com.sankhya.studio.annotations.enums.EJBTransactionType;
import br.com.sankhya.studio.persistence.Transactional;
import com.google.inject.Inject;
import javax.validation.Valid;

@Controller(
    serviceName = "RegistroControllerSP",                // Obrigatorio, sufixo "SP"
    transactionType = EJBTransactionType.Supports        // Opcional (Supports e o padrao)
)
public class RegistroController {

    private final RegistroService service;               // Service da feature
    private final RegistroMapper mapper;                 // Mapper da feature

    @Inject                                              // Injecao via construtor
    public RegistroController(RegistroService service,
                              RegistroMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Transactional                                       // Metodos que alteram dados
    public RegistroResponse criar(@Valid CriarRegistroRequest request) throws Exception {
        Registro entity = mapper.toEntity(request);      // DTO -> Entity
        Registro salvo = service.criar(entity);          // Delega ao Service
        return mapper.toResponse(salvo);                 // Entity -> DTO
    }
}
```

---

## 2. Atributos do `@Controller`

### `serviceName` (obrigatorio)

Nome do servico registrado na plataforma. **Deve** terminar com sufixo `SP`.

```java
@Controller(serviceName = "RegistroControllerSP")
```

> `serviceName` define a URL de acesso ao servico. Cada metodo publico e exposto como `<serviceName>.<nomeDoMetodo>`.

### `transactionType` (opcional)

Define o comportamento transacional **padrao** de todos os metodos da classe.

| `EJBTransactionType` | Descricao | Quando usar |
|:---------------------|:----------|:------------|
| `Supports` | Usa transacao se ja existir; senao, sem. | **Padrao.** Controllers misturam leitura e escrita. |
| `Required` | Sempre executa em transacao (cria se nao existir). | Controllers 100% escrita. |
| `NotSupported` | Executa fora de transacao (suspende se existir). | Controllers 100% leitura. |

```java
// Padrao (leitura + escrita com @Transactional granular)
@Controller(serviceName = "RegistroControllerSP", transactionType = EJBTransactionType.Supports)

// Somente escrita
@Controller(serviceName = "RegistroControllerSP", transactionType = EJBTransactionType.Required)

// Somente leitura
@Controller(serviceName = "ConsultaControllerSP", transactionType = EJBTransactionType.NotSupported)
```

---

## 3. Controle Transacional com `@Transactional`

`@Transactional` em metodo **sempre tem precedencia** sobre `transactionType` da classe.

```java
@Controller(serviceName = "RegistroControllerSP", transactionType = EJBTransactionType.NotSupported)
public class RegistroController {

    // Usa o padrao da classe (NotSupported) - sem transacao
    public List<RegistroResponse> listar() { ... }

    // Sobrepoe o padrao - executa em transacao propria
    @Transactional
    public RegistroResponse criar(@Valid CriarRegistroRequest request) { ... }

    // Sobrepoe com transacao nova (isolada)
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void processarBatch() { ... }
}
```

### Quando usar `@Transactional`

| Operacao | `@Transactional` | Motivo |
|:---------|:-----------------|:-------|
| Create / Update / Delete | Sim | Garante atomicidade |
| Leitura simples | Nao | Sem necessidade de transacao |
| Leitura + escrita no mesmo metodo | Sim | Garante consistencia |
| Operacao idempotente (sem side effects) | Nao | Desnecessario |

---

## 4. DTOs (Request / Response)

Controllers **nunca** expoem entidades diretamente. Use DTOs como contratos de entrada/saida.

### Organizacao de pacotes

```
br/com/hagious/qualitymanager/<feature>/
|-- controller/
|   |-- <Feature>Controller.java
|-- service/
|   |-- <Feature>Service.java
|-- dto/
|   |-- Criar<Feature>Request.java        # Request DTO (entrada)
|   |-- <Feature>Response.java            # Response DTO (saida)
|-- mapper/
|   |-- <Feature>Mapper.java              # MapStruct mapper
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
public class CriarRegistroRequest {

    @NotNull(message = "O codigo do parceiro e obrigatorio.")
    private BigDecimal codParceiro;

    @NotBlank(message = "A descricao e obrigatoria.")
    private String descricao;

    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
    private BigDecimal valor;

    private String observacao;   // Opcional - sem validacao
}
```

### Response DTO

Usa `@Data` (Lombok), sem validacao:

```java
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RegistroResponse {
    private Integer codRegistro;
    private String descricao;
    private BigDecimal valor;
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

> `@Valid` em parametro de metodo de controller ativa a validacao automatica. Falha = framework retorna erro antes de executar o metodo.

---

## 5. Protocolo HTTP - Request e Response

### URL de acesso

```
<dns>/<contexto-addon>/service.sbr?serviceName=<serviceName>.<nomeMetodo>&mgeSession=<jsessionId>
```

- `contexto-addon` = valor de `rootProject.name` em `settings.gradle`
- `serviceName` = atributo `@Controller`
- `nomeMetodo` = nome do metodo publico do controller

**Exemplo:**
```
http://localhost:8080/bp-qualitymanager/service.sbr?serviceName=RegistroControllerSP.criar&mgeSession=ABC123
```

### Autenticacao

Todas as requisicoes exigem auth via `mgeSession`. `jsessionId` e obtido com `MobileLogin`:

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

> Ambientes com Gateway Sankhya: auth via `MobileLogin` nao e necessaria - o Gateway gerencia o token.

### Formato da Request (POST)

```json
{
  "serviceName": "RegistroControllerSP.criar",
  "requestBody": {
    "request": {
      "codParceiro": 12345,
      "descricao": "Registro de teste",
      "valor": 150.50,
      "observacao": "Entrega urgente"
    }
  }
}
```

**Regras:**
- `serviceName`: `<serviceName>.<nomeMetodo>` (mesmo valor da URL).
- `requestBody`: contem os argumentos do metodo como propriedades nomeadas pelo nome do parametro Java.

> Metodo `criar(CriarRegistroRequest request)` -> JSON usa `"request"` como chave. Se fosse `criar(CriarRegistroRequest registro)`, a chave seria `"registro"`.

### Formato da Response

**Sucesso (`status = "1"`):**

```json
{
  "serviceName": "RegistroControllerSP.criar",
  "status": "1",
  "pendingPrinting": "false",
  "transactionId": "CB0F625A72C214CF8449F0B18E1FA81A",
  "responseBody": {
    "codRegistro": 987654,
    "descricao": "Registro de teste",
    "status": "PENDENTE"
  }
}
```

**Erro (`status != "1"`):**

```json
{
  "serviceName": "RegistroControllerSP.criar",
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

> Em erro, `responseBody` nao e incluido. A mensagem fica em `statusMessage`.

---

## 6. Tipo de Retorno dos Metodos

Tipo de retorno de cada metodo definido pela regra de negocio do projeto:

- Metodos que retornam dados = retornam **DTO de resposta diretamente**.
- Metodos sem retorno de dados = **`void`**.

```java
// Com retorno de dados
public RegistroResponse criar(@Valid CriarRegistroRequest request) throws Exception {
    Registro entity = mapper.toEntity(request);
    Registro salvo = service.criar(entity);
    return mapper.toResponse(salvo);
}

// Sem retorno de dados
@Transactional
public void cancelar(@Valid CancelarRegistroRequest request) throws Exception {
    service.cancelar(request.getCodRegistro());
}
```

O framework serializa automaticamente o objeto retornado em `responseBody` da response.

---

## 7. Tratamento Global de Excecoes (`@ControllerAdvice`)

Tratamento de excecoes centralizado em classe `@ControllerAdvice` em `shared/exception/` ou em pacote raiz dedicado.

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
- Cada `@ExceptionHandler` trata **um tipo** de excecao.
- Ordenar do mais especifico ao mais generico (framework prioriza especifico).
- Logar excecao com nivel adequado (`INFO`, `WARNING`, `SEVERE`).
- **Nunca** capturar excecao de negocio no controller - deixe o `@ControllerAdvice` tratar.

### Mapeamento Excecao - nivel de log sugerido

| Excecao | Level de Log |
|:--------|:-------------|
| `EntityNotFoundException` | `INFO` |
| `IllegalArgumentException` | `WARNING` |
| Excecoes de negocio da feature | `WARNING` |
| `RuntimeException` | `SEVERE` |
| `Exception` | `SEVERE` |

---

## 8. Fluxo Padrao de um Metodo

```
Request DTO --@Valid--> Controller --mapper--> Entity
                          |
                          |-- Service.acao(entity)
                          |       |-- valida regra de negocio
                          |       |-- Repository.save/find/delete
                          |       |-- return resultado
                          |
                          |-- mapper.toResponse(resultado)
                          |
                          |-- return Response
```

### Metodo com entrada e saida (CRUD)

```java
@Transactional
public RegistroResponse criar(@Valid CriarRegistroRequest request) throws Exception {
    Registro entity = mapper.toEntity(request);
    Registro salvo = service.criar(entity);
    return mapper.toResponse(salvo);
}
```

### Metodo somente com entrada (acao sem retorno)

```java
@Transactional
public void cancelar(@Valid CancelarRegistroRequest request) throws Exception {
    service.cancelar(request.getCodRegistro());
}
```

### Metodo somente com saida (consulta)

```java
public List<RegistroResponse> listar() {
    return service.listarAtivos().stream()
        .map(mapper::toResponse)
        .collect(Collectors.toList());
}
```

### Metodo sem entrada e sem saida (trigger)

```java
@Transactional
public void sincronizar() throws Exception {
    service.sincronizar();
}
```

---

## 9. Exemplos Completos

### Controller simples (CRUD)

```java
@Controller(
    serviceName = "FornecedorControllerSP",
    transactionType = EJBTransactionType.Supports
)
public class FornecedorController {

    private final FornecedorService service;
    private final FornecedorMapper mapper;

    @Inject
    public FornecedorController(FornecedorService service, FornecedorMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    public List<FornecedorResponse> listar() {
        return service.listarAtivos().stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }
}
```

### Controller completo (multiplas operacoes)

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

    @Transactional
    public RegistroResponse atualizar(@Valid AtualizarRegistroRequest request) throws Exception {
        Registro entity = mapper.toEntity(request);
        Registro atualizado = service.atualizar(entity);
        return mapper.toResponse(atualizado);
    }

    @Transactional
    public void cancelar(@Valid CancelarRegistroRequest request) throws Exception {
        service.cancelar(request.getCodRegistro());
    }

    public List<RegistroResponse> listar() {
        return service.listarAtivos().stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }
}
```

---

## 10. Convencoes

| Convencao | Padrao | Exemplo |
|:----------|:-------|:--------|
| Nome da classe | `<Feature>Controller` | `RegistroController` |
| `serviceName` | `<Feature>ControllerSP` | `RegistroControllerSP` |
| Pacote | `br/com/hagious/qualitymanager/<feature>/controller/` | `.../registro/controller/` |
| DTOs | `<feature>/dto/` | `CriarRegistroRequest.java` |
| Mapper | `<feature>/mapper/` | `RegistroMapper.java` |
| Nome Request DTO | `<Acao><Feature>Request` ou `<Acao>Request` | `CriarRegistroRequest` |
| Nome Response DTO | `<Feature>Response` ou `<Acao>Response` | `RegistroResponse` |
| Nome Mapper | `<Feature>Mapper` | `RegistroMapper` |

---

## 11. Checklist: Novo Controller

1. [ ] Criar classe em `br/com/hagious/qualitymanager/<feature>/controller/`.
2. [ ] Anotar com `@Controller(serviceName = "<Feature>ControllerSP")`.
3. [ ] Definir `transactionType` adequado (ou usar padrao `Supports`).
4. [ ] Injetar Service + Mapper via construtor com `@Inject`.
5. [ ] Criar Request DTOs com validacao (`@NotNull`, `@NotBlank`, etc.) em `<feature>/dto/`.
6. [ ] Criar Response DTOs em `<feature>/dto/`.
7. [ ] Criar MapStruct Mapper em `<feature>/mapper/` (ver `mapstruct-instructions.md`).
8. [ ] Usar `@Valid` em parametro dos metodos que recebem DTOs.
9. [ ] Usar `@Transactional` em metodos que alteram dados.
10. [ ] Retornar tipo adequado conforme regra de negocio (DTO de resposta ou `void`).
11. [ ] **NAO** colocar regra de negocio - delegar ao Service.
12. [ ] **NAO** capturar excecoes - deixar o `@ControllerAdvice` tratar.
13. [ ] Verificar se `@ControllerAdvice` cobre as excecoes do Service.

---

## 12. Anti-Patterns (PROIBIDO)

| Anti-Pattern | Correcao |
|:-------------|:---------|
| Retornar entidade diretamente | Usar Response DTO + MapStruct |
| Regra de negocio no controller | Mover para o Service |
| `try/catch` no controller para excecoes de negocio | Deixar o `@ControllerAdvice` tratar |
| Controller acessando Repository diretamente | Sempre via Service |
| `serviceName` sem sufixo `SP` | Sempre `<Nome>SP` |
| Esquecer `@Transactional` em metodo de escrita | Adicionar `@Transactional` |
| Esquecer `@Valid` no parametro | Adicionar `@Valid` para ativar validacao |
| Adicionar `@Component` no controller | `@Controller` ja e gerenciado - nao misturar |
| Capturar excecao e retornar `null` | Deixar a excecao propagar para o `@ControllerAdvice` |
