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
@JapeEntity(entity = "ThgAudParticipante", table = "THGQMGAUDPART")
public class Participante {

    @Id
    @Column(name = "CODPART")
    private BigDecimal codPart;

    @Column(name = "CODAUD")
    private BigDecimal codAud;

    @Column(name = "TIPOREUNIAO")
    private String tipoReuniao;

    @Column(name = "CODPARC")
    private BigDecimal codParc;

    @Column(name = "FUNCAO")
    private String funcao;

    @Column(name = "PRESENCA")
    private String presenca;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
