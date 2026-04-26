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
@JapeEntity(entity = "QmRncRisco", table = "THGQMGRISCO")
public class Risco {

    @Id
    @Column(name = "CODRISCO")
    private BigDecimal codRisco;

    @Column(name = "CODRNC")
    private BigDecimal codRnc;

    @Column(name = "TIPORISCO")
    private String tipoRisco;

    @Column(name = "OQUE")
    private String oQue;

    @Column(name = "COMO")
    private String como;

    @Column(name = "ONDE")
    private String onde;

    @Column(name = "PORQUE")
    private String porQue;

    @Column(name = "QUANDO")
    private Timestamp quando;

    @Column(name = "QUANTO")
    private String quanto;

    @Column(name = "NAOSEAPLICA")
    private String naoSeAplica;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;

    @Column(name = "DHALTER")
    private Timestamp dhAlter;
}
