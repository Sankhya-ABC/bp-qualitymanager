package br.com.hagious.qualitymanager.mudancas.entity;

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
@JapeEntity(entity = "QmGmAvaliacao", table = "THGQMGGMAVAL")
public class AvaliacaoRisco {

    @Id
    @Column(name = "CODAVALRISCO")
    private BigDecimal codAvalRisco;

    @Column(name = "CODGM")
    private BigDecimal codGm;

    @Column(name = "CODACAOGM")
    private BigDecimal codAcaoGm;

    @Column(name = "RISCO")
    private String risco;

    @Column(name = "PROBABILIDADE")
    private String probabilidade;

    @Column(name = "IMPACTO")
    private String impacto;

    @Column(name = "NIVELRISCO")
    private String nivelRisco;

    @Column(name = "MITIGACAO")
    private String mitigacao;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;
}
