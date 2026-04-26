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
@JapeEntity(entity = "ThgRncRegistro", table = "THGQMGREG")
public class RegistroRnc {

    @Id
    @Column(name = "CODRNC")
    private BigDecimal codRnc;

    @Column(name = "NURNC")
    private String nuRnc;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CODFASE")
    private BigDecimal codFase;

    @Column(name = "ORIGEM")
    private String origem;

    @Column(name = "PRIORIDADE")
    private String prioridade;

    @Column(name = "TIPONC")
    private String tipoNc;

    @Column(name = "CODPARC")
    private BigDecimal codParc;

    @Column(name = "NUNOTA")
    private BigDecimal nuNota;

    @Column(name = "PROCESSO")
    private String processo;

    @Column(name = "DTREGISTRO")
    private Timestamp dtRegistro;

    @Column(name = "DTPREVENCERRAR")
    private Timestamp dtPrevEncerrar;

    @Column(name = "DTENCERRAMENTO")
    private Timestamp dtEncerramento;

    @Column(name = "DETALHAMENTO")
    private String detalhamento;

    @Column(name = "REINCIDENTE")
    private String reincidente;

    @Column(name = "NAOPROCEDENTE")
    private String naoProcedente;

    @Column(name = "NCVINCULADA")
    private BigDecimal ncVinculada;

    @Column(name = "CODAUD")
    private BigDecimal codAud;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;

    @Column(name = "DHALTER")
    private Timestamp dhAlter;

    @OneToMany(
        cascade = Cascade.ALL,
        relationship = {
            @Relationship(fromField = "CODRNC", toField = "CODRNC")
        }
    )
    private List<Responsavel> responsaveis;

    @OneToMany(
        cascade = Cascade.ALL,
        relationship = {
            @Relationship(fromField = "CODRNC", toField = "CODRNC")
        }
    )
    private List<Evidencia> evidencias;
}
