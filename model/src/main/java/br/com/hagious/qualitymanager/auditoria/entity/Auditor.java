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
@JapeEntity(entity = "ThgAudAuditor", table = "THGQMGAUDTOR")
public class Auditor {

    @Id
    @Column(name = "CODAUDTOR")
    private BigDecimal codAudtor;

    @Column(name = "CODPARC")
    private BigDecimal codParc;

    @Column(name = "TIPO")
    private String tipo;

    @Column(name = "NORMASAUDIT")
    private String normasAudit;

    @Column(name = "FORMACAO")
    private String formacao;

    @Column(name = "NRCERTIFICADO")
    private String nrCertificado;

    @Column(name = "DTVALCERTIF")
    private Timestamp dtValCertif;

    @Column(name = "ATIVO")
    private String ativo;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;
}
