---
applyTo: "**/*Mapper.java"
---

# MapStruct - Addon Studio 2.0

MapStruct = biblioteca padrao para conversao DTO <-> Entity. **Nunca** faca mapper manual - use MapStruct.

> **Referencia complementar:** veja `dependency-injection-instructions.md` para como mappers se registram no container Guice.
>
> **Pacote:** todo mapper REST de uma feature vive em `br/com/hagious/qualitymanager/<feature>/mapper/`. Mappers auxiliares compartilhados ficam em `shared/mapper/`.

---

## 1. Configuracao Global do Projeto

Projeto ja define flags globais de compilacao no `build.gradle`:

```groovy
dependencies {
    implementation 'org.mapstruct:mapstruct:1.5.5.Final'
    annotationProcessor 'org.mapstruct:mapstruct-processor:1.5.5.Final'

    compileOnly 'org.projectlombok:lombok:1.18.30'
    annotationProcessor 'org.projectlombok:lombok:1.18.30'
    annotationProcessor 'org.projectlombok:lombok-mapstruct-binding:0.2.0'
}

tasks.withType(JavaCompile) {
    options.compilerArgs += [
        "-Amapstruct.defaultComponentModel=jakarta",
        "-Amapstruct.unmappedTargetPolicy=IGNORE"
    ]
}
```

### O que cada flag faz

| Flag | Valor | Efeito |
|:-----|:------|:-------|
| `defaultComponentModel` | `jakarta` | MapStruct gera implementacoes com `@Named` para registro automatico no Guice. **NAO sobrescrever** nos mappers. |
| `unmappedTargetPolicy` | `IGNORE` | Campos target sem mapeamento explicito sao ignorados sem erro de compilacao. |
| `lombok-mapstruct-binding` | `0.2.0` | Garante compatibilidade Lombok (getters/setters gerados) com MapStruct (annotation processor). |

> **ATENCAO - Nao confunda `componentModel` com `injectionStrategy`:**
>
> | Parametro | Configurado globalmente? | Regra |
> |:----------|:-------------------------|:------|
> | `componentModel` | **SIM** - `jakarta` no `build.gradle` | **NUNCA** declare no `@Mapper` individual. Declarar sobrescreve o global. |
> | `injectionStrategy` | **NAO** - sem flag global | **SEMPRE** declare `InjectionStrategy.CONSTRUCTOR` em mapper que use `uses = {...}` ou que seja `abstract class` com `@Inject`. |
>
> **componentModel:** **NUNCA** declare no `@Mapper` individual. Ja e global como `jakarta` em `build.gradle`. Declarar - mesmo com mesmo valor `"jakarta"` - sobrescreve o global e causa conflitos.
>
> **injectionStrategy:** declare explicito como `InjectionStrategy.CONSTRUCTOR` em mapper que use `uses = {...}` ou seja `abstract class` com `@Inject`.

---

## 2. Tipos de Mapper

Tres configuracoes de mapper relevantes para MVC:

| Tipo | `@Mapper(...)` | Classe | Quando usar |
|:-----|:---------------|:-------|:------------|
| **Simples** | `@Mapper` | `interface` | Mappers REST sem deps externas |
| **Com `uses`** | `@Mapper(uses = {...}, injectionStrategy = CONSTRUCTOR)` | `interface` | Mappers que usam classes auxiliares `@Component` (ex: normalizadores) |
| **Com `uses` + builder desabilitado** | `@Mapper(uses = {...}, injectionStrategy = CONSTRUCTOR, builder = @Builder(disableBuilder = true))` | `abstract class` | Mappers com `@AfterMapping` ou logica custom em metodos concretos |

---

## 3. Mapper Simples (`interface`)

Conversoes diretas sem deps externas. Cobre 90% dos casos REST.

```java
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RegistroMapper {

    Registro toEntity(CriarRegistroRequest dto);

    @Mapping(source = "codRegistro", target = "id")
    RegistroResponse toResponse(Registro entity);
}
```

**Caracteristicas:**
- `interface`.
- So `@Mapper` (sem params).
- Injetavel direto via `@Inject` no construtor.

---

## 4. Mapper com `uses` (`interface` + `@Component`)

Mapper precisa de classe auxiliar para transformacoes custom (ex: normalizar strings, parsear datas).

### Classe auxiliar (`@Component`)

```java
package br.com.hagious.qualitymanager.shared.mapper;

import br.com.sankhya.studio.stereotypes.Component;

@Component
public class StringMappingNormalizer {

    public String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
```

### Mapper usando classe auxiliar

```java
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    uses = {StringMappingNormalizer.class},
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface RegistroMapper {

    @Mapping(source = "nome", target = "descricao")
    @Mapping(target = "ativo", constant = "true")
    Registro toEntity(CriarRegistroRequest dto);
}
```

**Regras obrigatorias:**
- Classe em `uses` **deve** ser `@Component` (para o Guice resolver).
- `injectionStrategy = InjectionStrategy.CONSTRUCTOR` **obrigatorio** com `uses`.
- MapStruct aplica os metodos auto quando os tipos batem (ex: `String -> String` usa `normalize`).

---

## 5. Mapper com Builder Desabilitado (`abstract class`)

Use quando o mapper precisa de `@AfterMapping`, logica custom em metodos concretos, ou quando o target usa `@Builder` (Lombok) e voce precisa que o MapStruct use setters.

```java
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
    uses = {StringMappingNormalizer.class},
    injectionStrategy = org.mapstruct.InjectionStrategy.CONSTRUCTOR,
    builder = @org.mapstruct.Builder(disableBuilder = true)
)
public abstract class RegistroMapper {

    @Mapping(source = "nome", target = "descricao")
    public abstract Registro toEntity(CriarRegistroRequest dto);

    @AfterMapping
    protected void posMapeamento(@MappingTarget Registro entity) {
        if (entity.getDescricao() != null) {
            entity.setDescricao(entity.getDescricao().toUpperCase());
        }
    }
}
```

**Quando usar:**
- Target tem `@Builder` (Lombok) e MapStruct deve usar setters em vez do Builder.
- Precisa de `@AfterMapping` para pos-processamento.
- Classe tem que ser `abstract class` (nao `interface`) para ter metodos concretos.

---

## 6. Padroes de Mapeamento

### 6.1 Campos com nomes diferentes

```java
@Mapping(source = "nomeRequest", target = "descricao")
Registro toEntity(CriarRegistroRequest dto);
```

### 6.2 Campos nested (objetos aninhados)

Mapear campo flat do DTO para campo dentro de objeto aninhado da entity:

```java
@Mapping(source = "codParceiro", target = "parceiro.codParceiro")
Registro toEntity(CriarRegistroRequest dto);
```

Tambem le de nested:

```java
@Mapping(source = "parceiro.nome", target = "nomeParceiro")
RegistroResponse toResponse(Registro entity);
```

### 6.3 Ignorar campos target

```java
@Mapping(target = "codRegistro", ignore = true)
@Mapping(target = "dhCreate", ignore = true)
Registro toEntity(CriarRegistroRequest dto);
```

> Com `unmappedTargetPolicy=IGNORE` global, campos nao mapeados ja sao ignorados. Use `ignore = true` quando quiser ser **explicito** sobre a intencao (ex: ignorar PK na criacao).

### 6.4 Valores constantes

```java
@Mapping(target = "ativo", constant = "true")
Registro toEntity(CriarRegistroRequest dto);
```

> Valor sempre como `String`. MapStruct converte para o tipo target automaticamente (`"true"` -> `Boolean.TRUE`).

### 6.5 Mapeamento bidirecional (toEntity + toResponse)

```java
@Mapper
public interface RegistroMapper {

    // Request -> Entity
    @Mapping(source = "nome", target = "descricao")
    @Mapping(target = "codRegistro", ignore = true)
    Registro toEntity(CriarRegistroRequest dto);

    // Entity -> Response
    @Mapping(source = "descricao", target = "nome")
    @Mapping(source = "codRegistro", target = "id")
    RegistroResponse toResponse(Registro entity);
}
```

### 6.6 Campos com mesmo nome (mapeamento implicito)

Source e target com mesmo nome e tipo = mapeamento **automatico**. Sem `@Mapping` necessario.

```java
// Se Request.descricao e Entity.descricao existem com o mesmo tipo:
// NAO precisa: @Mapping(source = "descricao", target = "descricao")
Registro toEntity(CriarRegistroRequest dto);
```

> `@Mapping` explicito para campos de mesmo nome e **permitido** para documentar, mas nao obrigatorio.

### 6.7 Update de entity existente (`@MappingTarget`)

Para atualizar uma entidade ja persistida sem criar nova instancia:

```java
@Mapper
public interface RegistroMapper {

    @Mapping(source = "nome", target = "descricao")
    @Mapping(target = "codRegistro", ignore = true)
    void atualizar(@MappingTarget Registro existente, AtualizarRegistroRequest dto);
}
```

Uso no Service:

```java
public Registro atualizar(Integer codRegistro, AtualizarRegistroRequest dto) throws Exception {
    Registro existente = repository.findByPK(codRegistro);
    mapper.atualizar(existente, dto);
    return repository.save(existente);
}
```

---

## 7. Organizacao de Pacotes

| Tipo de Mapper | Pacote | Convencao de nome |
|:---------------|:-------|:------------------|
| Mapper REST da feature | `<feature>/mapper/` | `<Feature>Mapper` |
| Classe auxiliar compartilhada | `shared/mapper/` | Nome descritivo (ex: `StringMappingNormalizer`) |

### Exemplos de nomes

```
br/com/hagious/qualitymanager/registro/mapper/RegistroMapper.java
br/com/hagious/qualitymanager/fornecedor/mapper/FornecedorMapper.java
br/com/hagious/qualitymanager/shared/mapper/StringMappingNormalizer.java
```

---

## 8. Convencao de Metodos

| Direcao | Nome do metodo | Assinatura |
|:--------|:---------------|:-----------|
| Request -> Entity | `toEntity` ou `to<Entity>` | `Entity toEntity(Request dto)` |
| Entity -> Response | `toResponse` ou `to<Response>` | `Response toResponse(Entity entity)` |
| Update Entity | `atualizar` | `void atualizar(@MappingTarget Entity ent, Request dto)` |

### Exemplos

```java
// REST: Request -> Entity, Entity -> Response
Registro toEntity(CriarRegistroRequest dto);
RegistroResponse toResponse(Registro entity);
void atualizar(@MappingTarget Registro existente, AtualizarRegistroRequest dto);
```

---

## 9. Injecao e Uso

### No Controller (via construtor)

```java
@Controller(serviceName = "RegistroControllerSP", transactionType = EJBTransactionType.Supports)
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

> Mappers sao injetados como qualquer outra dep - `@Inject` via construtor. Guice resolve a implementacao gerada automaticamente.

---

## 10. Fluxo de Decisao: Qual tipo de Mapper usar?

```
Precisa de @AfterMapping ou logica custom em metodo concreto?
|
+-- SIM --> abstract class
|           @Mapper(uses={...}, injectionStrategy=CONSTRUCTOR, builder=@Builder(disableBuilder=true))
|
+-- NAO --> Precisa de classe auxiliar em uses?
            |
            +-- SIM --> @Mapper(uses={...}, injectionStrategy=CONSTRUCTOR) [interface]
            |
            +-- NAO --> @Mapper [interface simples]
```

---

## 11. Checklist

### Novo mapper

1. [ ] Identificar tipo correto (simples, com `uses`, ou com builder desabilitado).
2. [ ] Declarar `interface` (ou `abstract class` se precisar `@AfterMapping` ou builder desabilitado).
3. [ ] **NAO** declarar `componentModel` - ja global como `jakarta` em `build.gradle`. Nem com valor `"jakarta"`.
4. [ ] Se usar `uses` ou for `abstract class`, declarar `injectionStrategy = InjectionStrategy.CONSTRUCTOR`.
5. [ ] Se usar `uses`, garantir que classes referenciadas sao `@Component`.
6. [ ] Mapear campos de nomes diferentes via `@Mapping(source, target)`.
7. [ ] Usar `target = "nested.field"` para objetos aninhados.
8. [ ] Usar `ignore = true` para campos nao mapeaveis (incluindo PK na criacao).
9. [ ] Usar `constant = "valor"` para valores fixos.
10. [ ] Pacote correto (`<feature>/mapper/`).
11. [ ] Injetar via construtor com `@Inject`.

### Revisao de mapper existente

1. [ ] Verificar se campos source estao mapeados no target (ou ignorados explicito).
2. [ ] Verificar campos nested (`a.b.c`) corretos.
3. [ ] Verificar que `uses` tem `injectionStrategy = CONSTRUCTOR`.
4. [ ] Verificar que NAO tem `componentModel` no `@Mapper` (nem `"jakarta"` - ja global).

---

## 12. Erros Comuns

| Erro | Correcao |
|:-----|:---------|
| Declarar `componentModel` no `@Mapper` (qualquer valor, incluindo `"jakarta"`) | Remover - ja global como `jakarta` em `build.gradle`. |
| Usar `uses` sem `injectionStrategy = CONSTRUCTOR` | Adicionar `injectionStrategy = InjectionStrategy.CONSTRUCTOR`. |
| Classe em `uses` sem `@Component` | Adicionar `@Component` na auxiliar. |
| `@AfterMapping` nao executado | Adicionar `builder = @Builder(disableBuilder = true)` no `@Mapper` e usar `abstract class`. |
| Mapper `abstract class` sem `builder = @Builder(disableBuilder = true)` | Adicionar atributo - sem ele MapStruct pode usar Builder Lombok e ignorar setters. |
| Mapper manual (`new DTO(); dto.setX(...)`) | Usar MapStruct. Nunca mapper manual. |
| Campo target nao populado | Verificar `@Mapping`. Mesmo nome = automatico; diferentes precisam `@Mapping` explicito. |
| Erro de compilacao "Ambiguous mapping methods" | Renomear metodos para evitar conflitos de assinatura ou usar `@Named` para qualificar. |
| Erro de compilacao com Lombok | Verificar `lombok-mapstruct-binding` nas deps do `annotationProcessor`. |
| Mapper fora do pacote `<feature>/mapper/` | Mover para o pacote correto da feature. |
