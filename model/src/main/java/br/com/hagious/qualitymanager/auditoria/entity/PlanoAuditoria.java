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
@JapeEntity(entity = "ThgAudPlano", table = "THGQMGAUDPLAN")
public class PlanoAuditoria {

    @Id
    @Column(name = "CODPLAN")
    private BigDecimal codPlan;

    @Column(name = "CODAUD")
    private BigDecimal codAud;

    @Column(name = "ATIVIDADE")
    private String atividade;

    @Column(name = "CODPARC")
    private BigDecimal codParc;

    @Column(name = "DTINICIO")
    private Timestamp dtInicio;

    @Column(name = "DTFIM")
    private Timestamp dtFim;

    @Column(name = "CLAUSULAS")
    private String clausulas;

    @Column(name = "LOCAL")
    private String local;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;
}
