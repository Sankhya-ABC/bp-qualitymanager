# Card 005 — Tela Acoes Corretivas 5W2H (Fase 5)

## Identificacao
| Atributo        | Valor                                          |
|:----------------|:-----------------------------------------------|
| Modulo          | RNC — Nao Conformidades                        |
| Fase Roadmap    | Fase 1 — Core + Modulo NC/RNC                  |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 10.2.1e      |
| Ordem no modulo | 005 de 014                                     |
| Depende de      | Card 001 (THGQMGREG)                     |

---

## Tabela Principal

| Atributo  | Valor                    |
|:----------|:-------------------------|
| Nome      | `THGQMGACCOR`  |
| Instancia | `QmNcAcaoCorretiva`      |
| Sequencia | AUTO (`CODACCOR`) |
| Dual-DB   | Oracle + SQL Server      |

### Instancias da tela
| Instancia          | Tipo       | Descricao                                        |
|:-------------------|:-----------|:-------------------------------------------------|
| `QmNcAcaoCorretiva`| Formulario | Acao corretiva com detalhe + 5W2H em aba separada|

---

## Campos

| # | Rotulo          | Coluna           | Tipo      | Tam | Obrig | Aba            | Grupo        | Opcoes / Comportamento                                |
|:--|:----------------|:-----------------|:----------|:----|:------|:---------------|:-------------|:------------------------------------------------------|
| 1 | Id Acao         | `CODACCOR`| INTEIRO   | -   | PK    | __main         | -            | readOnly, auto sequence                               |
| 2 | RNC             | `CODRNC`           | INTEIRO   | -   | Sim   | __main         | -            | readOnly. FK para THGQMGREG.CODRNC                |
| 3 | Prazo           | `DTPRAZO`        | DATA_HORA | -   | Nao   | __main         | -            | Data limite para execucao da acao corretiva           |
| 4 | Detalhe da Acao | `DETALHEACAO`    | HTML      | -   | Sim   | __main         | -            | Editor HTML. Descricao da acao corretiva a implementar|
| 5 | Nao se Aplica   | `NAOSEAPLICA`    | CHECKBOX  | 1   | Nao   | __main         | -            | Permite avancar sem preencher os campos               |
| 6 | O Que           | `OQUE`           | TEXTO     | 500 | Nao   | Analise 5W2H   | -            | O que deve ser feito na acao corretiva                |
| 7 | Como            | `COMO`           | TEXTO     | 500 | Nao   | Analise 5W2H   | -            | Como sera executada a acao                            |
| 8 | Onde            | `ONDE`           | TEXTO     | 500 | Nao   | Analise 5W2H   | -            | Onde sera executada                                   |
| 9 | Por Que         | `PORQUE`         | TEXTO     | 500 | Nao   | Analise 5W2H   | -            | Por que esta acao foi definida                        |
| 10| Quando          | `QUANDO`         | DATA_HORA | -   | Nao   | Analise 5W2H   | -            | Data prevista                                         |
| 11| Quanto          | `QUANTO`         | TEXTO     | 200 | Nao   | Analise 5W2H   | -            | Custo ou recurso necessario                           |
| 12| Aberto por      | `CODUSU`         | PESQUISA  | -   | Nao   | __main         | -            | readOnly. Auto: $ctx_usuario_logado. visivel=N        |
| 13| Criado em       | `DHCREATE`      | DATA_HORA | -   | Nao   | __main         | -            | readOnly. Auto: $ctx_dh_atual. visivel=N              |
| 14| Alterado em     | `DHALTER`        | DATA_HORA | -   | Nao   | __main         | -            | readOnly. Trigger BIU. visivel=N                      |

---

## Diferenca de UX em relacao a Abrangencia (Card 004)

> **ATENCAO:** O 5W2H desta tela fica em aba SEPARADA chamada "Analise 5W2H".
> Na Abrangencia (Card 004) o 5W2H fica em __main.
> Esta inconsistencia e proposital — reflete a logica de negocio:
> na Abrangencia o 5W2H descreve o ESCOPO da acao;
> nas Acoes Corretivas o 5W2H e uma ANALISE adicional apos o detalhe principal.

---

## Action Buttons

| Botao              | Classe Java                            | Instancia           | Transacao | Descricao                                          |
|:-------------------|:---------------------------------------|:--------------------|:----------|:---------------------------------------------------|
| Mudar Fase         | `MudarFaseAcaoCorretivaActionButton`   | `QmNcAcaoCorretiva` | AUTOMATIC | Avanca RNC para Fase 6 (Revisao de Documentos)     |
| Voltar Fase        | `VoltarFaseAcaoCorretivaActionButton`  | `QmNcAcaoCorretiva` | AUTOMATIC | Retorna RNC para Fase 4 (Abrangencia)              |
| Cancelar RNC       | `CancelarRncAcaoCorretivaActionButton` | `QmNcAcaoCorretiva` | AUTOMATIC | Cancela a RNC (STATUS='C')                         |
| Enviar Notificacao | `NotificarAcaoCorretivaActionButton`   | `QmNcAcaoCorretiva` | AUTOMATIC | Envia e-mail ao responsavel                        |

---

## Regras de Negocio

1. Tela so e acessivel quando THGQMGREG.CODFASE = 5
2. Campo DETALHEACAO e obrigatorio para avancar, exceto se NAOSEAPLICA='S'
3. 5W2H na aba "Analise 5W2H" e opcional — complementa o detalhe principal
4. Esta fase pode ser destino do DESVIO DE FLUXO: se ORIGEM da RNC for Riscos/Oportunidades, FaseNcBusinessService pula da Fase 3 direto para aqui (Fase 5)
5. Toda mudanca registra entrada em THGQMGLOG

---

## Gatilhos e Automatismos

| Evento           | O que acontece                                       | Onde implementar      |
|:-----------------|:-----------------------------------------------------|:----------------------|
| INSERT           | CODACCOR via sequence, DHCREATE=now()        | Listener BI   |
| UPDATE           | DHALTER = SYSDATE/GETDATE()                          | Trigger BIU           |
| Mudar Fase (btn) | CODFASE=6, registra THGQMGREGFASE, audit log       | FaseNcBusinessService |
| Desvio de fluxo  | CODFASE=5 recebido diretamente da Fase 3 (Causa Raiz) | FaseNcBusinessService |

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGACCOR, triggers, sequence

### Dicionario de Dados
- [ ] `datadictionary/THGQMGACCOR.xml` — 2 abas: __main e "Analise 5W2H"

### Backend Java
- [ ] `model/.../actionButtons/MudarFaseAcaoCorretivaActionButton.java`
- [ ] `model/.../actionButtons/VoltarFaseAcaoCorretivaActionButton.java`
- [ ] `model/.../actionButtons/CancelarRncAcaoCorretivaActionButton.java`
- [ ] `model/.../actionButtons/NotificarAcaoCorretivaActionButton.java`

### Menu
- [x] `datadictionary/menu.xml` — instancia QmNcAcaoCorretiva ja registrada como item 05

---

## Observacoes Tecnicas

- 5W2H em aba SEPARADA "Analise 5W2H" — diferente do Card 004 (Abrangencia) que usa __main
- DETALHEACAO usa dataType=HTML — editor rico de texto
- instanceName: `QmNcAcaoCorretiva` — copiar exatamente do XML
- Desvio de fluxo: FaseNcBusinessService deve receber CODFASE=5 como destino quando ORIGEM=Riscos/Oportunidades
