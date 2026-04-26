# Card CORE-04 — Dominio Fases RNC

## Identificacao
| Atributo        | Valor                                    |
|:----------------|:-----------------------------------------|
| Modulo          | Core                                     |
| Fase Roadmap    | Fase 1                                   |
| ISO             | ABNT NBR ISO 9001:2015 — Clausula 8.7    |
| Ordem no modulo | 004 de 004                               |
| Depende de      | —                                        |

---

## Tabela

| Atributo  | Valor               |
|:----------|:--------------------|
| Nome      | `THGQMGRNCFASE`       |
| Instancia | `QmRncFase`          |
| Sequencia | MANUAL (`CODFASE`)   |
| Dual-DB   | Oracle + SQL Server |

### Instancias da tela
| Instancia  | Tipo       | Descricao                           |
|:-----------|:-----------|:------------------------------------|
| `QmRncFase` | Formulario | Cadastro das fases do workflow de NC|

---

## Campos

| # | Rotulo     | Coluna     | Tipo     | Tam | Obrig | Aba    | Grupo | Opcoes / Comportamento                                          |
|:--|:-----------|:-----------|:---------|:----|:------|:-------|:------|:----------------------------------------------------------------|
| 1 | Id Fase    | `CODFASE`   | INTEIRO  | -   | PK    | __main | -     | readOnly. Informado manualmente nos dados iniciais              |
| 2 | Descricao  | `DESCFASE` | TEXTO    | 100 | Sim   | __main | -     | isPresentation=S. Nome exibido na tela de NC                   |
| 3 | Ativo      | `ATIVO`    | CHECKBOX | 1   | Nao   | __main | -     | Default S. Se N, fase e pulada pelo FaseNcBusinessService       |

---

## Dados Iniciais (dbscripts/V1.xml — executar=SEMPRE com verificacao)

| CODFASE | DESCFASE                       |
|:-------|:-------------------------------|
| 1      | Registro de Nao Conformidade   |
| 2      | Acoes Imediatas                |
| 3      | Causa Raiz                     |
| 4      | Abrangencia                    |
| 5      | Acoes Corretivas               |
| 6      | Revisao de Documentos          |
| 7      | Riscos e Oportunidades         |
| 8      | Implementacao                  |
| 9      | Liberacao de Produto           |
| 10     | Verificacao de Eficacia        |

---

## Regras de Negocio

1. Dados iniciais inseridos via V1.xml com verificacao: `IF COUNT(*) = 0 THEN INSERT`
2. Fases com ATIVO='N' sao puladas pelo FaseNcBusinessService.avancarFase()
3. Empresa pode desativar fases que nao se aplicam ao seu processo (ex: Liberacao de Produto para empresas de servico)
4. CODFASE=1 e sempre o inicio — nunca pode ser desativado
5. CODFASE=10 e sempre o encerramento — nunca pode ser desativado

---

## Artefatos

### Banco de Dados
- [x] `dbscripts/V1.xml` — DDL THGQMGRNCFASE + INSERT dados iniciais das 10 fases

### Dicionario de Dados
- [x] `datadictionary/THGQMGRNCFASE.xml` — instancia QmRncFase

### Menu
- [ ] `datadictionary/menu.xml` — adicionar QmRncFase em pasta Configuracao

---

## Observacoes Tecnicas

- sequenceType=M (manual) — PK definida nos dados iniciais, nao gerada automaticamente
- CODFASE e campo de FK em THGQMGREG (campo CODFASE)
- instanceName: `QmRncFase` — copiar exatamente do XML
- FaseNcBusinessService faz: `SELECT CODFASE FROM THGQMGRNCFASE WHERE CODFASE > :atual AND ATIVO='S' ORDER BY CODFASE`
