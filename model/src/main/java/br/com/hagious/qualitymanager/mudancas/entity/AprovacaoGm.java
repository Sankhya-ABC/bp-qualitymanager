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
@JapeEntity(entity = "ThgGmAprovacao", table = "THGQMGGMRESP")
public class AprovacaoGm {

    @Id
    @Column(name = "CODRESPGM")
    private BigDecimal codRespGm;

    @Column(name = "CODGM")
    private BigDecimal codGm;

    @Column(name = "CODPARC")
    private BigDecimal codParc;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "JUSTIFICATIVA")
    private String justificativa;

    @Column(name = "DTDECISAO")
    private Timestamp dtDecisao;
}
