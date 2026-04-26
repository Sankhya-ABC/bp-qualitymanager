# Card 011 — Tela Evidencias (arquivos vinculados a RNC)

## Identificacao
| Atributo        | Valor                                           |
|:----------------|:------------------------------------------------|
| Modulo          | RNC — Registro de Nao Conformidades                         |
| Fase Roadmap    | Fase 1 — Core + Modulo NC/RNC                   |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 10.2.2        |
| Ordem no modulo | 011 de 014                                      |
| Depende de      | Card 001, Card 008 (Implementacao), Card 010 (Eficacia) |

---

## Tabela Principal

| Atributo  | Valor                |
|:----------|:---------------------|
| Nome      | `THGQMGEVID`   |
| Instancia | `ThgRncEvidencia`      |
| Sequencia | AUTO (`CODEVID`) |
| Dual-DB   | Oracle + SQL Server  |

---

## Campos

| # | Rotulo         | Coluna        | Tipo      | Tam | Obrig | Aba    | Grupo | Opcoes / Comportamento                                              |
|:--|:---------------|:--------------|:----------|:----|:------|:-------|:------|:--------------------------------------------------------------------|
| 1 | Id Evidencia   | `CODEVID` | INTEIRO   | -   | PK    | __main | -     | readOnly, auto sequence                                             |
| 2 | RNC            | `CODRNC`        | INTEIRO   | -   | Sim   | __main | -     | readOnly. FK para THGQMGREG.CODRNC                              |
| 3 | Origem         | `ORIGEM`      | LISTA     | 1   | Sim   | __main | -     | 1=Verificacao de Eficacia, 2=Implementacao. Define em qual fase foi anexada |
| 4 | Descricao      | `DESCRICAO`   | TEXTO     | 300 | Nao   | __main | -     | Descricao do arquivo ou evidencia                                   |
| 5 | Arquivo        | `ARQUIVO`     | ARQUIVO   | -   | Nao   | __main | -     | dataType=ARQUIVO. Upload de arquivo (foto, PDF, planilha, etc.)     |
| 6 | Aberto por     | `CODUSU`      | PESQUISA  | -   | Nao   | __main | -     | readOnly. Auto: $ctx_usuario_logado. visivel=N                      |
| 7 | Data Anexo     | `DHCREATE`   | DATA_HORA | -   | Nao   | __main | -     | readOnly. Auto: $ctx_dh_atual                                       |

---

## Regras de Negocio

1. Tabela compartilhada entre Fase 8 (Implementacao) e Fase 10 (Verificacao de Eficacia)
2. Campo ORIGEM diferencia em qual fase a evidencia foi inserida: 1=Eficacia, 2=Implementacao
3. A grid filha na tela de Implementacao (Card 008) exibe apenas ORIGEM=2
4. A grid filha na tela de Verificacao de Eficacia (Card 010) exibe apenas ORIGEM=1
5. Campo ARQUIVO usa dataType=ARQUIVO — armazenado no servidor de arquivos Sankhya

---

## Bug de producao identificado no produto legado (TGQ)

> **ATENCAO:** No produto legado (TGQ), a primaryKey desta tabela estava declarada como
> `CODAQRQCODAQRQ` (nome duplicado por erro de digitacao) em vez de `CODAQRQ`.
> No novo produto THGQMG, garantir que primaryKey aponta para `CODEVID` corretamente.

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGEVID, Listener BI, sequence

### Dicionario de Dados
- [ ] `datadictionary/THGQMGEVID.xml` — campo ARQUIVO com dataType=ARQUIVO, ORIGEM como LISTA

### Backend Java
- Sem action buttons proprios — e uma grid filha das telas de Implementacao e Eficacia

### Menu
- Nao aparece no menu — acessada apenas como aba filha dos cards 008 e 010
