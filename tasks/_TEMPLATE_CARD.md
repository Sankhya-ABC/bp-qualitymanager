# Card NNN — Tela NOME

## Identificacao
| Atributo       | Valor                              |
|:---------------|:-----------------------------------|
| Modulo         | Nome do Modulo                     |
| Fase Roadmap   | Fase X — Descricao                 |
| ISO            | ABNT NBR ISO XXXX:AAAA — Clausula  |
| Ordem no modulo| NNN de TTT                         |
| Depende de     | Card NNN (se houver)               |

---

## Tabela Principal

| Atributo   | Valor             |
|:-----------|:------------------|
| Nome       | `THGQMG___`         |
| Instancia  | `ThgXxxNome`       |
| Sequencia  | AUTO (`CODXXX`)    |
| Dual-DB    | Oracle + SQL Server |

### Instancias da tela
| Instancia       | Tipo       | Descricao                        |
|:----------------|:-----------|:---------------------------------|
| `ThgXxxConsulta` | Lista/Grid | Visao de consulta com filtros    |
| `ThgXxxRegistro` | Formulario | Formulario completo de cadastro  |

---

## Campos

| # | Rotulo          | Coluna      | Tipo       | Tam | Obrig | Aba        | Grupo        | Opcoes / Comportamento                    |
|:--|:----------------|:------------|:-----------|:----|:------|:-----------|:-------------|:------------------------------------------|
| 1 | Id              | `CODXXX`     | INTEIRO    | -   | PK    | __main     | -            | readOnly, auto sequence                   |
| 2 | Campo           | `CAMPO`     | TIPO       | N   | S/N   | NomeAba    | NomeGrupo    | Descricao do comportamento                |

### Tipos de campo disponiveis
`INTEIRO` `TEXTO` `LISTA` `CHECKBOX` `DATA_HORA` `PESQUISA` `HTML` `ARQUIVO` `DECIMAL`

### Campos automaticos padrao (todo cadastro tem)
| Campo     | Coluna        | Valor automatico              |
|:----------|:--------------|:------------------------------|
| Usuario   | `CODUSU`      | `$ctx_usuario_logado`         |
| Criado em | `DHCREATE`   | `$ctx_dh_atual`               |
| Alterado  | `DHALTER`     | Listener BIU                   |
| Ativo     | `ATIVO`       | CHECKBOX default 'S'          |

---

## Tabelas Filhas (grids na tela)

| Tabela filha       | Instancia filha  | Vinculo FK     | Descricao              |
|:-------------------|:-----------------|:---------------|:-----------------------|
| `THGQMG___`          | `ThgXxxFilha`     | `CODXXX`        | Descricao do grid      |

---

## Action Buttons

| Botao              | Classe Java                  | Instancia alvo  | Transacao  | Descricao                        |
|:-------------------|:-----------------------------|:----------------|:-----------|:---------------------------------|
| Mudar Fase         | `MudarFaseXxxActionButton`   | `ThgXxxNome`     | AUTOMATIC  | Avanca para proxima fase         |
| Voltar Fase        | `VoltarFaseXxxActionButton`  | `ThgXxxNome`     | AUTOMATIC  | Retorna fase anterior            |
| Enviar Notificacao | `NotificarXxxActionButton`   | `ThgXxxNome`     | AUTOMATIC  | Envia e-mail ao responsavel      |

---

## Regras de Negocio

1. Regra 1 — descricao clara do comportamento esperado
2. Regra 2 — condicoes, validacoes, defaults
3. Regra N — gatilhos e automatismos

---

## Gatilhos e Automatismos

| Evento             | O que acontece                              | Onde implementar          |
|:-------------------|:--------------------------------------------|:--------------------------|
| INSERT             | Preenche CODXXX via sequence, seta defaults  | Listener BI       |
| Mudanca de STATUS  | Registra em THGQMGLOG               | BusinessService Java      |
| Avanco de fase     | Atualiza FASEATUAL, registra historico       | FaseRncBusinessService     |

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/VXXX-NOME.xml` — DDL Oracle + SQL Server + trigger + indices

### Dicionario de Dados
- [ ] `datadictionary/THGQMG___.xml` — instancias, campos, relacionamentos

### Backend Java (model/)
- [ ] `model/.../entity/XxxEntity.java`
- [ ] `model/.../repository/XxxRepository.java`

### Backend Java (vc/)
- [ ] `vc/.../service/XxxService.java`
- [ ] `vc/.../controller/XxxController.java`
- [ ] `vc/.../dto/XxxRequestDTO.java`
- [ ] `vc/.../dto/XxxResponseDTO.java`
- [ ] `vc/.../mapper/XxxMapper.java`
- [ ] `vc/.../listener/XxxListener.java`
- [ ] `vc/.../callback/MudarFaseXxxActionButton.java`

### Menu
- [ ] `datadictionary/menu.xml` — entrada ja registrada

---

## Observacoes Tecnicas

- Prefixo de tabela: `THGQMG` (THG QualityManager)
- autoDDL = false para esta tabela — DDL manual no dbscripts
- FKs para tabelas nativas Sankhya definidas apenas no datadictionary via PESQUISA
- Audit log obrigatorio: toda mudanca de STATUS ou FASE gera registro em `THGQMGLOG`
- instanceName dos @ActionButton e @Listener deve ser copiado EXATAMENTE do XML
