# Card FORN-06 — Score Automatico de Fornecedor (Java)

## Identificacao
| Atributo        | Valor                                     |
|:----------------|:------------------------------------------|
| Modulo          | Fornecedores                              |
| Fase Roadmap    | Fase 2                                    |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 8.4.1c  |
| Ordem no modulo | 006 de 009                                |
| Depende de      | FORN-01, FORN-02, FORN-05                 |

---

## Descricao

Card de logica Java — nao cria tela. Cria o service e o listener responsaveis pelo calculo
automatico do score (IQF) do fornecedor apos o recebimento das respostas ao questionario.

---

## Classes Java

### ScoreFornBusinessService.java

| Metodo                    | Parametros       | O que faz                                                              |
|:--------------------------|:-----------------|:-----------------------------------------------------------------------|
| `calcularScore`           | idQualificacao   | Busca respostas, aplica pesos dos criterios, retorna BigDecimal        |
| `classificarResultado`    | pontuacao        | Retorna 'A' (>=80), 'B' (50-79), 'T' (<50)                            |
| `verificarBloqueio`       | codParc          | Conta NCs Criticas em 90 dias — se >= 2, bloqueia automaticamente      |
| `atualizarQualificacao`   | idQualificacao   | Persiste PONTUACAO, RESULTADOIQF e STATUS em THGQMGQUAL    |

### QualificacaoScoreListener.java

```
instanceName: "ThgFornResposta"   <- copiar EXATAMENTE do XML de THGQMGFRESP.xml
Evento: afterInsert
Logica:
  1. Buscar CODQUAL da resposta inserida
  2. Chamar ScoreFornBusinessService.calcularScore(idQualificacao)
  3. Chamar ScoreFornBusinessService.atualizarQualificacao(idQualificacao)
  4. Gravar em THGQMGLOG com ACAO='CALCULO_SCORE'
```

### Logica de calculo de score

```
Para cada resposta do questionario (CODQUAL):
  SE TIPORESPOSTA = 'S':
    SE RESPOSTA = 'SIM' -> pontos += 1.0
    SE RESPOSTA = 'NAO' -> pontos += 0.0
  SE TIPORESPOSTA = 'N':
    valor = Integer.parseInt(RESPOSTA)
    SE valor < 10   -> pontos += 0.15
    SE valor < 30   -> pontos += 0.30
    SE valor < 50   -> pontos += 0.45
    SE valor < 70   -> pontos += 0.60
    SE valor < 90   -> pontos += 0.75
    SE valor >= 90  -> pontos += 0.90

scoreRaw = BigDecimal(pontos).divide(BigDecimal(totalPerguntas), 4, HALF_UP)
           .multiply(BigDecimal(100))
score    = scoreRaw.setScale(2, HALF_UP)
```

---

## Regras Criticas de Implementacao

1. BigDecimal.divide() SEMPRE com escala e RoundingMode.HALF_UP — bug critico do produto legado
2. instanceName `ThgFornResposta` deve ser copiado do XML — nunca digitado manualmente
3. Listener nao deve lancar excecao que interrompa o INSERT da resposta — catch e log
4. AuditLogService.registrar() deve ser chamado ao final do calculo

---

## Artefatos

### Backend Java
- [ ] `model/.../services/ScoreFornBusinessService.java`
- [ ] `model/.../listeners/QualificacaoScoreListener.java`
- [ ] `model/.../exceptions/FornecedorBloqueadoException.java`
