# agent-instructions.md

## Proposito

Arquivo = fonte unica de governanca para agentes IA neste repo.
`AGENTS.md`, `CLAUDE.md`, `.github/copilot-instructions.md` = thin wrappers delegando pra este doc.

---

## Principio de Operacao

**Antes de qualquer implementacao, identifique tipo de artefato a criar/modificar e siga protocolo correspondente.**

Nao leia instrucoes como lista de recomendacoes. Leia como pre-condicoes obrigatorias. Pre-requisito nao satisfeito -> pergunte ao dev antes de prosseguir.

---

## Pre-requisito Universal

**Antes de escrever codigo:**

1. Execute `git fetch origin develop`
2. Execute `git merge origin/develop`
3. Conflitos: **PARE** e pergunte ao dev como resolver antes de continuar

---

## Arquitetura

Projeto segue **MVC + package by feature**. Toda feature concentra seus artefatos em `br/com/hagious/qualitymanager/<feature>/` com subpacotes `controller/`, `service/`, `repository/`, `entity/`, `vo/`, `dto/`, `mapper/`, `listener/`, `callback/`, `exception/`.

Detalhes em `architecture-instructions.md`.

---

## Protocolos por Tipo de Artefato

### Nova Feature

**Antes de criar arquivos:**
1. Leia `architecture-instructions.md` (visao geral, estrutura de pacotes, checklist)
2. Crie pacote raiz `br/com/hagious/qualitymanager/<feature>/`
3. Siga ordem: Entity -> Repository -> Service -> Controller (+ DTO + Mapper)
4. Pergunte ao dev se a feature precisa de Listener ou Callback

### Entity (`<feature>/entity/`)

**Antes de criar/modificar:**
1. Leia `entity-instructions.md`
2. Confirme: nenhuma anotacao de serializacao externa (Jackson, Gson)
3. Confirme: metadata UI no XML do dicionario, nao na entidade Java
4. Entidade pode ter metodos de dominio simples sobre o proprio estado

**Consistencia obrigatoria:**
- Campo **adicionado** -> gerar script `ADD COLUMN` em `dbscripts/` e atualizar dicionario XML
- Campo **removido** -> gerar script (se aplicavel) e remover do dicionario XML
- Campo **renomeado** -> atualizar script e dicionario
- Dicionario XML ou script alterado -> verificar se entidade Java reflete a mudanca

Nunca deixe entidade Java e dicionario fora de sincronia.

### Repository (`<feature>/repository/`)

**Antes de criar/modificar:**
1. Leia `repository-instructions.md`
2. Confirme: interface anotada com `@Repository`
3. Confirme: `JapeRepository<TipoID, TipoEntity>` - ID primeiro parametro
4. Confirme: toda clause `@Criteria` usa prefixo `this.` nos campos
5. Confirme: interfaces `@NativeQuery.Result` em `<feature>/vo/`

### Service (`<feature>/service/`)

**Antes de criar/modificar:**
1. Confirme: classe anotada com `@Component`
2. Confirme: dependencias injetadas via construtor com `@Inject`
3. Confirme: regra de negocio centralizada aqui, nunca em Controller/Listener/Callback
4. Confirme: nao depende de DTO nem Mapper
5. Confirme: lanca excecao tipada (em `<feature>/exception/` ou `shared/exception/`) - nunca `RuntimeException` generica

### Controller (`<feature>/controller/`)

**Antes de criar/modificar:**
1. Leia `controller-instructions.md`
2. Confirme: nenhuma logica de negocio inline - delega para Service
3. Confirme: nenhum acesso direto a Repository - sempre via Service
4. Confirme: `serviceName` termina com sufixo `SP`
5. Confirme: usa `@Transactional` em metodos de escrita
6. Confirme: parametros DTO anotados com `@Valid`

### DTO (`<feature>/dto/`)

**Antes de criar/modificar:**
1. Confirme: usa `@Data` do Lombok
2. Request com validacao `javax.validation` (`@NotNull`, `@NotBlank`, etc.)
3. Response sem validacao
4. Sem logica - so dados

### Mapper (`<feature>/mapper/`)

**Antes de criar/modificar:**
1. Leia `mapstruct-instructions.md`
2. Confirme: nao declarar `componentModel` no `@Mapper` (ja global)
3. Mapper com `uses` ou `abstract class` com `@Inject`: declarar `injectionStrategy = InjectionStrategy.CONSTRUCTOR`

### Listener (`<feature>/listener/`)

**Antes de criar/modificar:**
1. Leia `architecture-instructions.md` secao 3.7
2. Confirme: estende `PersistenceEventAdapter`
3. Confirme: delega regra de negocio ao Service - listener so filtra e delega
4. Confirme: usa guard clauses para sair cedo quando evento for irrelevante

### Callback (`<feature>/callback/`)

**Antes de criar/modificar:**
1. Leia `architecture-instructions.md` secao 3.8
2. Confirme: implementa `ICustomCallBack`
3. Confirme: delega regra de negocio ao Service

### Injecao de Dependencia / Modulos Guice

**Antes de criar/modificar:**
1. Leia `dependency-injection-instructions.md`
2. Confirme: `@Inject` via construtor em todas as classes gerenciadas
3. Confirme: estereotipo correto (`@Controller`, `@Repository`, `@Component`, `@CustomModule`, `@ControllerAdvice`)

### Testes (`src/test/java/`, arquivos `*Test.java`)

**Antes de criar/modificar:**
1. Leia `test-instructions.md` integra
2. Confirme: arquivo de teste espelha exatamente o pacote da classe sob teste
3. Confirme: ao menos um cenario de sucesso e um cenario de erro por metodo publico relevante

---

## Regras Transversais (Sempre)

Regras que se aplicam a qualquer arquivo, sem excecao:

- **Java 8 estrito.** Nenhuma API pos Java 8 (`var`, `List.of`, `Map.of`, `String.isBlank`, `Files.readString`, `Optional.ifPresentOrElse`)
- **Sem `var`.** Tipagem explicita sempre
- **Lombok extensivamente:** `@Data`, `@Builder`, `@AllArgsConstructor`, `@NoArgsConstructor`, `@Log`
- **Logging:** `java.util.logging` via `@Log` Lombok. Nunca SLF4J, nunca `System.out`
- **Sem `new` para dependencias:** `@Inject` via construtor
- **Sem excecao generica:** `throw new RuntimeException(...)` -> excecao tipada
- **Sem implementacao manual de mapper:** usar MapStruct
- **Package by feature:** todo artefato dentro do pacote da sua feature; compartilhamento apenas via `shared/`

---

## Quando Parar e Perguntar

Pare a implementacao e pergunte ao dev quando:

- Nao estiver claro a qual feature o novo artefato pertence
- Mudanca em uma feature parecer afetar outra (potencial extracao para `shared/`)
- Houver duvida sobre nome de tabela ou de feature
- Service existente parecer precisar ser dividido para acomodar nova regra

**Nunca assuma. Pergunte.**

---

## Checklist de Entrega

Antes de concluir uma implementacao:

- [ ] Protocolo do tipo de artefato seguido
- [ ] Artefatos no pacote correto da feature
- [ ] Controllers sem regra de negocio (delegam para Service)
- [ ] Services centralizam regra de negocio (nao dependem de DTO/Mapper)
- [ ] Repositories anotados com `@Repository` e estendendo `JapeRepository`
- [ ] DTOs com `@Data` e validacao quando Request
- [ ] Mappers MapStruct, nunca manuais
- [ ] Java 8 estrito (sem APIs pos-Java 8)
- [ ] Sem `var`, sem SLF4J, sem `new` para dependencias
- [ ] Build/testes executados quando aplicavel
- [ ] Branch sincronizado com `origin/develop` antes de iniciar a implementacao

---

## Fonte de Instrucoes Tecnicas

| Arquivo | Quando ler |
|:--------|:-----------|
| `architecture-instructions.md` | Toda alteracao arquitetural, nova feature, duvida sobre camadas |
| `backend-instructions.md` | Toda alteracao backend |
| `controller-instructions.md` | Controllers REST |
| `repository-instructions.md` | Repositorios `@Repository`, `@Criteria`, `@NativeQuery` |
| `entity-instructions.md` | Entidades Java |
| `dependency-injection-instructions.md` | Modulos Guice, bindings |
| `mapstruct-instructions.md` | Mappers MapStruct |
| `test-instructions.md` | Testes JUnit + Mockito |
| `database-instructions.md` | Scripts de banco em `dbscripts/` |
| `datadictionary-instructions.md` | Dicionario de dados em `datadictionary/` |
| `build-instructions.md` | Build e deploy |

Conflito entre arquivos: aplique a regra mais restritiva.
