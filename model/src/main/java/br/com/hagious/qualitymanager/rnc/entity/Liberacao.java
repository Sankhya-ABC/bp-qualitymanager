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
@JapeEntity(entity = "ThgRncLiberacao", table = "THGQMGLIB")
public class Liberacao {

    @Id
    @Column(name = "CODLIB")
    private BigDecimal codLib;

    @Column(name = "CODRNC")
    private BigDecimal codRnc;

    @Column(name = "PRODUTO")
    private String produto;

    @Column(name = "QUANTIDADE")
    private BigDecimal quantidade;

    @Column(name = "UNIDADE")
    private String unidade;

    @Column(name = "DECISAO")
    private String decisao;

    @Column(name = "JUSTIFICATIVA")
    private String justificativa;

    @Column(name = "CODPARC")
    private BigDecimal codParc;

    @Column(name = "DTLIBERACAO")
    private Timestamp dtLiberacao;

    @Column(name = "NAOSEAPLICA")
    private String naoSeAplica;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
