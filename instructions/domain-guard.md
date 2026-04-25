# domain-guard.md

## Propósito

Ativado automaticamente quando arquivo em `core/domain/` criado ou modificado.

**Leia íntegra antes de escrever código no domínio.**

---

## Regra Absoluta

> `core/domain/` não conhece outras camadas. Zero imports de `infrastructure/`, `entrypoint/` ou `config/`.

Prestes a adicionar import que não seja:
- Classe do próprio pacote `core/domain/**`
- Anotação de framework (`@JapeEntity`, `@Id`, `@Column`, `@Data`, `@Builder`, etc.)
- Classe Java padrão (`java.util.*`, `java.math.*`, `java.sql.*`, etc.)

**PARE. Violação de arquitetura.**

---

## O que PODE existir em `core/domain/`

| Pacote | Conteúdo permitido |
|:-------|:-------------------|
| `entity/` | Classes com `@JapeEntity` + métodos de negócio. Classes `@Data` sem tabela (domínio puro). |
| `gateway/` | **Só interfaces.** Zero implementação concreta. |
| `repository/` | **Só interfaces** com `@Repository` estendendo `JapeRepository`. |
| `service/` | Domain Services com `@Component`. Lógica complexa entre entidades. Injeta só `repository/` e outros `service/` do domínio. |
| `exception/` | Exceções tipadas com semântica de negócio. Estendem `RuntimeException` ou hierarquia em `architecture-instructions.md`. |
| `vo/` | Enums com valor persistido. PKs compostas (`@Embeddable`). DTOs internos (sem anotação de serialização externa). |

---

## O que NUNCA pode existir em `core/domain/`

### Imports proibidos — se aparecerem, violação imediata:

```
// PROIBIDO — classes de infraestrutura
import ...infrastructure.*

// PROIBIDO — clientes HTTP
import retrofit2.*
import okhttp3.*

// PROIBIDO — DTOs de API externa (pertencem a infrastructure/integration/<plataforma>/dto/)
// Qualquer classe cujo nome contenha o nome de uma plataforma externa
// Ex: BionexoXxxDto, AgrosigXxxResponse, etc.

// PROIBIDO — anotações de serialização externa
import com.google.gson.*
import com.fasterxml.jackson.*

// PROIBIDO — entrypoint
import ...entrypoint.*

// PROIBIDO — config
import ...config.*
```

### Situações que exigem PARADA imediata:

| Situação | O que fazer |
|:---------|:------------|
| Preciso usar DTO de resposta de API externa no domínio | Criar objeto de domínio puro em `vo/` e mapear via MapStruct no Gateway concreto |
| Preciso chamar API HTTP do domínio | Criar interface em `domain/gateway/` e implementar em `infrastructure/integration/<plataforma>/` |
| Preciso saber qual plataforma está ativa | Não é do domínio. `DynamicGateway` resolve na infra. |
| Entidade precisa de campo que só existe na API externa | Modelar conceito de negócio equivalente no domínio. Mapper traduz. |

---

## Checklist obrigatório antes de salvar qualquer arquivo em `core/domain/`

- [ ] Zero import de `infrastructure/`
- [ ] Zero import de `entrypoint/`
- [ ] Zero DTO de plataforma externa referenciado
- [ ] Interfaces de gateway usam só tipos do domínio na assinatura
- [ ] Exceções usam hierarquia tipada (não `RuntimeException` genérico)
- [ ] Zero dependência criada com `new` — usar `@Inject` via construtor
- [ ] Gateway: **interface**, nunca classe concreta

---

## Exemplo: o que fazer quando um tipo externo "precisa" entrar no domínio

**Erro comum:** Modelo recebe resposta da API com campo `statusCotacao` — string proprietária da plataforma (`"ABERTA"`, `"ENCERRADA"`). Em vez de mapear, joga campo direto na entidade.

**Errado:**
```java
// core/domain/entity/Cotacao.java
@JapeEntity
public class Cotacao {
    // VIOLAÇÃO: tipo/valor acoplado à plataforma externa
    private String statusCotacao; // "ABERTA", "ENCERRADA" — strings da API
}
```

**Correto:**
```java
// core/domain/vo/StatusCotacao.java — enum de domínio
@AllArgsConstructor @Getter
public enum StatusCotacao {
    ABERTA("A"), ENCERRADA("E"), CONFIRMADA("C");
    private final String value;
}

// core/domain/entity/Cotacao.java — usa o tipo do domínio
@JapeEntity
public class Cotacao {
    private StatusCotacao status; // tipo do domínio, agnóstico de plataforma
}

// infrastructure/integration/<plataforma>/mapper/<Plataforma>CotacaoMapper.java
// O mapper traduz "ABERTA" → StatusCotacao.ABERTA
```