# Card 002 — Tela Acoes Imediatas (Fase 2)

## Identificacao
| Atributo        | Valor                                          |
|:----------------|:-----------------------------------------------|
| Modulo          | RNC — Registro de Nao Conformidades                        |
| Fase Roadmap    | Fase 1 — Core + Modulo NC/RNC                  |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 10.2.1b      |
| Ordem no modulo | 002 de 014                                     |
| Depende de      | Card 001 (THGQMGREG)                     |

---

## Tabela Principal

| Atributo  | Valor                    |
|:----------|:-------------------------|
| Nome      | `THGQMGACIM`   |
| Instancia | `QmRncAcaoImediata`       |
| Sequencia | AUTO (`CODACIM`)  |
| Dual-DB   | Oracle + SQL Server      |

### Instancias da tela
| Instancia           | Tipo       | Descricao                                     |
|:--------------------|:-----------|:----------------------------------------------|
| `QmRncAcaoImediata`  | Formulario | Acao imediata vinculada a RNC — aba filha      |

---

## Campos

| # | Rotulo           | Coluna           | Tipo      | Tam | Obrig | Aba          | Grupo        | Opcoes / Comportamento                              |
|:--|:-----------------|:-----------------|:----------|:----|:------|:-------------|:-------------|:----------------------------------------------------|
| 1 | Id Acao          | `CODACIM` | INTEIRO   | -   | PK    | __main       | -            | readOnly, auto sequence                             |
| 2 | RNC              | `CODRNC`           | INTEIRO   | -   | Sim   | __main       | -            | readOnly. FK para THGQMGREG.CODRNC              |
| 3 | Prazo            | `DTPRAZO`        | DATA_HORA | -   | Nao   | __main       | Dados da Acao| Data limite para execucao da acao imediata          |
| 4 | Detalhe da Acao  | `ACAODETALHE`    | HTML      | -   | Sim   | Dados da Acao| -            | Editor HTML. Descricao detalhada da acao a executar |
| 5 | Nao se Aplica    | `NAOSEAPLICA`    | CHECKBOX  | 1   | Nao   | __main       | -            | S=Fase nao aplicavel. Permite avancar sem preencher |
| 6 | Aberto por       | `CODUSU`         | PESQUISA  | -   | Nao   | __main       | -            | readOnly. Auto: $ctx_usuario_logado                 |
| 7 | Data Acao        | `DHCREATE`      | DATA_HORA | -   | Nao   | __main       | -            | readOnly. Auto: $ctx_dh_atual. visivel=N            |
| 8 | Alterado em      | `DHALTER`        | DATA_HORA | -   | Nao   | __main       | -            | readOnly. Trigger BIU. visivel=N                    |

---

## Tabelas Filhas (grids na tela)

Nao possui tabelas filhas. Esta tela e uma aba filha da tela de Registro de RNC (Card 001).

---

## Action Buttons

| Botao              | Classe Java                          | Instancia          | Transacao | Descricao                                       |
|:-------------------|:-------------------------------------|:-------------------|:----------|:------------------------------------------------|
| Mudar Fase         | `MudarFaseAcaoImediataActionButton`  | `QmRncAcaoImediata` | AUTOMATIC | Avanca RNC para Fase 3 (Causa Raiz)             |
| Voltar Fase        | `VoltarFaseAcaoImediataActionButton` | `QmRncAcaoImediata` | AUTOMATIC | Retorna RNC para Fase 1 (Registro)              |
| Cancelar RNC       | `CancelarRncAcaoImediataActionButton`| `QmRncAcaoImediata` | AUTOMATIC | Cancela a RNC (STATUS='C')                      |
| Enviar Notificacao | `NotificarAcaoImediataActionButton`  | `QmRncAcaoImediata` | AUTOMATIC | Envia e-mail ao responsavel via MSDFilaMensagem |

---

## Regras de Negocio

1. Tela so e acessivel quando THGQMGREG.CODFASE = 2
2. Campo ACAODETALHE e obrigatorio para avancar fase, exceto se NAOSEAPLICA='S'
3. Se NAOSEAPLICA='S', o campo ACAODETALHE nao precisa ser preenchido
4. Ao clicar em Mudar Fase, FaseRncBusinessService avanca CODFASE para 3
5. Apenas uma acao imediata por RNC nesta fase (registro unico por CODRNC)
6. Toda mudanca registra entrada em THGQMGLOG

---

## Gatilhos e Automatismos

| Evento           | O que acontece                                        | Onde implementar              |
|:-----------------|:------------------------------------------------------|:------------------------------|
| INSERT           | CODACIM via sequence, DHCREATE=now()          | Listener BI           |
| UPDATE           | DHALTER = SYSDATE/GETDATE()                           | Trigger BIU (V1.xml)          |
| Mudar Fase (btn) | THGQMGREG.CODFASE=3, registra THGQMGREGFASE  | FaseRncBusinessService         |
| Voltar Fase (btn)| THGQMGREG.CODFASE=1, registra THGQMGREGFASE  | FaseRncBusinessService         |

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — adicionar DDL THGQMGACIM, Listener BI/BIU, sequence

### Dicionario de Dados
- [ ] `datadictionary/THGQMGACIM.xml` — instancia, campos, grupo "Dados da Acao"

### Backend Java
- [ ] `model/.../actionButtons/MudarFaseAcaoImediataActionButton.java`
- [ ] `model/.../actionButtons/VoltarFaseAcaoImediataActionButton.java`
- [ ] `model/.../actionButtons/CancelarRncAcaoImediataActionButton.java`
- [ ] `model/.../actionButtons/NotificarAcaoImediataActionButton.java`

### Menu
- [x] `datadictionary/menu.xml` — instancia QmRncAcaoImediata ja registrada como item 02

---

## Observacoes Tecnicas

- Tela e aba filha de QmRncRegistro — exibida dentro do formulario de RNC
- Campo ACAODETALHE usa dataType=HTML — editor rico de texto
- CODRNC e readOnly — preenchido automaticamente pela relacao pai-filho
- Grupo "Dados da Acao" agrupa: DTPRAZO, ACAODETALHE, CODUSU, DHCREATE
- instanceName dos @ActionButton: `QmRncAcaoImediata` — copiar exatamente do XML
