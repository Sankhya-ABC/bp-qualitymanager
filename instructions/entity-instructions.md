---
applyTo: "**/*.java"
---

# Entidade Java (@JapeEntity) ? Addon Studio 2.0

Entidade Java = representação domínio de tabela banco. **Limpa** ? contém só mapeamento estrutural mínimo. Metadata UI, tipos, descrições, comportamento fica no **Dicionário de Dados** (XMLs em `datadictionary/`).

> **Referência complementar:** consulte `datadictionary-instructions.md` para criar XML correspondente à entidade.

---

## 1. Regras Fundamentais

| Regra                                                | Detalhe                                                   |
|:-----------------------------------------------------|:----------------------------------------------------------|
| `@Column` só tem `name`                              | `@Column(name = "COLUNA")` ? nenhum outro atributo.       |
| `@JoinColumn` só tem `name` e `referencedColumnName` | `@JoinColumn(name = "...", referencedColumnName = "...")` |
| `@JapeEntity` só tem `entity` e `table`              | `@JapeEntity(entity = "...", table = "...")`              |
| Sem `@Expression`                                    | Expressões ficam no XML (`<expression>`).                 |
| Sem `@GeneratedValue`                                | Sequência fica no XML (`sequenceType`/`sequenceField`).   |
| Sem `@Option` / `@Property`                          | Opções ficam no XML (`<fieldOptions>`).                   |
| Lombok obrigatório                                   | `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`.     |
| Prefixo `Tdc` obrigatorio no `entity`               | Ex.: `@JapeEntity(entity = "TdcCabecalhoCotacao", ...)` |

---

## 2. Organização de Pacotes

```
core/domain/
|-- entity/       - Entidades (@JapeEntity) e classes de domínio puro
|-- vo/           - Value Objects: @Embeddable (PKs compostas) e Enums
|-- repository/   - Interfaces de repositório
|-- service/      - Serviços de domínio
|-- gateway/      - Interfaces de integração externa
|-- exception/    - Exceções de domínio
```

| Tipo de classe                            | Pacote   | Exemplo                                |
|:------------------------------------------|:---------|:---------------------------------------|
| Entidade persistida (`@JapeEntity`)       | `entity` | `MeuProduto.java`                      |
| PK composta (`@Embeddable`)               | `vo`     | `MinhaEntidadeId.java`                 |
| Enum de domínio                           | `vo`     | `TipoEnum.java`                        |
| Classe de domínio puro (sem persistência) | `entity` | `Receituario.java`, `AreaTratada.java` |

---

## 3. Tipos de Classe no Domínio

### 3.1 Entidade Persistida (`@JapeEntity`)

Representa tabela no banco. Tem `@JapeEntity`, `@Id`, `@Column`, opcionalmente relacionamentos.

```java

@JapeEntity(entity = "MeuAlvo", table = "ADCALVO")
public class MeuAlvo { ...
}
```

### 3.2 PK Composta (`@Embeddable`)

Tabela com PK composta ? classe separada anotada com `@Embeddable`.

```java

@Embeddable
public class MinhaEntidadeId { ...
}
```

### 3.3 Enum (Value Object)

Valores finitos de domínio (listas opções). Usados como tipo campo em entidades. Têm `value` = valor armazenado no banco.

**Regras obrigatórias:**

| Regra                              | Detalhe                                                          |
|:-----------------------------------|:-----------------------------------------------------------------|
| `@Getter` (Lombok)                 | Gera o getter de `value` automaticamente.                        |
| `@AllArgsConstructor` (Lombok)     | Gera o construtor que recebe `value`.                            |
| Campo `private final String value` | Valor persistido no banco (código curto).                        |
| Pacote `vo/`                       | Todo enum de domínio fica em `core/domain/vo/`.                  |
| Sufixo `Enum`                      | Nome da classe sempre termina com `Enum` (ex: `TipoStatusEnum`). |

**Anatomia completa:**

```java
package br.com.sankhya.devcenter.{addon}.core.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoEnum {
    OPCAO_A("A"),
    OPCAO_B("B");

    private final String value;
}
```

### 3.4 Classe de Domínio Puro

Classes representando conceitos domínio **sem persistência direta**. Sem `@JapeEntity`. Contêm lógica negócio, validações, factory methods.

```java
public class ResultadoCalculo { ...
}

public class DadosConsolidados { ...
}
```

---

## 4. Anatomia de uma Entidade

```java
package br.com.sankhya.devcenter.{addon}.core.domain.entity;  // 1. Pacote

import br.com.sankhya.studio.persistence.*;                    // 2. Imports de persistência
import lombok.*;                                                // 3. Imports Lombok

@Data                                                           // 4. Lombok obrigatório
@NoArgsConstructor
@AllArgsConstructor
@JapeEntity(                                                    // 5. Mapeamento: somente entity + table
    entity = "NomeDaEntidade",
    table = "NOME_TABELA"
)
public class NomeDaEntidade {                                   // 6. Classe plana (sem herança de metadata)

    @Id                                                         // 7. Chave primária
    @Column(name = "COLUNA_PK")
    private Integer id;

    @Column(name = "COLUNA_1")                                  // 8. Campos: somente name
    private String campo1;

    @OneToMany(...)                                             // 9. Relacionamentos (se houver)
    private List<EntidadeFilha> filhos;

    public void metodoDeNegocio() { ...}                      // 10. Métodos de domínio (se houver)
}
```

---

## 5. Anotações Permitidas

### Anotações que DEVEM ser usadas

| Anotação                     | Uso                                             | Pacote                              |
|:-----------------------------|:------------------------------------------------|:------------------------------------|
| `@JapeEntity(entity, table)` | Toda entidade persistida                        | `br.com.sankhya.studio.persistence` |
| `@Id`                        | Campo(s) de chave primária                      | `br.com.sankhya.studio.persistence` |
| `@Column(name)`              | Todo campo persistido                           | `br.com.sankhya.studio.persistence` |
| `@Embeddable`                | Classe de PK composta                           | `br.com.sankhya.studio.persistence` |
| `@Data`                      | Getter/Setter/ToString/Equals/Hash              | `lombok`                            |
| `@NoArgsConstructor`         | Construtor vazio (obrigatório para o framework) | `lombok`                            |
| `@AllArgsConstructor`        | Construtor com todos os campos                  | `lombok`                            |

### Anotações opcionais (usar quando necessário)

| Anotação                                  | Quando usar                                                                  | Pacote                              |
|:------------------------------------------|:-----------------------------------------------------------------------------|:------------------------------------|
| `@Builder`                                | Quando a entidade é construída programaticamente no domínio                  | `lombok`                            |
| `@OneToMany`                              | Relacionamento pai ? filhos                                                  | `br.com.sankhya.studio.persistence` |
| `@OneToOne`                               | Navegação para entidade referenciada                                         | `br.com.sankhya.studio.persistence` |
| `@ManyToOne`                              | Navegação inversa filho ? pai                                                | `br.com.sankhya.studio.persistence` |
| `@JoinColumn(name, referencedColumnName)` | Junto com `@OneToOne` / `@ManyToOne`                                         | `br.com.sankhya.studio.persistence` |
| `@JoinColumns`                            | Múltiplos `@JoinColumn` (FK composta)                                        | `br.com.sankhya.studio.persistence` |
| `@Cascade`                                | Dentro de `@OneToMany` para cascatear operações                              | `br.com.sankhya.studio.persistence` |
| `@Relationship`                           | Dentro de `@OneToMany` para definir campos do vínculo                        | `br.com.sankhya.studio.persistence` |
| `@SuperBuilder`                           | Quando a entidade herda de classe base de domínio (ex: VO com campos comuns) | `lombok.experimental`               |
| `@EqualsAndHashCode(callSuper = true)`    | Junto com herança + `@SuperBuilder`                                          | `lombok`                            |

### Anotações PROIBIDAS na entidade

| Anotação          | Onde fica                          | Motivo                |
|:------------------|:-----------------------------------|:----------------------|
| `@Expression`     | XML `<expression>`                 | Metadata do framework |
| `@GeneratedValue` | XML `sequenceType`/`sequenceField` | Metadata do framework |
| `@Option`         | XML `<fieldOptions>`               | Metadata de UI        |
| `@Property`       | XML                                | Metadata do framework |

---

## 6. Chave Primária (PK)

### 6.1 PK Simples

Tabela com única coluna como PK:

```java

@Id
@Column(name = "CODENTIDADE")
private Integer codEntidade;
```

> PK sequencial: nao use coluna com prefixo `ID`. Use `COD*` (cadastros) ou `NU*` (movimentos/documentos).

### 6.2 PK Composta (`@Embeddable`)

Tabela com PK composta ? crie classe separada no pacote `vo`:

**Classe `@Embeddable` (em `vo/`):**

```java
package br.com.sankhya.devcenter.{addon}.core.domain.vo;

import br.com.sankhya.studio.persistence.Column;
import br.com.sankhya.studio.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class MinhaEntidadeId {

    @Column(name = "CODPAI")
    private Integer codPai;

    @Column(name = "NUITEM")
    private Integer nuItem;
}
```

**Uso na entidade:**

```java

@Id
private MinhaEntidadeId embeddedId;
```

### Convenções da PK composta

| Regra          | Detalhe                                                             |
|:---------------|:--------------------------------------------------------------------|
| Nome da classe | `<NomeEntidade>Id` (ex: `MeuProdutoId`)                             |
| Pacote         | `core.domain.vo`                                                    |
| Anotações      | `@Data`, `@AllArgsConstructor`, `@NoArgsConstructor`, `@Embeddable` |
| Campos         | Cada campo com `@Column(name = "...")` ? somente `name`             |
| Na entidade    | Campo anotado apenas com `@Id` (sem `@Column`)                      |

### Métodos auxiliares na entidade (opcional)

Facilitar acesso aos campos da PK composta ? crie métodos delegadores:

```java
public Integer getCodPai() {
    return this.embeddedId.getCodPai();
}

public Integer getNuItem() {
    return this.embeddedId.getNuItem();
}
```

---

## 7. Campos (@Column)

### Regra única

```java

@Column(name = "NOME_COLUNA")
private TipoJava nomeCampo;
```

Nenhum outro atributo. Ponto.

### Mapeamento de tipos

| Tipo no banco / XML              | Tipo Java sugerido                     |
|:---------------------------------|:---------------------------------------|
| `INTEIRO`                        | `Integer` ou `BigDecimal`              |
| `TEXTO`                          | `String`                               |
| `DECIMAL`                        | `BigDecimal`                           |
| `DATA_HORA`                      | `Timestamp` (`java.sql.Timestamp`)     |
| `CHECKBOX`                       | `Boolean`                              |
| `PESQUISA` (FK)                  | `BigDecimal` ou `Integer` (tipo da FK) |
| Campo com opções (enum no banco) | Enum Java (veja seção 9)               |

### Quando usar `Integer` vs `BigDecimal`

| Contexto                                       | Tipo                      | Motivo                           |
|:-----------------------------------------------|:--------------------------|:---------------------------------|
| PKs de tabelas do add-on (`COD*`/`NU*`)        | `Integer`                 | Sequenciais do add-on sao inteiros simples |
| PKs de tabelas nativas (NUNOTA, CODPARC, etc.) | `BigDecimal`              | Padrão do Sankhya Om             |
| Quantidades, valores monetários                | `BigDecimal`              | Precisão decimal                 |
| Campos numéricos simples                       | `BigDecimal` ou `Integer` | Conforme necessidade             |

---

## 8. Relacionamentos

### 8.1 `@OneToMany` ? Pai ? Filhos

Entidade com lista de filhos.

```java

@OneToMany(
    cascade = Cascade.ALL,
    relationship = {
        @Relationship(
            fromField = "CODPRODUTO",  // Campo da tabela PAI
            toField = "CODPRODUTO"     // Campo da tabela FILHA
        )
    }
)
private List<VinculoProduto> vinculos;
```

| Atributo    | Significado                                       |
|:------------|:--------------------------------------------------|
| `cascade`   | `Cascade.ALL` para cascatear insert/update/delete |
| `fromField` | Nome da **coluna** na tabela pai                  |
| `toField`   | Nome da **coluna** na tabela filha                |

> Tipo do campo sempre `List<EntidadeFilha>`.

### 8.2 `@OneToOne` + `@JoinColumn` ? Navegação para entidade referenciada

Domínio precisa **navegar** para outra entidade (acessar campos dela).

```java

@OneToOne
@JoinColumn(name = "CODPARC", referencedColumnName = "CODPARC")
private Parceiro parceiro;
```

| Atributo               | Significado                                                |
|:-----------------------|:-----------------------------------------------------------|
| `name`                 | Coluna na **tabela atual** (FK)                            |
| `referencedColumnName` | Coluna na **tabela referenciada** (PK ou campo de vínculo) |

### 8.3 `@OneToOne` + `@JoinColumns` ? FK Composta

FK aponta para PK composta ? use `@JoinColumns`:

```java

@OneToOne
@JoinColumns({
    @JoinColumn(name = "CODTIPOPER", referencedColumnName = "CODTIPOPER"),
    @JoinColumn(name = "DHALTER", referencedColumnName = "DHTIPOPER")
})
private TipoOperacao tipoOperacao;
```

### 8.4 `@ManyToOne` + `@JoinColumn` ? Filho ? Pai

Navegação inversa: filho ? pai.

```java

@ManyToOne
@JoinColumn(name = "CODORIGEM", referencedColumnName = "CODPRODUTO")
private MeuProduto produto;
```

### Quando usar cada relacionamento

| Preciso de...                         | Uso                                                 |
|:--------------------------------------|:----------------------------------------------------|
| Lista de filhos na entidade pai       | `@OneToMany`                                        |
| Acessar campos de outra entidade (FK) | `@OneToOne` + `@JoinColumn`                         |
| Acessar o pai a partir do filho       | `@ManyToOne` + `@JoinColumn`                        |
| Apenas armazenar a FK (sem navegar)   | Somente `@Column(name = "FK")` ? sem relacionamento |

> **Importante:** Só precisa do valor da FK (ex: `codParceiro`) ? use apenas `@Column`. `@OneToOne`/`@JoinColumn` necessário **somente** quando domínio precisa acessar campos da entidade referenciada.

---

## 9. Enums (Value Objects)

Enums = valores finitos armazenados no banco como texto curto (geralmente 1 caractere).

### Estrutura padrão

```java
package br.com.sankhya.devcenter.{addon}.core.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NomeEnum {
    OPCAO_UM("V"),
    OPCAO_DOIS("P");

    private final String value;
}
```

### Convenções

| Regra                | Detalhe                                                  |
|:---------------------|:---------------------------------------------------------|
| Pacote               | `core.domain.vo`                                         |
| Anotações            | `@AllArgsConstructor`, `@Getter`                         |
| Campo `value`        | `private final String value` ? valor armazenado no banco |
| Nomes das constantes | `UPPER_SNAKE_CASE` descritivo                            |
| Valores              | Strings curtas (1-2 caracteres, geralmente)              |

### Uso na entidade

```java

@Column(name = "TIPMOV")
private TipoMovimento tipoMovimento;
```

Framework converte automaticamente entre valor no banco (`"V"`) e enum Java (`TipoMovimento.VENDA`).

---

## 10. Classes de Domínio Puro

Classes = conceitos de negócio sem mapeamento direto para tabela. Contêm lógica domínio rica.

### Características

- **Sem** `@JapeEntity`, `@Id`, `@Column`.
- Ficam no pacote `entity` (convenção projeto).
- Usam só `@Data` do Lombok (ou construtor manual).
- Podem ter factory methods (`create(...)`) com validações.

### Exemplo simples

```java

@Data
public class ResultadoCalculo {

    private BigDecimal valor;
    private String mensagem;
}
```

### Exemplo com factory method e validação

```java

@Data
public class DocumentoGerado {

    private CabecalhoDocumento cabecalho;
    private List<ItemDocumento> itens;

    private DocumentoGerado(CabecalhoDocumento cabecalho, List<ItemDocumento> itens) {
        this.cabecalho = cabecalho;
        this.itens = itens;
    }

    public static DocumentoGerado create(CabecalhoDocumento cabecalho, List<ItemDocumento> itens) {
        if (itens == null || itens.isEmpty()) {
            throw new CreateIssueException("Deve conter ao menos um item.");
        }
        return new DocumentoGerado(cabecalho, itens);
    }
}
```

---

## 11. Métodos de Domínio

Entidades podem (e devem) conter **lógica negócio** relacionada ao estado interno.

### Regras

- Métodos operam sobre campos da própria entidade.
- Nomes expressam intenção de negócio (não técnica).
- Não acessam banco, serviços, repositórios ? só próprios dados.

### Exemplos

```java
// Consulta de estado
public Boolean isVenda() {
    return this.tipoMovimento == TipoMovimento.PEDIDO_VENDA
        || this.tipoMovimento == TipoMovimento.VENDA;
}

public Boolean estaAtivo() {
    return this.ativo != null && this.ativo;
}

// Mutação de estado
public void cancelar(Timestamp dataCancelamento) {
    this.dhCancelamento = dataCancelamento;
    this.status = Status.CANCELADO;
}

// Delegação para entidade relacionada
public Boolean deveProcessar() {
    return this.getTipoOperacao() != null
        && this.getTipoOperacao().deveProcessar();
}
```

---

## 12. Passo a Passo: Criando uma Entidade do Zero

### Cenário: Criar uma nova entidade `MeuFornecedor`

Tabela `ADCFORNECEDOR`, PK simples `CODFORN` (auto), campos `NOME`, `CNPJ`, `ATIVO`, com vinculo para `Parceiro`.

---

### Passo 1 ? Criar o XML do dicionário de dados

Arquivo `datadictionary/ADCFORNECEDOR.xml`:

```xml
<?xml version="1.0" encoding="ISO-8859-1" ?>
<metadados xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:noNamespaceSchemaLocation="../.gradle/metadados.xsd">

    <table name="ADCFORNECEDOR" sequenceType="A" sequenceField="CODFORN">
        <description>Fornecedores</description>
        <primaryKey>
            <field name="CODFORN"/>
        </primaryKey>
        <instances>
            <instance name="MeuFornecedor">
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

### Passo 2 ? Criar a entidade Java

Arquivo `core/domain/entity/MeuFornecedor.java`:

```java
package br.com.sankhya.devcenter.{addon}.core.domain.entity;

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
    entity = "MeuFornecedor",
    table = "ADCFORNECEDOR"
)
public class MeuFornecedor {

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

    // Navegação para Parceiro (somente se o domínio precisar acessar campos do Parceiro)
    @OneToOne
    @JoinColumn(name = "CODPARC", referencedColumnName = "CODPARC")
    private Parceiro parceiro;
}
```

---

### Passo 3 ? (Se PK composta) Criar a classe `@Embeddable`

Entidade com PK composta ? criaria em `core/domain/vo/MeuFornecedorId.java`.

---

### Passo 4 ? (Se enum) Criar enum no pacote `vo`

Entidade usa novo enum ? crie em `core/domain/vo/`.

---

### Passo 5 ? Validar

- Entidade tem `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`?
- `@JapeEntity` tem só `entity` e `table`?
- Todos `@Column` têm só `name`?
- Todos `@JoinColumn` têm só `name` e `referencedColumnName`?
- Sem `@Expression`, `@GeneratedValue`, `@Option`, `@Property`?
- XML em `datadictionary/` criado?
- XML declara `allowSearch` e `visibleOnSearch` em todos campos?

---

## 13. Exemplos Completos

### Entidade com PK simples

```java

@Data
@NoArgsConstructor
@AllArgsConstructor
@JapeEntity(entity = "MinhaEntidade", table = "ADCENTIDADE")
public class MinhaEntidade {

    @Id
    @Column(name = "CODENTIDADE")
    private Integer codEntidade;

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
@JapeEntity(entity = "MinhaRelacao", table = "ADCRELACAO")
public class MinhaRelacao {

    @Id
    private MinhaRelacaoId embeddedId;

    @Column(name = "NUREF")
    private BigDecimal nuRef;

    @Column(name = "QTDMIN")
    private BigDecimal qtdMinima;

    // Navegação OneToOne
    @OneToOne
    @JoinColumn(name = "CODORIGEM", referencedColumnName = "CODPRODUTO")
    private MeuProduto produtoOrigem;

    // Navegação ManyToOne
    @ManyToOne
    @JoinColumn(name = "CODORIGEM", referencedColumnName = "CODPAI")
    private MeuProduto produto;

    // Métodos auxiliares para PK composta
    public Integer getCodPai() {
        return this.embeddedId.getCodPai();
    }
}
```

### Entidade com @OneToMany (pai com filhos)

```java

@Data
@NoArgsConstructor
@AllArgsConstructor
@JapeEntity(entity = "MeuProduto", table = "ADCPRODUTO")
public class MeuProduto {

    @Id
    @Column(name = "CODPRODUTO")
    private Integer codProduto;

    @Column(name = "DESCRPRODUTO")
    private String nomeProduto;

    @OneToMany(
        cascade = Cascade.ALL,
        relationship = {
            @Relationship(fromField = "CODPRODUTO", toField = "CODPRODUTO")
        }
    )
    private List<VinculoProduto> vinculos;
}
```

### Entidade nativa com @Builder e métodos de domínio

```java

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JapeEntity(entity = "CabecalhoNota", table = "TGFCAB")
public class CabecalhoNota {

    @Id
    @Column(name = "NUNOTA")
    private BigDecimal nuNota;

    @Column(name = "TIPMOV")
    private TipoMovimento tipoMovimento;

    @Column(name = "ADC_STATUS")
    private StatusProcesso status;

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

    // Métodos de domínio
    public Boolean isVenda() {
        return this.tipoMovimento == TipoMovimento.PEDIDO_VENDA
            || this.tipoMovimento == TipoMovimento.VENDA;
    }

    public void cancelar(Timestamp dataCancelamento) {
        this.dhCancelamento = dataCancelamento;
        this.status = StatusProcesso.CANCELADO;
    }
}
```

### Entidade somente com PK composta (sem campos próprios)

```java

@Data
@NoArgsConstructor
@AllArgsConstructor
@JapeEntity(entity = "MeuVinculo", table = "ADCVINCULO")
public class MeuVinculo {

    @Id
    private MeuVinculoId embeddedId;
}
```

### Enum (Value Object)

```java

@AllArgsConstructor
@Getter
public enum StatusProcesso {
    PENDENTE("P"),
    EMITIDO("E"),
    CANCELADO("C");

    private final String value;
}
```

### PK Composta (`@Embeddable`)

```java

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ItemNotaId {

    @Column(name = "NUNOTA")
    private BigDecimal nuNota;

    @Column(name = "SEQUENCIA")
    private BigDecimal sequencia;
}
```

---

## 14. Checklist

### Criando uma entidade nova

1. ? Criar XML dicionário em `datadictionary/<TABELA>.xml` (ver `datadictionary-instructions.md`).
2. ? Criar classe Java no pacote `core.domain.entity`.
3. ? Anotar com `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`.
4. ? Anotar com `@JapeEntity(entity = "...", table = "...")` ? só esses 2 atributos.
5. ? Definir PK com `@Id` + `@Column(name)` (simples) ou `@Id` + classe `@Embeddable` (composta).
6. ? Cada campo persistido com `@Column(name = "...")` ? só `name`.
7. ? Tipo Java correto para cada campo (ver tabela tipos).
8. ? Relacionamentos só quando necessários no domínio.
9. ? `@JoinColumn` com só `name` e `referencedColumnName`.
10. ? Nenhum `@Expression`, `@GeneratedValue`, `@Option`, `@Property`.
11. ? Imports limpos (só o usado).

### Criando um enum

1. ? Criar no pacote `core.domain.vo`.
2. ? Anotar com `@AllArgsConstructor`, `@Getter`.
3. ? Campo `private final String value`.
4. ? Constantes em `UPPER_SNAKE_CASE`.

### Criando uma PK composta

1. ? Criar classe no pacote `core.domain.vo` com nome `<Entidade>Id`.
2. ? Anotar com `@Data`, `@AllArgsConstructor`, `@NoArgsConstructor`, `@Embeddable`.
3. ? Cada campo com `@Column(name = "...")` ? só `name`.
4. ? Na entidade, campo anotado só com `@Id` (sem `@Column`).

---

## 15. Erros Comuns

| Erro                                                    | Correção                                                  |
|:--------------------------------------------------------|:----------------------------------------------------------|
| Colocar `description`, `dataType`, `size` no `@Column`  | Somente `name`. Metadata fica no XML.                     |
| Colocar `@GeneratedValue` no `@Id`                      | Sequência fica no XML (`sequenceType`/`sequenceField`).   |
| Colocar `@Expression` no campo                          | Expressões ficam no XML (`<expression>`).                 |
| Colocar `isNativeTable`, `description` no `@JapeEntity` | Somente `entity` e `table`.                               |
| Criar `@OneToOne` quando só precisa do valor da FK      | Use `@Column(name = "FK")` se não precisa navegar.        |
| Esquecer de criar o XML do dicionário                   | Toda entidade **precisa** do XML correspondente.          |
| Esquecer `@NoArgsConstructor`                           | Obrigatório para o framework instanciar a entidade.       |
| Usar `String` para campo `CHECKBOX`                     | Use `Boolean` ? o framework converte S/N automaticamente. |
| Usar `Integer` para PKs nativas (NUNOTA, CODPARC)       | Use `BigDecimal` ? padrão do Sankhya Om.                  |