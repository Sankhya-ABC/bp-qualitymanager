# agent-instructions.md

## Propósito

Arquivo = fonte única governança agentes IA neste repo.
`AGENTS.md`, `CLAUDE.md`, `.github/copilot-instructions.md` = thin wrappers delegando pra este doc.

---

## Princípio de Operação

**Antes qualquer implementação, identifique tipo artefato criar/modificar e siga protocolo correspondente.**

Não leia instruções como lista recomendações. Leia como pré-condições obrigatórias. Pré-requisito não satisfeito → pergunte dev antes prosseguir.

---

## Pré-requisito Universal

**Antes escrever código:**

1. Execute `git fetch origin develop`
2. Execute `git merge origin/develop`
3. Conflitos: **PARE** e pergunte dev como resolver antes continuar

---

## Protocolos por Tipo de Artefato

### Qualquer arquivo em `core/domain/`

**Antes criar/modificar:**
1. Leia `instructions/domain-guard.md` íntegra
2. Confirme: nenhum import `infrastructure/`, `entrypoint/`, `config/` será adicionado
3. Confirme: interfaces Gateway só tipos do próprio domínio nas assinaturas
4. Conceito vindo API externa: modele como tipo domínio em `vo/`, não use DTO plataforma

**Impossível satisfazer regras sem modificar domínio inadequadamente: PARE e pergunte dev.**

---

### Qualquer arquivo em `core/application/usecase/`

**Antes criar/modificar:**
1. Leia `instructions/usecase-guard.md` íntegra
2. Confirme: nome classe sem nome plataforma
3. Confirme: nenhum import `infrastructure/integration/<plataforma>/` será adicionado
4. Confirme: nenhuma conversão DTO→Domain dentro UseCase
5. UseCase polling: confirme uso `Map<PlataformaIntegracaoEnum, Port>` com `@Named`

---

### Nova plataforma de integração

**Antes criar arquivo:**
1. Leia `instructions/architecture-instructions.md` seções 3.4, 9, 10
2. Leia `instructions/new-platform-checklist.md` íntegra
3. Pergunte dev:
   - Quais Ports `core/application/port/` já existem?
   - Quais interfaces `core/domain/gateway/` já existem?
   - Enum `PlataformaIntegracaoEnum` já tem valor pra plataforma?
4. **Crie artefatos ordem exata** definida em `new-platform-checklist.md`
5. Nunca crie Gateway concreto antes interface existir em `domain/gateway/`

---

### Controllers, Listeners, Callbacks (`entrypoint/`)

**Antes criar/modificar:**
1. Leia `instructions/architecture-instructions.md` seção 3.3
2. Confirme: nenhuma lógica negócio inline — delega pra UseCase
3. Confirme: nenhum acesso direto Repository/Gateway — sempre via UseCase
4. Leia `instructions/controller-instructions.md` se Controller REST

---

### Qualquer arquivo de infraestrutura (`infrastructure/`)

**Antes criar/modificar:**
1. Leia `instructions/architecture-instructions.md` seção 3.4
2. Confirme: implementações concretas Gateway implementam interface `domain/gateway/`
3. Confirme: DTOs externos ficam `infrastructure/integration/<plataforma>/dto/`, não saem dali
4. Confirm: conversão DTO→Domain ocorre Gateway concreto via MapStruct

---

### Repositórios (`@Repository`)

**Antes criar/modificar:**
1. Leia `instructions/repository-instructions.md` íntegra
2. Confirme: interface em `core/domain/repository/`, nunca `infrastructure/`
3. Confirme: `JapeRepository<TipoID, TipoEntidade>` — ID primeiro parâmetro
4. Confirme: toda clause `@Criteria` usa prefixo `this.` nos campos
5. Confirme: interfaces `@NativeQuery.Result` em `core/domain/vo/`

---

### Entidades Java (`@JapeEntity` ou objeto de domínio puro)

**Antes criar/modificar:**
1. Leia `instructions/entity-instructions.md`
2. Confirme: entidade rica (métodos negócio, não só getters/setters)
3. Confirme: nenhuma anotação serialização externa (Jackson, Gson)
4. Confirme: metadata UI no XML dicionário, não na entidade Java

**Consistência obrigatória — acione agente `dba` sempre que:**
- Campo **adicionado** à entidade → `dba` gera script `ADD COLUMN` e atualiza dicionário XML
- Campo **removido** da entidade → `dba` gera script `DROP COLUMN` (se aplicável) e remove dicionário XML
- Campo **renomeado** → `dba` atualiza script e dicionário
- Dicionário XML ou script banco alterado → verifique se entidade Java reflete mudança

Nunca deixe entidade Java e dicionário dados fora sincronia.

---

### Injeção de Dependência / Módulos Guice

**Antes criar/modificar:**
1. Leia `instructions/dependency-injection-instructions.md`
2. Confirme: `@Inject` via construtor em todas classes gerenciadas
3. Nova plataforma: confirme 4 Adapters pipeline registrados nos `MapBinder` com `@Named` correto

---

### Testes (`src/test/java/`, arquivos `*Test.java`)

**Antes criar/modificar:**
1. Leia `instructions/test-instructions.md` íntegra
2. Use agent `test-guard` passando caminho arquivo **fonte** correspondente
3. Confirme: arquivo teste espelha exatamente pacote classe sob teste
4. Confirme: ao menos um cenário sucesso e um erro por método público relevante

---

## Regras Transversais (Sempre)

Regras aplicam qualquer arquivo, sem exceção:

- **Java 8 estrito.** Nenhuma API pós Java 8 (`var`, `List.of`, `Map.of`, `String.isBlank`, `Files.readString`, `Optional.ifPresentOrElse`)
- **Sem `var`.** Tipagem explícita sempre
- **Lombok extensivamente:** `@Data`, `@Builder`, `@AllArgsConstructor`, `@NoArgsConstructor`, `@Log`
- **Logging:** `java.util.logging` via `@Log` Lombok. Nunca SLF4J, nunca `System.out`
- **Sem `new` pra dependências:** `@Inject` via construtor
- **Sem exceção genérica:** `throw new RuntimeException(...)` → exceção tipada de `domain/exception/`
- **Sem implementação manual mapper:** usar MapStruct
- **Sem `HttpClient` nativo:** usar Retrofit via `RetrofitCallExecutor`

---

## Quando Parar e Perguntar

Pare implementação e pergunte dev quando:

- Interface domínio necessária não existe ainda
- Não claro em qual camada tipo deve ser definido
- Mudança `core/domain/` parece necessária pra acomodar plataforma
- UseCase existente precisaria ser modificado pra suportar nova plataforma
- Ambiguidade sobre qual Port ou Gateway já existe no projeto

**Nunca assuma. Pergunte.**

---

## Checklist de Entrega

Antes concluir implementação:

- [ ] Protocolo tipo artefato seguido
- [ ] `core/domain/` sem imports infraestrutura
- [ ] UseCases sem nome plataforma e não importam `infrastructure/integration/`
- [ ] Gateways concretos implementam interface `domain/gateway/`
- [ ] DTOs externos não saíram de `infrastructure/integration/<plataforma>/dto/`
- [ ] Todos novos Adapters pipeline registrados no módulo Guice
- [ ] Java 8 estrito (sem APIs pós-Java 8)
- [ ] Sem `var`, sem SLF4J, sem `new` pra dependências
- [ ] Build/testes executados quando aplicável
- [ ] Branch sincronizado com `origin/develop` antes iniciar implementação

---

## Fonte de Instruções Técnicas

| Arquivo | Quando ler |
|:--------|:-----------|
| `architecture-instructions.md` | Toda alteração arquitetural, nova plataforma, dúvida camadas |
| `backend-instructions.md` | Toda alteração backend |
| `domain-guard.md` | Qualquer arquivo em `core/domain/` |
| `usecase-guard.md` | Qualquer arquivo em `core/application/usecase/` |
| `new-platform-checklist.md` | Nova plataforma integração |
| `controller-instructions.md` | Controllers REST |
| `repository-instructions.md` | Repositórios `@Repository`, `@Criteria`, `@NativeQuery` |
| `entity-instructions.md` | Entidades Java |
| `dependency-injection-instructions.md` | Módulos Guice, bindings |
| `mapstruct-instructions.md` | Mappers MapStruct |
| `test-instructions.md` | Testes JUnit + Mockito |
| `build-instructions.md` | Build e deploy |

Conflito entre arquivos: aplique regra mais restritiva.