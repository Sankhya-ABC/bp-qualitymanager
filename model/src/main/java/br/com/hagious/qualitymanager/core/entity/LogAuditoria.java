package br.com.hagious.qualitymanager.core.entity;

import br.com.sankhya.studio.persistence.Column;
import br.com.sankhya.studio.persistence.Id;
import br.com.sankhya.studio.persistence.JapeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JapeEntity(entity = "QmLogAuditoria", table = "THGQMGLOG")
public class LogAuditoria {

    @Id
    @Column(name = "CODLOG")
    private BigDecimal codLog;

    @Column(name = "DTREGISTRO")
    private Timestamp dtRegistro;

    @Column(name = "MODULO")
    private String modulo;

    @Column(name = "ENTIDADE")
    private String entidade;

    @Column(name = "IDENTIDADE")
    private BigDecimal idEntidade;

    @Column(name = "ACAO")
    private String acao;

    @Column(name = "CAMPO_ALTERADO")
    private String campoAlterado;

    @Column(name = "VALOR_ANTERIOR")
    private String valorAnterior;

    @Column(name = "VALOR_NOVO")
    private String valorNovo;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;
}
