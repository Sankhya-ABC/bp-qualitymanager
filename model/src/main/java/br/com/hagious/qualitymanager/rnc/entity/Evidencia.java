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
@JapeEntity(entity = "QmRncEvidencia", table = "THGQMGEVID")
public class Evidencia {

    @Id
    @Column(name = "CODEVID")
    private BigDecimal codEvid;

    @Column(name = "CODRNC")
    private BigDecimal codRnc;

    @Column(name = "ORIGEM")
    private String origem;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
