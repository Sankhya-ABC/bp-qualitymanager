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
@JapeEntity(entity = "QmRncImplementacao", table = "THGQMGIMPL")
public class Implementacao {

    @Id
    @Column(name = "CODIMPL")
    private BigDecimal codImpl;

    @Column(name = "CODRNC")
    private BigDecimal codRnc;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "CODPARC")
    private BigDecimal codParc;

    @Column(name = "DTIMPLEMENTACAO")
    private Timestamp dtImplementacao;

    @Column(name = "NAOSEAPLICA")
    private String naoSeAplica;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;

    @Column(name = "DHALTER")
    private Timestamp dhAlter;
}
