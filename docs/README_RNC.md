# Modulo RNC — Nao Conformidades

ISO 9001 clausulas 8.7 (Controle de saidas nao conformes) e 10.2 (Nao conformidade e acao corretiva).

## Workflow de 10 Fases

```
1. Registro → 2. Acoes Imediatas → 3. Causa Raiz (Ishikawa 6M)
→ 4. Abrangencia (5W2H) → 5. Acoes Corretivas (5W2H)
→ 6. Revisao Documentos → 7. Riscos/Oportunidades
→ 8. Implementacao → 9. Liberacao Produto
→ 10. Verificacao Eficacia → [Encerramento ou Retorno Fase 5]
```

**Desvio de fluxo:** Se ORIGEM=Auditoria ou Outro, fase 4 (Abrangencia) e pulada.
**Fases desativaveis:** Empresa pode desativar fases nao aplicaveis em THGQMGNCFASE.

## Tabelas

| Tabela | Instance | Descricao | Fase |
|:-------|:---------|:----------|:-----|
| THGQMGREG | QmNcRegistro | Registro principal da RNC | 1 |
| THGQMGACIM | QmNcAcaoImediata | Acoes imediatas | 2 |
| THGQMGCR | QmNcCausaRaiz | Causa raiz (Ishikawa 6M) | 3 |
| THGQMGABR | QmNcAbrangencia | Abrangencia 5W2H | 4 |
| THGQMGNCQUEM | QmNcQuem | Responsaveis da abrangencia | 4 |
| THGQMGACCOR | QmNcAcaoCorretiva | Acoes corretivas 5W2H | 5 |
| THGQMGREVDOC | QmNcRevisaoDoc | Revisao de documentos | 6 |
| THGQMGRISCO | QmNcRisco | Riscos e oportunidades | 7 |
| THGQMGIMPL | QmNcImplementacao | Implementacao | 8 |
| THGQMGLIB | QmNcLiberacao | Liberacao de produto | 9 |
| THGQMGEFIC | QmNcEficacia | Verificacao de eficacia | 10 |
| THGQMGEVID | QmNcEvidencia | Evidencias (fotos, docs) | 8+10 |
| THGQMGRESP | QmNcResponsavel | Responsaveis por fase | Todas |
| THGQMGREGFASE | QmNcRegFase | Historico de transicoes | — |

## Campos Principais (THGQMGREG)

| Campo | Tipo | Comportamento |
|:------|:-----|:-------------|
| CODRNC | PK AUTO | Identificador |
| NURNC | TEXTO(20) | Formato NC-YYYY-NNNN (gerado automaticamente) |
| STATUS | LISTA | A=Aberta, P=Andamento, E=Encerrada, C=Cancelada, X=Nao procedente |
| CODFASE | INTEIRO | FK para THGQMGNCFASE (1-10) |
| ORIGEM | LISTA | C=Cliente, F=Fornecedor, I=Interno, A=Auditoria, O=Outro |
| PRIORIDADE | LISTA | 1=Simples, 2=Prioritario, 3=Critico |
| TIPONC | LISTA | P=Produto, S=Servico, PR=Processo, SG=Sist. Gestao |
| DTPREVENCERRAR | DATA_HORA | Calculado: DTREGISTRO + prazo da prioridade |
| REINCIDENTE | CHECKBOX | Marcado automaticamente pelo sistema |
| DETALHAMENTO | HTML | Editor rico |

## Services

### FaseNcBusinessService
Engine central do workflow. Metodos:
- `avancarFase(rncId, origem)` — calcula proxima fase (com desvio e skip de inativas)
- `retornarFase(rncId)` — volta uma fase (bloqueia se cancelada ou fase 1)
- `cancelarRnc(rncId)` — STATUS=C, DTENCERRAMENTO=now
- `encerrarRnc(rncId)` — STATUS=E, DTENCERRAMENTO=now (eficacia verificada)
- `reabrirParaFase5(rncId)` — volta pra fase 5 (verificacao ineficaz)

### NcReincidenciaListener
Listener afterInsert em QmNcRegistro. Busca NCs anteriores com mesma ORIGEM+PROCESSO nao encerradas. Se encontrar, marca REINCIDENTE=S e vincula.

## Regras de Negocio
1. STATUS e CODFASE gerenciados exclusivamente por FaseNcBusinessService
2. Toda mudanca de STATUS/CODFASE registrada em THGQMGLOG
3. RNC cancelada nao pode avancar/retornar de fase
4. Verificacao eficaz (fase 10, RESULTADO=E) encerra RNC
5. Verificacao ineficaz (RESULTADO=I) reabre para fase 5
6. NAOPROCEDENTE=S muda STATUS para X

## Artefatos
- dbscripts: V007-V020 (14 scripts)
- datadictionary: 14 XMLs
- Java: 14 entities, 14 repositories, 1 service, 1 listener, 3 exceptions
