package br.com.hagious.qualitymanager.mudancas.entity;

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
@JapeEntity(entity = "ThgGmQuestionario", table = "THGQMGGMQUEST")
public class QuestionarioGm {

    @Id
    @Column(name = "CODAVAL")
    private BigDecimal codAval;

    @Column(name = "CODGM")
    private BigDecimal codGm;

    @Column(name = "CODQUEST")
    private BigDecimal codQuest;

    @Column(name = "ORIGEM")
    private BigDecimal origem;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
