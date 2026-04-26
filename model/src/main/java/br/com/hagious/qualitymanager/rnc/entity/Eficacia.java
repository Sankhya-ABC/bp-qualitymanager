package br.com.hagious.qualitymanager.rnc.entity;

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
@JapeEntity(entity = "ThgRncEficacia", table = "THGQMGEFIC")
public class Eficacia {

    @Id
    @Column(name = "CODEFIC")
    private BigDecimal codEfic;

    @Column(name = "CODRNC")
    private BigDecimal codRnc;

    @Column(name = "RESULTADO")
    private String resultado;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "CODPARC")
    private BigDecimal codParc;

    @Column(name = "DTVERIFICACAO")
    private Timestamp dtVerificacao;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
