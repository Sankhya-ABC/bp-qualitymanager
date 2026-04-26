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
@JapeEntity(entity = "QmGmQuem", table = "THGQMGGMQUEM")
public class QuemGm {

    @Id
    @Column(name = "CODQUEMGM")
    private BigDecimal codQuemGm;

    @Column(name = "CODACAOGM")
    private BigDecimal codAcaoGm;

    @Column(name = "CODPARC")
    private BigDecimal codParc;
}
