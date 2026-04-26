package br.com.hagious.qualitymanager.auditoria.entity;

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
@JapeEntity(entity = "QmAudPrograma", table = "THGQMGAUDPROG")
public class ProgramaAuditoria {

    @Id
    @Column(name = "CODPROG")
    private BigDecimal codProg;

    @Column(name = "ANO")
    private BigDecimal ano;

    @Column(name = "PROCESSO")
    private String processo;

    @Column(name = "NORMAISO")
    private String normaIso;

    @Column(name = "CLAUSULAS")
    private String clausulas;

    @Column(name = "DTPREVISTA")
    private Timestamp dtPrevista;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CODAUDTOR")
    private BigDecimal codAudtor;

    @Column(name = "CODAUD")
    private BigDecimal codAud;

    @Column(name = "OBSERVACOES")
    private String observacoes;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
