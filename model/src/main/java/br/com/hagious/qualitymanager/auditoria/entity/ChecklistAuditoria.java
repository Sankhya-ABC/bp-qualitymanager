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
@JapeEntity(entity = "ThgAudChecklist", table = "THGQMGAUDCHK")
public class ChecklistAuditoria {

    @Id
    @Column(name = "CODCHK")
    private BigDecimal codChk;

    @Column(name = "CODAUD")
    private BigDecimal codAud;

    @Column(name = "NORMAISO")
    private String normaIso;

    @Column(name = "CLAUSULA")
    private String clausula;

    @Column(name = "PERGUNTA")
    private String pergunta;

    @Column(name = "RESULTADO")
    private String resultado;

    @Column(name = "EVIDENCIA")
    private String evidencia;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHRESPOSTA")
    private Timestamp dhResposta;
}
