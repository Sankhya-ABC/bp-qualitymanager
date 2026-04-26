package br.com.hagious.qualitymanager.fornecedor.entity;

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
@JapeEntity(entity = "ThgFornQualificacao", table = "THGQMGQUAL")
public class Qualificacao {

    @Id
    @Column(name = "CODQUAL")
    private BigDecimal codQual;

    @Column(name = "CODPARC")
    private BigDecimal codParc;

    @Column(name = "TIPOFORN")
    private String tipoForn;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "PONTUACAO")
    private BigDecimal pontuacao;

    @Column(name = "RESULTADOIQF")
    private String resultadoIqf;

    @Column(name = "DTQUALIFICACAO")
    private Timestamp dtQualificacao;

    @Column(name = "DTVALIDADE")
    private Timestamp dtValidade;

    @Column(name = "DTREAV")
    private Timestamp dtReav;

    @Column(name = "BLOQUEADO")
    private String bloqueado;

    @Column(name = "MOTIVOBLOQUEIO")
    private String motivoBloqueio;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
