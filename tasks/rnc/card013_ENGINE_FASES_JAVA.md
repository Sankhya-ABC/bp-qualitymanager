# Card 013 — Engine de Fases da RNC (Java)

## Identificacao
| Atributo        | Valor                                         |
|:----------------|:----------------------------------------------|
| Modulo          | RNC — Registro de Nao Conformidades                       |
| Fase Roadmap    | Fase 1 — Core + Modulo NC/RNC                 |
| ISO             | ABNT NBR ISO 9001:2015 — Clausulas 8.7 + 10.2 |
| Ordem no modulo | 013 de 014                                    |
| Depende de      | Todos os cards 001 a 012                      |

---

## Descricao

Este card nao cria tela — cria a logica Java central que alimenta todos os action buttons
do modulo RNC. Todos os botoes "Mudar Fase", "Voltar Fase" e "Cancelar RNC" delegam
para este servico. Nenhum action button contem SQL ou logica de negocio diretamente.

---

## Classes Java

### BaseRncActionButton.java (abstrata — em services/)

```
Pacote: br.com.bpabc.addon.qualitymanager.services
Funcao: classe abstrata com o loop de registros e tratamento de erro padrao
Herda: AcaoRotinaJava (SDK Sankhya)
Metodo abstrato: executar(ContextoAcao ctx, Registro linha)
Tratamento de erro: QualityManagerException -> ctx.setMensagemRetorno(e.getMsgUsuario())
Log obrigatorio: [QM-NC] prefixo em todos os logs
```

### FaseRncBusinessService.java (em services/)

| Metodo                 | Parametros              | O que faz                                                                |
|:-----------------------|:------------------------|:-------------------------------------------------------------------------|
| `avancarFase`          | rncId, origem           | Calcula proxima fase (com desvio por origem), atualiza CODFASE, registra historico e audit log |
| `retornarFase`         | rncId, statusAtual      | Volta CODFASE-1, registra historico. Bloqueia se STATUS='C' ou CODFASE=1  |
| `cancelarRnc`          | rncId                   | STATUS='C', DTENCERRAMENTO=now(), registra audit log "CANCELADO"         |
| `encerrarRnc`          | rncId                   | STATUS='E', DTENCERRAMENTO=now(), registra audit log "ENCERRADO_EFICAZ"  |
| `reabrirParaFase5`     | rncId                   | CODFASE=5, STATUS='P', registra audit log "REABERTO_INEFICAZ"             |

### Logica de desvio de fluxo em avancarFase()

```
SE origem da RNC contiver "RISCOS" ou "OPORTUNIDADES" (consulta THGQMGCFG ou dominio):
    proximaFase = 5   (pula para Acoes Corretivas)
SENAO:
    proximaFase = faseAtual + 1

SE proximaFase = 10 E resultado da eficacia = 'E':
    encerrarRnc(rncId)
```

### RncReincidenciaListener.java (em listeners/)

```
instanceName: "QmRncRegistro"  <- copiar EXATAMENTE do XML
Evento: afterInsert
Logica: busca RNCs anteriores com mesma ORIGEM+PROCESSO nao encerradas (STATUS != 'E' e != 'C')
        Se encontrar: REINCIDENTE='S', NCVINCULADA=CODRNC da mais recente anterior
```

### NotificacaoRncService.java (em services/)

| Metodo                    | Parametros                  | O que faz                                                      |
|:--------------------------|:----------------------------|:---------------------------------------------------------------|
| `enviaNotificacaoPorFase` | rncId, codParc, origem, enviarEmail | Monta assunto/mensagem por ORIGEM, busca EMAIL em TGFPAR, insere em MSDFilaMensagem |

---

## Excecoes customizadas

| Classe                   | Quando usar                                        |
|:-------------------------|:---------------------------------------------------|
| `RncValidacaoException`   | Campo obrigatorio nao preenchido, estado invalido  |
| `FaseInvalidaException`  | Tentativa de mover RNC para fase invalida          |
| `QualityManagerException`| Base — mensagemUsuario em PT-BR + causa tecnica no log |

---

## Regras criticas de implementacao

1. NUNCA colocar SQL dentro de ActionButton — apenas chamada para FaseRncBusinessService
2. BigDecimal.divide() SEMPRE com escala: `.divide(total, 2, RoundingMode.HALF_UP)`
3. instanceName do @Listener DEVE ser copiado literalmente do XML — erro silencioso se errado
4. Audit log OBRIGATORIO em toda mudanca de STATUS ou CODFASE via THGQMGLOG
5. Log Java: prefixo `[QM-NC]` em todos os pontos relevantes
6. Catch: nunca silencioso — sempre logar e propagar mensagem ao usuario
7. Registro em THGQMGREGFASE a cada transicao (historico completo de fases)

---

## Artefatos

### Backend Java
- [ ] `model/.../services/BaseRncActionButton.java` — classe abstrata base
- [ ] `model/.../services/FaseRncBusinessService.java` — engine central
- [ ] `model/.../services/NotificacaoRncService.java` — envio de e-mails
- [ ] `model/.../listeners/RncReincidenciaListener.java` — deteccao de reincidencia
- [ ] `model/.../exceptions/RncValidacaoException.java`
- [ ] `model/.../exceptions/FaseInvalidaException.java`
- [ ] `model/.../exceptions/QualityManagerException.java` — ja criada no core

### Banco de Dados
- [ ] `dbscripts/V1.xml` — DDL THGQMGREGFASE (historico de fases), Listener BI, sequence

### Dicionario de Dados
- Sem tela propria — suporte a todos os outros cards do modulo RNC
