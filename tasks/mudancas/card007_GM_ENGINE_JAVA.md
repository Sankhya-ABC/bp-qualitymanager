# Card GM-07 — Engine de Fases GM (Java)

## Identificacao
| Atributo        | Valor                                   |
|:----------------|:----------------------------------------|
| Modulo          | Gestao de Mudancas                      |
| Fase Roadmap    | Fase 3                                  |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 6.3   |
| Ordem no modulo | 007 de 008                              |
| Depende de      | Cards GM-01 a GM-06                     |

## Classes Java

### FaseGmBusinessService.java
| Metodo                     | O que faz                                                               |
|:---------------------------|:------------------------------------------------------------------------|
| `avancarFase`              | CODFASE+1, registra historico, audit log, chama criaQuestionariosGestao |
| `retornarFase`             | CODFASE-1, bloqueia se STATUS='C' ou 'R'                                |
| `cancelarGestao`           | STATUS='X', audit log                                                   |
| `aprovarGestao`            | Verifica todos aprovadores STATUS='C' -> THGQMGGM.STATUS='C'     |
| `criaQuestionariosGestao`  | Ao avancar para fase 2, 4 ou 5: cria THGQMGGMQUEST automaticamente |

### Logica de criaQuestionariosGestao
```
SE fase = 2 OU fase = 4 OU fase = 5:
  buscar THGQMGFQUEST WHERE ORIGEM != 1 AND ATIVO='S'
  para cada questionario:
    SE NAO EXISTE (CODQUEST + CODGM) em THGQMGGMQUEST:
      INSERT THGQMGGMQUEST
      para cada THGQMGFPERG do questionario:
        INSERT THGQMGGMPERG
```

## Artefatos
- [ ] `model/.../services/FaseGmBusinessService.java`
