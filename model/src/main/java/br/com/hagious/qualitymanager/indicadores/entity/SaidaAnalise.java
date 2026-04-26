package br.com.hagious.qualitymanager.indicadores.entity;

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
@JapeEntity(entity = "QmAcrSaida", table = "THGQMGSAI")
public class SaidaAnalise {

    @Id
    @Column(name = "CODSAIDA")
    private BigDecimal codSaida;

    @Column(name = "CODACR")
    private BigDecimal codAcr;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "CODPARC")
    private BigDecimal codParc;

    @Column(name = "DTPRAZO")
    private Timestamp dtPrazo;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;
}
