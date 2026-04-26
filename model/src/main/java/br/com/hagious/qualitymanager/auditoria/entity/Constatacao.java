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
@JapeEntity(entity = "ThgAudConstatacao", table = "THGQMGAUDCON")
public class Constatacao {

    @Id
    @Column(name = "CODCONST")
    private BigDecimal codConst;

    @Column(name = "CODAUD")
    private BigDecimal codAud;

    @Column(name = "TIPO")
    private String tipo;

    @Column(name = "CLAUSULAISO")
    private String clausulaIso;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "EVIDENCIA")
    private String evidencia;

    @Column(name = "CODRNC")
    private BigDecimal codRnc;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
