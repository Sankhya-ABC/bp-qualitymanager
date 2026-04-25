package br.com.hagious.qualitymanager.rnc.entity;

import br.com.sankhya.studio.persistence.Column;
import br.com.sankhya.studio.persistence.Id;
import br.com.sankhya.studio.persistence.JapeEntity;
import br.com.sankhya.studio.persistence.JoinColumn;
import br.com.sankhya.studio.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JapeEntity(entity = "QmNcRegistro", table = "THGQMGREG")
public class RegistroRnc {

    @Id
    @Column(name = "CODRNC")
    private BigDecimal codRnc;

    @Column(name = "NURNC")
    private BigDecimal nuRnc;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "FASEATUAL")
    private String faseAtual;

    @Column(name = "ORIGEM")
    private String origem;

    @Column(name = "CODPARC")
    private BigDecimal codParc;

    @Column(name = "ATIVO")
    private String ativo;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHALTER")
    private Timestamp dhAlter;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
