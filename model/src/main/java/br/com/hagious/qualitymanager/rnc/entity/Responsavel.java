package br.com.hagious.qualitymanager.rnc.entity;

import br.com.sankhya.studio.persistence.Column;
import br.com.sankhya.studio.persistence.Id;
import br.com.sankhya.studio.persistence.JapeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JapeEntity(entity = "QmNcResponsavel", table = "THGQMGRESP")
public class Responsavel {

    @Id
    @Column(name = "CODRESP")
    private BigDecimal codResp;

    @Column(name = "CODRNC")
    private BigDecimal codRnc;

    @Column(name = "ORIGEM")
    private String origem;

    @Column(name = "CODPARC")
    private BigDecimal codParc;

    @Column(name = "ENVIAREMAIL")
    private String enviarEmail;
}
