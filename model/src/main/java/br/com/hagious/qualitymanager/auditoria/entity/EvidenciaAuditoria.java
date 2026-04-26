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
@JapeEntity(entity = "ThgAudEvidencia", table = "THGQMGAUDEVID")
public class EvidenciaAuditoria {

    @Id
    @Column(name = "CODEVAUD")
    private BigDecimal codEvAud;

    @Column(name = "CODAUD")
    private BigDecimal codAud;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "TIPOEVIDENCIA")
    private String tipoEvidencia;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
