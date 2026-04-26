# Card 003 — Tela Causa Raiz — Ishikawa 6M (Fase 3)

## Identificacao
| Atributo        | Valor                                          |
|:----------------|:-----------------------------------------------|
| Modulo          | RNC — Registro de Nao Conformidades                        |
| Fase Roadmap    | Fase 1 — Core + Modulo NC/RNC                  |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 10.2.1c      |
| Ordem no modulo | 003 de 014                                     |
| Depende de      | Card 001 (THGQMGREG)                     |

---

## Tabela Principal

| Atributo  | Valor                 |
|:----------|:----------------------|
| Nome      | `THGQMGCR`   |
| Instancia | `QmRncCausaRaiz`       |
| Sequencia | AUTO (`CODCR`)  |
| Dual-DB   | Oracle + SQL Server   |

### Instancias da tela
| Instancia       | Tipo       | Descricao                               |
|:----------------|:-----------|:----------------------------------------|
| `QmRncCausaRaiz` | Formulario | Analise de causa raiz — Ishikawa 6M     |

---

## Campos

| # | Rotulo          | Coluna         | Tipo     | Tam  | Obrig | Aba           | Grupo        | Opcoes / Comportamento                                      |
|:--|:----------------|:---------------|:---------|:-----|:------|:--------------|:-------------|:------------------------------------------------------------|
| 1 | Id Causa        | `CODCR`  | INTEIRO  | -    | PK    | __main        | -            | readOnly, auto sequence                                     |
| 2 | RNC             | `CODRNC`         | INTEIRO  | -    | Sim   | __main        | -            | readOnly. FK para THGQMGREG.CODRNC                      |
| 3 | Mao de Obra     | `MAO_OBRA`     | HTML     | -    | Nao   | Ishikawa 6M   | Mao de Obra  | Causas relacionadas a pessoas e competencias                |
| 4 | Metodo          | `METODO`       | HTML     | -    | Nao   | Ishikawa 6M   | Metodo       | Causas relacionadas a processos e procedimentos             |
| 5 | Maquina         | `MAQUINA`      | HTML     | -    | Nao   | Ishikawa 6M   | Maquina      | Causas relacionadas a equipamentos e ferramentas            |
| 6 | Medida          | `MEDIDA`       | HTML     | -    | Nao   | Ishikawa 6M   | Medida       | Causas relacionadas a medicoes e indicadores                |
| 7 | Material        | `MATERIAL`     | HTML     | -    | Nao   | Ishikawa 6M   | Material     | Causas relacionadas a materias-primas e insumos             |
| 8 | Meio Ambiente   | `MEIO_AMBIENTE`| HTML     | -    | Nao   | Ishikawa 6M   | Meio Ambiente| Causas relacionadas ao ambiente fisico e organizacional     |
| 9 | Nao se Aplica   | `NAOSEAPLICA`  | CHECKBOX | 1    | Nao   | __main        | -            | S=Fase nao aplicavel. Permite avancar sem preencher os 6M   |
| 10| Aberto por      | `CODUSU`       | PESQUISA | -    | Nao   | __main        | -            | readOnly. Auto: $ctx_usuario_logado. visivel=N              |
| 11| Criado em       | `DHCREATE`    | DATA_HORA| -    | Nao   | __main        | -            | readOnly. Auto: $ctx_dh_atual. visivel=N                    |
| 12| Alterado em     | `DHALTER`      | DATA_HORA| -    | Nao   | __main        | -            | readOnly. Trigger BIU. visivel=N                            |

---

## Tabelas Filhas (grids na tela)

Nao possui tabelas filhas. Esta tela e uma aba filha da tela de Registro de RNC (Card 001).

---

## Action Buttons

| Botao              | Classe Java                        | Instancia       | Transacao | Descricao                                    |
|:-------------------|:-----------------------------------|:----------------|:----------|:---------------------------------------------|
| Mudar Fase         | `MudarFaseCausaRaizActionButton`   | `QmRncCausaRaiz` | AUTOMATIC | Avanca RNC para Fase 4 (Abrangencia)         |
| Voltar Fase        | `VoltarFaseCausaRaizActionButton`  | `QmRncCausaRaiz` | AUTOMATIC | Retorna RNC para Fase 2 (Acoes Imediatas)    |
| Cancelar RNC       | `CancelarRncCausaRaizActionButton` | `QmRncCausaRaiz` | AUTOMATIC | Cancela a RNC (STATUS='C')                   |

---

## Regras de Negocio

1. Tela so e acessivel quando THGQMGREG.CODFASE = 3
2. Ao menos um dos 6M deve ser preenchido para avancar fase, exceto se NAOSEAPLICA='S'
3. Se NAOSEAPLICA='S', todos os campos 6M podem ficar vazios
4. Os 6 campos M sao independentes entre si — nenhum e obrigatorio individualmente
5. Toda mudanca registra entrada em THGQMGLOG
6. Desvio de fluxo: se ORIGEM da RNC contiver "Riscos" ou "Oportunidades", FaseNcBusinessService pula da Fase 3 direto para a Fase 5 (Acoes Corretivas)

---

## Gatilhos e Automatismos

| Evento           | O que acontece                                       | Onde implementar    |
|:-----------------|:-----------------------------------------------------|:--------------------|
| INSERT           | CODCR via sequence, DHCREATE=now()            | Listener BI |
| UPDATE           | DHALTER = SYSDATE/GETDATE()                          | Trigger BIU         |
| Mudar Fase (btn) | CODFASE=4 (ou 5 se desvio por origem), reg. historico | FaseNcBusinessService |
| Voltar Fase (btn)| CODFASE=2, registra historico                         | FaseNcBusinessService |

---

## Artefatos

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGCR, Listener BI/BIU, sequence

### Dicionario de Dados
- [ ] `datadictionary/THGQMGCR.xml` — instancia, 6 campos HTML por M, aba "Ishikawa 6M"

### Backend Java
- [ ] `model/.../actionButtons/MudarFaseCausaRaizActionButton.java`
- [ ] `model/.../actionButtons/VoltarFaseCausaRaizActionButton.java`
- [ ] `model/.../actionButtons/CancelarRncCausaRaizActionButton.java`

### Menu
- [x] `datadictionary/menu.xml` — instancia QmRncCausaRaiz ja registrada como item 03

---

## Observacoes Tecnicas

- Os 6 campos do Ishikawa (MAO_OBRA, METODO, MAQUINA, MEDIDA, MATERIAL, MEIO_AMBIENTE) usam dataType=HTML
- Todos ficam na aba "Ishikawa 6M" — cada M em seu proprio grupo nomeado
- Desvio de fluxo por origem: se TGQCADASTROS/THGQMGPRIO do tipo Riscos/Oportunidades — implementar em FaseNcBusinessService
- instanceName: `QmRncCausaRaiz` — copiar exatamente do XML
