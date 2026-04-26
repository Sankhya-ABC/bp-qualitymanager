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
@JapeEntity(entity = "QmRncAcaoCorretiva", table = "THGQMGACCOR")
public class AcaoCorretiva {

    @Id
    @Column(name = "CODACCOR")
    private BigDecimal codAccor;

    @Column(name = "CODRNC")
    private BigDecimal codRnc;

    @Column(name = "DTPRAZO")
    private Timestamp dtPrazo;

    @Column(name = "DETALHEACAO")
    private String detalheAcao;

    @Column(name = "NAOSEAPLICA")
    private String naoSeAplica;

    @Column(name = "OQUE")
    private String oQue;

    @Column(name = "COMO")
    private String como;

    @Column(name = "ONDE")
    private String onde;

    @Column(name = "PORQUE")
    private String porque;

    @Column(name = "QUANDO")
    private Timestamp quando;

    @Column(name = "QUANTO")
    private String quanto;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;

    @Column(name = "DHALTER")
    private Timestamp dhAlter;
}
