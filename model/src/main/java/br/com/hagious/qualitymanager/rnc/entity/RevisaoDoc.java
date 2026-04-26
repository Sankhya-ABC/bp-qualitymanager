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
@JapeEntity(entity = "QmNcRevisaoDoc", table = "THGQMGREVDOC")
public class RevisaoDoc {

    @Id
    @Column(name = "CODREVDOC")
    private BigDecimal codRevdoc;

    @Column(name = "CODRNC")
    private BigDecimal codRnc;

    @Column(name = "DOCUMENTO")
    private String documento;

    @Column(name = "MOTIVO")
    private String motivo;

    @Column(name = "CODPARC")
    private BigDecimal codParc;

    @Column(name = "DTPRAZO")
    private Timestamp dtPrazo;

    @Column(name = "NAOSEAPLICA")
    private String naoSeAplica;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;

    @Column(name = "DHALTER")
    private Timestamp dhAlter;
}
