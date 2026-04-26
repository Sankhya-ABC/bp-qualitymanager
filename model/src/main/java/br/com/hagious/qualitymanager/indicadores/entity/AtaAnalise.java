package br.com.hagious.qualitymanager.indicadores.entity;

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
@JapeEntity(entity = "ThgAcrAta", table = "THGQMGATA")
public class AtaAnalise {

    @Id
    @Column(name = "CODACR")
    private BigDecimal codAcr;

    @Column(name = "NUMACR")
    private String numAcr;

    @Column(name = "PERIODO")
    private String periodo;

    @Column(name = "DTREUNIAO")
    private Timestamp dtReuniao;

    @Column(name = "LOCAL")
    private String local;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CONCLUSAO")
    private String conclusao;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
