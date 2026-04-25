---
applyTo: "**/*.java"
---

# Entidade Java (@JapeEntity) - Addon Studio 2.0

Entidade Java = representacao de tabela do banco. **Limpa** - contem so o mapeamento estrutural minimo. Metadata UI, tipos, descricoes e comportamento de UI ficam no **Dicionario de Dados** (XMLs em `datadictionary/`).

> **Referencia complementar:** consulte `datadictionary-instructions.md` para criar XML correspondente a entidade.

---

## 1. Regras Fundamentais

| Regra | Detalhe |
|:------|:--------|
| `@Column` so tem `name` | `@Column(name = "COLUNA")` - nenhum outro atributo. |
| `@JoinColumn` so tem `name` e `referencedColumnName` | `@JoinColumn(name = "...", referencedColumnName = "...")` |
| `@JapeEntity` so tem `entity` e `table` | `@JapeEntity(entity = "...", table = "...")` |
| Sem `@Expression` | Expressoes ficam no XML (`<expression>`). |
| Sem `@GeneratedValue` | Sequencia fica no XML (`sequenceType`/`sequenceField`). |
| Sem `@Option` / `@Property` | Opcoes ficam no XML (`<fieldOptions>`). |
| Lombok obrigatorio | `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`. |
| Prefixo `Thg` obrigatorio no `entity` | Ex.: `@JapeEntity(entity = "ThgRegistro", ...)` |

---

## 2. Organizacao de Pacotes (Package by Feature)

Entidades vivem dentro do pacote da feature, nao em pacote de dominio global:

```
br/com/hagious/qualitymanager/<feature>/
|-- entity/       - Entidades (@JapeEntity) e classes de dominio puro
|-- vo/           - Value Objects: @Embeddable (PKs compostas) e Enums
|-- repository/   - Interfaces de repositorio
|-- service/      - Service da feature
|-- ...
```

| Tipo de classe | Pacote | Exemplo |
|:---------------|:-------|:--------|
| Entidade persistida (`@JapeEntity`) | `<feature>/entity/` | `Registro.java` |
| PK composta (`@Embeddable`) | `<feature>/vo/` | `RegistroItemId.java` |
| Enum de dominio | `<feature>/vo/` | `StatusEnum.java` |
| Classe de dominio puro (sem persistencia) | `<feature>/entity/` | `ResultadoConsolidado.java` |

---

## 3. Tipos de Classe

### 3.1 Entidade Persistida (`@JapeEntity`)

Representa tabela no banco. Tem `@JapeEntity`, `@Id`, `@Column`, opcionalmente relacionamentos.

```java
@JapeEntity(entity = "ThgRegistro", table = "THGQMGREGISTRO")
public class Registro { ... }
```

### 3.2 PK Composta (`@Embeddable`)

Tabela com PK composta -> classe separada anotada com `@Embeddable`.

```java
@Embeddable
public class RegistroItemId { ... }
```

### 3.3 Enum (Value Object)

Valores finitos de dominio (listas de opcoes). Usados como tipo de campo em entidades. Tem `value` = valor armazenado no banco.

**Regras obrigatorias:**

| Regra | Detalhe |
|:------|:--------|
| `@Getter` (Lombok) | Gera o getter de `value` automaticamente. |
| `@AllArgsConstructor` (Lombok) | Gera o construtor que recebe `value`. |
| Campo `private final String value` | Valor persistido no banco (codigo curto). |
| Pacote `<feature>/vo/` | Todo enum de dominio fica no pacote `vo/` da feature. |
| Sufixo `Enum` | Nome da classe sempre termina com `Enum` (ex: `StatusEnum`). |

**Anatomia completa:**

```java
package br.com.hagious.qualitymanager.<feature>.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {
    ATIVO("A"),
    INATIVO("I");

    private final String value;
}
```

### 3.4 Classe de Dominio Puro

Classes que representam conceitos de dominio **sem persistencia direta**. Sem `@JapeEntity`. Contem dados consolidados ou resultados de operacao.

```java
public class ResultadoConsolidado { ... }
public class DadosImportacao { ... }
```

---

## 4. Anatomia de uma Entidade

```java
package br.com.hagious.qualitymanager.<feature>.entity;        // 1. Pacote da feature

import br.com.sankhya.studio.persistence.*;                    // 2. Imports de persistencia
import lombok.*;                                                // 3. Imports Lombok

@Data                                                           // 4. Lombok obrigatorio
@NoArgsConstructor
@AllArgsConstructor
@JapeEntity(                                                    // 5. Mapeamento: somente entity + table
    entity = "ThgRegistro",
    table = "THGQMGREGISTRO"
)
public class Registro {                                         // 6. Classe plana (sem heranca de metadata)

    @Id                                                         // 7. Chave primaria
    @Column(name = "CODREGISTRO")
    private Integer codRegistro;

    @Column(name = "DESCR")                                     // 8. Campos: somente name
    private String descricao;

    @OneToMany(...)                                             // 9. Relacionamentos (se houver)
    private List<RegistroItem> itens;

    public void inativar() { ... }                              // 10. Metodos de dominio (se houver)
}
```

---

## 5. Anotacoes Permitidas

### Anotacoes que DEVEM ser usadas

| Anotacao | Uso | Pacote |
|:---------|:----|:-------|
| `@JapeEntity(entity, table)` | Toda entidade persistida | `br.com.sankhya.studio.persistence` |
| `@Id` | Campo(s) de chave primaria | `br.com.sankhya.studio.persistence` |
| `@Column(name)` | Todo campo persistido | `br.com.sankhya.studio.persistence` |
| `@Embeddable` | Classe de PK composta | `br.com.sankhya.studio.persistence` |
| `@Data` | Getter/Setter/ToString/Equals/Hash | `lombok` |
| `@NoArgsConstructor` | Construtor vazio (obrigatorio para o framework) | `lombok` |
| `@AllArgsConstructor` | Construtor com todos os campos | `lombok` |

### Anotacoes opcionais (usar quando necessario)

| Anotacao | Quando usar | Pacote |
|:---------|:------------|:-------|
| `@Builder` | Quando a entidade e construida programaticamente | `lombok` |
| `@OneToMany` | Relacionamento pai -> filhos | `br.com.sankhya.studio.persistence` |
| `@OneToOne` | Navegacao para entidade referenciada | `br.com.sankhya.studio.persistence` |
| `@ManyToOne` | Navegacao inversa filho -> pai | `br.com.sankhya.studio.persistence` |
| `@JoinColumn(name, referencedColumnName)` | Junto com `@OneToOne` / `@ManyToOne` | `br.com.sankhya.studio.persistence` |
| `@JoinColumns` | Multiplos `@JoinColumn` (FK composta) | `br.com.sankhya.studio.persistence` |
| `@Cascade` | Dentro de `@OneToMany` para cascatear operacoes | `br.com.sankhya.studio.persistence` |
| `@Relationship` | Dentro de `@OneToMany` para definir campos do vinculo | `br.com.sankhya.studio.persistence` |
| `@SuperBuilder` | Quando a entidade herda de classe base | `lombok.experimental` |
| `@EqualsAndHashCode(callSuper = true)` | Junto com heranca + `@SuperBuilder` | `lombok` |

### Anotacoes PROIBIDAS na entidade

| Anotacao | Onde fica | Motivo |
|:---------|:----------|:-------|
| `@Expression` | XML `<expression>` | Metadata do framework |
| `@GeneratedValue` | XML `sequenceType`/`sequenceField` | Metadata do framework |
| `@Option` | XML `<fieldOptions>` | Metadata de UI |
| `@Property` | XML | Metadata do framework |

---

## 6. Chave Primaria (PK)

### 6.1 PK Simples

Tabela com unica coluna como PK:

```java
@Id
@Column(name = "CODREGISTRO")
private Integer codRegistro;
```

> PK sequencial: nao use coluna com prefixo `ID`. Use `COD*` (cadastros) ou `NU*` (movimentos/documentos).

### 6.2 PK Composta (`@Embeddable`)

Tabela com PK composta -> crie classe separada no pacote `vo/` da feature:

**Classe `@Embeddable` (em `<feature>/vo/`):**

```java
package br.com.hagious.qualitymanager.<feature>.vo;

import br.com.sankhya.studio.persistence.Column;
import br.com.sankhya.studio.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class RegistroItemId {

    @Column(name = "CODREGISTRO")
    private Integer codRegistro;

    @Column(name = "NUITEM")
    private Integer nuItem;
}
```

**Uso na entidade:**

```java
@Id
private RegistroItemId embeddedId;
```

### Convencoes da PK composta

| Regra | Detalhe |
|:------|:--------|
| Nome da classe | `<NomeEntidade>Id` (ex: `RegistroItemId`) |
| Pacote | `<feature>/vo/` |
| Anotacoes | `@Data`, `@AllArgsConstructor`, `@NoArgsConstructor`, `@Embeddable` |
| Campos | Cada campo com `@Column(name = "...")` - somente `name` |
| Na entidade | Campo anotado apenas com `@Id` (sem `@Column`) |

### Metodos auxiliares na entidade (opcional)

Facilitar acesso aos campos da PK composta -> crie metodos delegadores:

```java
public Integer getCodRegistro() {
    return this.embeddedId.getCodRegistro();
}

public Integer getNuItem() {
    return this.embeddedId.getNuItem();
}
```

---

## 7. Campos (@Column)

### Regra unica

```java
@Column(name = "NOME_COLUNA")
private TipoJava nomeCampo;
```

Nenhum outro atributo. Ponto.

### Mapeamento de tipos

| Tipo no banco / XML | Tipo Java sugerido |
|:--------------------|:-------------------|
| `INTEIRO` | `Integer` ou `BigDecimal` |
| `TEXTO` | `String` |
| `DECIMAL` | `BigDecimal` |
| `DATA_HORA` | `Timestamp` (`java.sql.Timestamp`) |
| `CHECKBOX` | `Boolean` |
| `PESQUISA` (FK) | `BigDecimal` ou `Integer` (tipo da FK) |
| Campo com opcoes (enum no banco) | Enum Java (veja secao 9) |

### Quando usar `Integer` vs `BigDecimal`

| Contexto | Tipo | Motivo |
|:---------|:-----|:-------|
| PKs de tabelas do add-on (`COD*`/`NU*`) | `Integer` | Sequenciais do add-on sao inteiros simples |
| PKs de tabelas nativas (NUNOTA, CODPARC, etc.) | `BigDecimal` | Padrao do Sankhya Om |
| Quantidades, valores monetarios | `BigDecimal` | Precisao decimal |
| Campos numericos simples | `BigDecimal` ou `Integer` | Conforme necessidade |

---

## 8. Relacionamentos

### 8.1 `@OneToMany` - Pai -> Filhos

Entidade com lista de filhos.

```java
@OneToMany(
    cascade = Cascade.ALL,
    relationship = {
        @Relationship(
            fromField = "CODREGISTRO",  // Campo da tabela PAI
            toField = "CODREGISTRO"     // Campo da tabela FILHA
        )
    }
)
private List<RegistroItem> itens;
```

| Atributo | Significado |
|:---------|:------------|
| `cascade` | `Cascade.ALL` para cascatear insert/update/delete |
| `fromField` | Nome da **coluna** na tabela pai |
| `toField` | Nome da **coluna** na tabela filha |

> Tipo do campo sempre `List<EntidadeFilha>`.

### 8.2 `@OneToOne` + `@JoinColumn` - Navegacao para entidade referenciada

Dominio precisa **navegar** para outra entidade (acessar campos dela).

```java
@OneToOne
@JoinColumn(name = "CODPARC", referencedColumnName = "CODPARC")
private Parceiro parceiro;
```

| Atributo | Significado |
|:---------|:------------|
| `name` | Coluna na **tabela atual** (FK) |
| `referencedColumnName` | Coluna na **tabela referenciada** (PK ou campo de vinculo) |

### 8.3 `@OneToOne` + `@JoinColumns` - FK Composta

FK aponta para PK composta -> use `@JoinColumns`:

```java
@OneToOne
@JoinColumns({
    @JoinColumn(name = "CODTIPOPER", referencedColumnName = "CODTIPOPER"),
    @JoinColumn(name = "DHALTER", referencedColumnName = "DHTIPOPER")
})
private TipoOperacao tipoOperacao;
```

### 8.4 `@ManyToOne` + `@JoinColumn` - Filho -> Pai

Navegacao inversa: filho -> pai.

```java
@ManyToOne
@JoinColumn(name = "CODREGISTRO", referencedColumnName = "CODREGISTRO")
private Registro registro;
```

### Quando usar cada relacionamento

| Preciso de... | Uso |
|:--------------|:----|
| Lista de filhos na entidade pai | `@OneToMany` |
| Acessar campos de outra entidade (FK) | `@OneToOne` + `@JoinColumn` |
| Acessar o pai a partir do filho | `@ManyToOne` + `@JoinColumn` |
| Apenas armazenar a FK (sem navegar) | Somente `@Column(name = "FK")` - sem relacionamento |

> **Importante:** Se so precisar do valor da FK (ex: `codParceiro`), use apenas `@Column`. `@OneToOne`/`@JoinColumn` so quando o codigo precisa acessar campos da entidade referenciada.

---

## 9. Enums (Value Objects)

Enums = valores finitos armazenados no banco como texto curto (geralmente 1 caractere).

### Estrutura padrao

```java
package br.com.hagious.qualitymanager.<feature>.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusEnum {
    PENDENTE("P"),
    APROVADO("A"),
    CANCELADO("C");

    private final String value;
}
```

### Convencoes

| Regra | Detalhe |
|:------|:--------|
| Pacote | `<feature>/vo/` |
| Anotacoes | `@AllArgsConstructor`, `@Getter` |
| Campo `value` | `private final String value` - valor armazenado no banco |
| Nomes das constantes | `UPPER_SNAKE_CASE` descritivo |
| Valores | Strings curtas (1-2 caracteres, geralmente) |

### Uso na entidade

```java
@Column(name = "STATUS")
private StatusEnum status;
```

Framework converte automaticamente entre valor no banco (`"P"`) e enum Java (`StatusEnum.PENDENTE`).

---

## 10. Classes de Dominio Puro

Classes = conceitos de negocio sem mapeamento direto para tabela.

### Caracteristicas

- **Sem** `@JapeEntity`, `@Id`, `@Column`.
- Ficam no pacote `<feature>/entity/` (convencao do projeto).
- Usam so `@Data` do Lombok (ou construtor manual).
- Podem ter factory methods (`create(...)`) com validacoes.

### Exemplo simples

```java
@Data
public class ResultadoConsolidado {

    private BigDecimal total;
    private String mensagem;
}
```

### Exemplo com factory method e validacao

```java
@Data
public class DadosImportacao {

    private RegistroCabecalho cabecalho;
    private List<RegistroItem> itens;

    private DadosImportacao(RegistroCabecalho cabecalho, List<RegistroItem> itens) {
        this.cabecalho = cabecalho;
        this.itens = itens;
    }

    public static DadosImportacao create(RegistroCabecalho cabecalho, List<RegistroItem> itens) {
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("Deve conter ao menos um item.");
        }
        return new DadosImportacao(cabecalho, itens);
    }
}
```

---

## 11. Metodos de Dominio

Entidades podem (e devem) conter **logica simples** relacionada ao proprio estado.

### Regras

- Metodos operam sobre campos da propria entidade.
- Nomes expressam intencao de negocio (nao tecnica).
- Nao acessam banco, services, repositories - so os proprios dados.

### Exemplos

```java
// Consulta de estado
public Boolean isAtivo() {
    return Boolean.TRUE.equals(this.ativo);
}

// Mutacao de estado
public void inativar() {
    this.ativo = Boolean.FALSE;
}

public void cancelar(Timestamp dataCancelamento) {
    this.dhCancelamento = dataCancelamento;
    this.status = StatusEnum.CANCELADO;
}
```

---

## 12. Passo a Passo: Criando uma Entidade do Zero

### Cenario: criar nova entidade `Fornecedor` na feature `fornecedor`

Tabela `THGQMGFORNECEDOR`, PK simples `CODFORN` (auto), campos `NOME`, `CNPJ`, `ATIVO`, com vinculo para `Parceiro`.

---

### Passo 1 - Criar o XML do dicionario de dados

Arquivo `datadictionary/THGQMGFORNECEDOR.xml`:

```xml
<?xml version="1.0" encoding="ISO-8859-1" ?>
<metadados xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:noNamespaceSchemaLocation="../.gradle/metadados.xsd">

    <table name="THGQMGFORNECEDOR" sequenceType="A" sequenceField="CODFORN">
        <description>Fornecedores</description>
        <primaryKey>
            <field name="CODFORN"/>
        </primaryKey>
        <instances>
            <instance name="ThgFornecedor">
                <description>Fornecedores</description>
            </instance>
        </instances>
        <fields>
            <field name="CODFORN" dataType="INTEIRO" readOnly="S" order="1" allowSearch="S" visibleOnSearch="S">
                <description>Codigo Fornecedor</description>
            </field>
            <field name="NOME" dataType="TEXTO" size="200" isPresentation="S" required="S"
                   allowSearch="S" visibleOnSearch="S" UITabName="__main" order="2">
                <description>Nome</description>
            </field>
            <field name="CNPJ" dataType="TEXTO" size="20" allowSearch="S" visibleOnSearch="S"
                   UITabName="__main" order="3">
                <description>CNPJ</description>
            </field>
            <field name="CODPARC" dataType="PESQUISA" targetInstance="Parceiro" targetField="CODPARC"
                   targetType="INTEIRO" allowSearch="S" visibleOnSearch="S" UITabName="__main" order="4">
                <description>Cod. Parceiro</description>
            </field>
            <field name="ATIVO" dataType="CHECKBOX" UITabName="__main" order="99" allowSearch="N" visibleOnSearch="N">
                <description>Ativo</description>
                <expression><![CDATA[if ($col_ATIVO == null) { return "S"; } else { return $col_ATIVO; }]]></expression>
            </field>
        </fields>
    </table>

</metadados>
```

---

### Passo 2 - Criar a entidade Java

Arquivo `model/src/main/java/br/com/hagious/qualitymanager/fornecedor/entity/Fornecedor.java`:

```java
package br.com.hagious.qualitymanager.fornecedor.entity;

import br.com.sankhya.studio.persistence.Column;
import br.com.sankhya.studio.persistence.Id;
import br.com.sankhya.studio.persistence.JapeEntity;
import br.com.sankhya.studio.persistence.JoinColumn;
import br.com.sankhya.studio.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JapeEntity(
    entity = "ThgFornecedor",
    table = "THGQMGFORNECEDOR"
)
public class Fornecedor {

    @Id
    @Column(name = "CODFORN")
    private Integer codFornecedor;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "CNPJ")
    private String cnpj;

    @Column(name = "CODPARC")
    private BigDecimal codParceiro;

    @Column(name = "ATIVO")
    private Boolean ativo;

    // Navegacao para Parceiro (somente se o codigo precisar acessar campos do Parceiro)
    @OneToOne
    @JoinColumn(name = "CODPARC", referencedColumnName = "CODPARC")
    private Parceiro parceiro;
}
```

---

### Passo 3 - (Se PK composta) Criar a classe `@Embeddable`

Entidade com PK composta -> criaria em `fornecedor/vo/FornecedorId.java`.

---

### Passo 4 - (Se enum) Criar enum no pacote `vo`

Entidade usa novo enum -> crie em `<feature>/vo/`.

---

### Passo 5 - Validar

- Entidade tem `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`?
- `@JapeEntity` tem so `entity` e `table`?
- Todos os `@Column` tem so `name`?
- Todos os `@JoinColumn` tem so `name` e `referencedColumnName`?
- Sem `@Expression`, `@GeneratedValue`, `@Option`, `@Property`?
- XML em `datadictionary/` criado?
- XML declara `allowSearch` e `visibleOnSearch` em todos os campos?

---

## 13. Exemplos Completos

### Entidade com PK simples

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@JapeEntity(entity = "ThgRegistro", table = "THGQMGREGISTRO")
public class Registro {

    @Id
    @Column(name = "CODREGISTRO")
    private Integer codRegistro;

    @Column(name = "DESCR")
    private String descricao;

    @Column(name = "OBSERVACAO")
    private String observacao;

    @Column(name = "CODIGO")
    private BigDecimal codigo;
}
```

### Entidade com PK composta + relacionamentos

```java
@Data
@NoArgsConstructor
@JapeEntity(entity = "ThgRegistroItem", table = "THGQMGREGISTROITEM")
public class RegistroItem {

    @Id
    private RegistroItemId embeddedId;

    @Column(name = "QTD")
    private BigDecimal quantidade;

    // Navegacao OneToOne
    @OneToOne
    @JoinColumn(name = "CODPRODUTO", referencedColumnName = "CODPROD")
    private Produto produto;

    // Navegacao ManyToOne
    @ManyToOne
    @JoinColumn(name = "CODREGISTRO", referencedColumnName = "CODREGISTRO")
    private Registro registro;

    // Metodos auxiliares para PK composta
    public Integer getCodRegistro() {
        return this.embeddedId.getCodRegistro();
    }
}
```

### Entidade com @OneToMany (pai com filhos)

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@JapeEntity(entity = "ThgRegistro", table = "THGQMGREGISTRO")
public class Registro {

    @Id
    @Column(name = "CODREGISTRO")
    private Integer codRegistro;

    @Column(name = "DESCR")
    private String descricao;

    @OneToMany(
        cascade = Cascade.ALL,
        relationship = {
            @Relationship(fromField = "CODREGISTRO", toField = "CODREGISTRO")
        }
    )
    private List<RegistroItem> itens;
}
```

### Entidade nativa com @Builder e metodos de dominio

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JapeEntity(entity = "ThgCabecalhoNota", table = "TGFCAB")
public class CabecalhoNota {

    @Id
    @Column(name = "NUNOTA")
    private BigDecimal nuNota;

    @Column(name = "TIPMOV")
    private String tipoMovimento;

    @Column(name = "QMG_STATUS")
    private StatusEnum status;

    @OneToOne
    @JoinColumn(name = "CODPARC", referencedColumnName = "CODPARC")
    private Parceiro parceiro;

    @OneToOne
    @JoinColumns({
        @JoinColumn(name = "CODTIPOPER", referencedColumnName = "CODTIPOPER"),
        @JoinColumn(name = "DHALTER", referencedColumnName = "DHTIPOPER")
    })
    private TipoOperacao tipoOperacao;

    @OneToMany(relationship = {
        @Relationship(fromField = "NUNOTA", toField = "NUNOTA")
    })
    private List<ItemNota> itens;

    // Metodos de dominio
    public void cancelar(Timestamp dataCancelamento) {
        this.dhCancelamento = dataCancelamento;
        this.status = StatusEnum.CANCELADO;
    }
}
```

### PK Composta (`@Embeddable`)

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class RegistroItemId {

    @Column(name = "CODREGISTRO")
    private Integer codRegistro;

    @Column(name = "NUITEM")
    private Integer nuItem;
}
```

### Enum (Value Object)

```java
@AllArgsConstructor
@Getter
public enum StatusEnum {
    PENDENTE("P"),
    APROVADO("A"),
    CANCELADO("C");

    private final String value;
}
```

---

## 14. Checklist

### Criando uma entidade nova

1. [ ] Criar XML do dicionario em `datadictionary/<TABELA>.xml` (ver `datadictionary-instructions.md`).
2. [ ] Criar classe Java no pacote `<feature>/entity/`.
3. [ ] Anotar com `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`.
4. [ ] Anotar com `@JapeEntity(entity = "...", table = "...")` - so esses 2 atributos.
5. [ ] Definir PK com `@Id` + `@Column(name)` (simples) ou `@Id` + classe `@Embeddable` (composta).
6. [ ] Cada campo persistido com `@Column(name = "...")` - so `name`.
7. [ ] Tipo Java correto para cada campo (ver tabela de tipos).
8. [ ] Relacionamentos so quando necessarios.
9. [ ] `@JoinColumn` com so `name` e `referencedColumnName`.
10. [ ] Nenhum `@Expression`, `@GeneratedValue`, `@Option`, `@Property`.
11. [ ] Imports limpos (so o usado).

### Criando um enum

1. [ ] Criar no pacote `<feature>/vo/`.
2. [ ] Anotar com `@AllArgsConstructor`, `@Getter`.
3. [ ] Campo `private final String value`.
4. [ ] Constantes em `UPPER_SNAKE_CASE`.

### Criando uma PK composta

1. [ ] Criar classe no pacote `<feature>/vo/` com nome `<Entidade>Id`.
2. [ ] Anotar com `@Data`, `@AllArgsConstructor`, `@NoArgsConstructor`, `@Embeddable`.
3. [ ] Cada campo com `@Column(name = "...")` - so `name`.
4. [ ] Na entidade, campo anotado so com `@Id` (sem `@Column`).

---

## 15. Erros Comuns

| Erro | Correcao |
|:-----|:---------|
| Colocar `description`, `dataType`, `size` no `@Column` | Somente `name`. Metadata fica no XML. |
| Colocar `@GeneratedValue` no `@Id` | Sequencia fica no XML (`sequenceType`/`sequenceField`). |
| Colocar `@Expression` no campo | Expressoes ficam no XML (`<expression>`). |
| Colocar `isNativeTable`, `description` no `@JapeEntity` | Somente `entity` e `table`. |
| Criar `@OneToOne` quando so precisa do valor da FK | Use `@Column(name = "FK")` se nao precisa navegar. |
| Esquecer de criar o XML do dicionario | Toda entidade **precisa** do XML correspondente. |
| Esquecer `@NoArgsConstructor` | Obrigatorio para o framework instanciar a entidade. |
| Usar `String` para campo `CHECKBOX` | Use `Boolean` - o framework converte S/N automaticamente. |
| Usar `Integer` para PKs nativas (NUNOTA, CODPARC) | Use `BigDecimal` - padrao do Sankhya Om. |
| Colocar entidade fora do pacote `<feature>/entity/` | Mover para o pacote correto da feature. |
