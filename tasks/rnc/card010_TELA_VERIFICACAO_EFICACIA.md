# Card 010 — Tela Verificacao de Eficacia (Fase 10)

## Identificacao
| Atributo        | Valor                                         |
|:----------------|:----------------------------------------------|
| Modulo          | RNC — Registro de Nao Conformidades                       |
| Fase Roadmap    | Fase 1 — Core + Modulo NC/RNC                 |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 10.2.1f     |
| Ordem no modulo | 010 de 014                                    |
| Depende de      | Card 001 (THGQMGREG)                    |

---

## Tabela Principal

| Atributo  | Valor                |
|:----------|:---------------------|
| Nome      | `THGQMGEFIC`    |
| Instancia | `QmRncEficacia`       |
| Sequencia | AUTO (`CODEFIC`)  |
| Dual-DB   | Oracle + SQL Server  |

---

## Campos

| # | Rotulo                | Coluna          | Tipo      | Tam | Obrig | Aba    | Grupo | Opcoes / Comportamento                                                    |
|:--|:----------------------|:----------------|:----------|:----|:------|:-------|:------|:--------------------------------------------------------------------------|
| 1 | Id Verificacao        | `CODEFIC`    | INTEIRO   | -   | PK    | __main | -     | readOnly, auto sequence                                                   |
| 2 | RNC                   | `CODRNC`          | INTEIRO   | -   | Sim   | __main | -     | readOnly. FK para THGQMGREG.CODRNC                                    |
| 3 | Resultado             | `RESULTADO`     | LISTA     | 1   | Sim   | __main | -     | E=Eficaz (encerra RNC), I=Ineficaz (retorna para Fase 5), P=Parcialmente eficaz |
| 4 | Descricao             | `DESCRICAO`     | HTML      | -   | Sim   | __main | -     | Descricao da verificacao realizada e evidencias coletadas                 |
| 5 | Responsavel           | `CODPARC`       | PESQUISA  | -   | Nao   | __main | -     | targetInstance=Parceiro. Quem realizou a verificacao                      |
| 6 | Data Verificacao      | `DTVERIFICACAO` | DATA_HORA | -   | Nao   | __main | -     | Data da verificacao de eficacia                                           |
| 7 | Aberto por            | `CODUSU`        | PESQUISA  | -   | Nao   | __main | -     | readOnly. Auto: $ctx_usuario_logado. visivel=N                            |
| 8 | Criado em             | `DHCREATE`     | DATA_HORA | -   | Nao   | __main | -     | readOnly. Auto: $ctx_dh_atual. visivel=N                                  |

---

## Tabelas Filhas (grids na tela)

| Tabela filha      | Instancia filha   | Vinculo FK        | Descricao                                              |
|:------------------|:------------------|:------------------|:-------------------------------------------------------|
| `THGQMGEVID`| `QmRncEvidencia`   | `CODRNC` + ORIGEM=1 | Evidencias de verificacao de eficacia (ORIGEM=1)       |

---

## Action Buttons

| Botao              | Classe Java                        | Instancia      | Transacao | Descricao                                                          |
|:-------------------|:-----------------------------------|:---------------|:----------|:-------------------------------------------------------------------|
| Encerrar RNC       | `EncerrarRncEficaciaActionButton`  | `QmRncEficacia` | AUTOMATIC | Se RESULTADO='E': STATUS='E', DTENCERRAMENTO=now(). RNC encerrada  |
| Reabrir para Fase 5| `ReabrirRncEficaciaActionButton`   | `QmRncEficacia` | AUTOMATIC | Se RESULTADO='I': CODFASE=5 (Acoes Corretivas). RNC reaberta        |
| Voltar Fase        | `VoltarFaseEficaciaActionButton`   | `QmRncEficacia` | AUTOMATIC | Retorna RNC para Fase 9 (Liberacao de Produto)                     |
| Cancelar RNC       | `CancelarRncEficaciaActionButton`  | `QmRncEficacia` | AUTOMATIC | Cancela a RNC (STATUS='C')                                         |

---

## Regras de Negocio

1. Tela so e acessivel quando THGQMGREG.CODFASE = 10
2. RESULTADO e obrigatorio — nao pode avancar sem definir o resultado
3. Se RESULTADO='E' (Eficaz): FaseRncBusinessService.encerrarRnc() — STATUS='E', DTENCERRAMENTO=now(), audit log
4. Se RESULTADO='I' (Ineficaz): FaseRncBusinessService.reabrirParaFase5() — CODFASE=5, STATUS='P', nova acao corretiva obrigada
5. Se RESULTADO='P' (Parcialmente eficaz): gestor decide manualmente o proximo passo
6. REINCIDENTE em THGQMGREG e verificado: se a RNC foi reaberta de Fase 10 para Fase 5, REINCIDENTE='S' na nova RNC gerada
7. Evidencias desta fase usam ORIGEM=1 (Verificacao de Eficacia) em THGQMGEVID
8. KPI "Taxa de Eficacia" calculado sobre este campo RESULTADO — base para dashboard

---

## Gatilhos e Automatismos

| Evento                   | O que acontece                                                   | Onde implementar             |
|:-------------------------|:-----------------------------------------------------------------|:-----------------------------|
| RESULTADO='E' + Encerrar | STATUS='E', DTENCERRAMENTO=now(), audit log "ENCERRADO_EFICAZ"   | FaseRncBusinessService        |
| RESULTADO='I' + Reabrir  | CODFASE=5, STATUS='P', historico, audit log "REABERTO_INEFICAZ"   | FaseRncBusinessService        |
| Encerramento             | REINCIDENTE verificado nas proximas NCs mesma origem+processo    | RncReincidenciaListener       |

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGEFIC, triggers, sequence

### Dicionario de Dados
- [ ] `datadictionary/THGQMGEFIC.xml`

### Backend Java
- [ ] `model/.../actionButtons/EncerrarRncEficaciaActionButton.java`
- [ ] `model/.../actionButtons/ReabrirRncEficaciaActionButton.java`
- [ ] `model/.../actionButtons/VoltarFaseEficaciaActionButton.java`
- [ ] `model/.../actionButtons/CancelarRncEficaciaActionButton.java`

### Menu
- [x] `datadictionary/menu.xml` — instancia QmRncEficacia ja registrada como item 10

---

## Observacoes Tecnicas

- Esta e a UNICA fase que encerra a RNC automaticamente — sem esta fase, a RNC nunca fecha
- RESULTADO='I' reinicia o ciclo a partir da Fase 5 — pode acontecer N vezes
- Evidencias se dividem por ORIGEM: 1=Verificacao, 2=Implementacao — a grid filha filtra pelo ORIGEM correto
- instanceName: `QmRncEficacia` — copiar exatamente do XML
