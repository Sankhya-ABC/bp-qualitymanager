package br.com.hagious.qualitymanager.rnc.entity;

import br.com.sankhya.studio.persistence.Cascade;
import br.com.sankhya.studio.persistence.Column;
import br.com.sankhya.studio.persistence.Id;
import br.com.sankhya.studio.persistence.JapeEntity;
import br.com.sankhya.studio.persistence.OneToMany;
import br.com.sankhya.studio.persistence.Relationship;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JapeEntity(entity = "QmRncAbrangencia", table = "THGQMGABR")
public class Abrangencia {

    @Id
    @Column(name = "CODABR")
    private BigDecimal codAbr;

    @Column(name = "CODRNC")
    private BigDecimal codRnc;

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

    @OneToMany(
        cascade = Cascade.ALL,
        relationship = {
            @Relationship(fromField = "CODABR", toField = "CODABR")
        }
    )
    private List<NcQuem> quem;
}
