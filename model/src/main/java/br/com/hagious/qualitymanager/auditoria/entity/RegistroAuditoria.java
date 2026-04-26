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
@JapeEntity(entity = "QmAudRegistro", table = "THGQMGAUD")
public class RegistroAuditoria {

    @Id
    @Column(name = "CODAUD")
    private BigDecimal codAud;

    @Column(name = "NUMAUDITORIA")
    private String numAuditoria;

    @Column(name = "TIPO")
    private String tipo;

    @Column(name = "NORMAISO")
    private String normaIso;

    @Column(name = "ESCOPO")
    private String escopo;

    @Column(name = "PROCESSO")
    private String processo;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CODAUDTOR")
    private BigDecimal codAudtor;

    @Column(name = "CODEMP")
    private BigDecimal codEmp;

    @Column(name = "DTPREVISTA")
    private Timestamp dtPrevista;

    @Column(name = "DTINICIO")
    private Timestamp dtInicio;

    @Column(name = "DTCONCLUSAO")
    private Timestamp dtConclusao;

    @Column(name = "CODPROG")
    private BigDecimal codProg;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
