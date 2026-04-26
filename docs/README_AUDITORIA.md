# Modulo Auditoria

ISO 9001 clausula 9.2 (Auditoria interna).

## Funcionalidades

- Programa anual de auditorias
- Registro de auditorias (interna, externa, fornecedor)
- Plano de auditoria com atividades e responsaveis
- Checklist por clausula ISO (Conforme/Nao Conforme/Oport. Melhoria)
- Constatacoes com vinculo para abertura de NC
- Upload de evidencias (fotos, documentos, registros)
- Controle de participantes (abertura e encerramento)
- Cadastro de auditores com certificacao

## Tabelas

| Tabela | Instance | Descricao |
|:-------|:---------|:----------|
| THGQMGAUDTOR | ThgAudAuditor | Cadastro de auditores |
| THGQMGAUDPROG | ThgAudPrograma | Programa anual de auditoria |
| THGQMGAUD | ThgAudRegistro | Registro da auditoria |
| THGQMGAUDPLAN | ThgAudPlano | Plano de auditoria (atividades) |
| THGQMGAUDCHK | ThgAudChecklist | Checklist por clausula |
| THGQMGAUDCON | ThgAudConstatacao | Constatacoes (NC Maior/Menor/OM) |
| THGQMGAUDEVID | ThgAudEvidencia | Evidencias (foto/doc/registro) |
| THGQMGAUDPART | ThgAudParticipante | Participantes (abertura/encerramento) |

## Tipos de Auditoria

| Tipo | Descricao |
|:-----|:----------|
| I | Interna |
| E | Externa |
| F | Fornecedor |

## Resultados do Checklist

| Resultado | Descricao |
|:----------|:----------|
| C | Conforme |
| NC | Nao Conforme |
| OM | Oportunidade de Melhoria |
| NA | Nao Aplicavel |

## Tipos de Constatacao

| Tipo | Descricao | Acao |
|:-----|:----------|:-----|
| NM | NC Maior | Gera RNC automaticamente (prioridade Critico) |
| Nm | NC Menor | Gera RNC automaticamente (prioridade Simples) |
| OM | Oportunidade Melhoria | Registro apenas, sem RNC |

## Artefatos
- dbscripts: V044-V051 (8 scripts)
- datadictionary: 8 XMLs
- Java: 8 entities, 8 repositories
