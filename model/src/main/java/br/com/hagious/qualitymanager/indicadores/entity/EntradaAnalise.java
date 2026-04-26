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
@JapeEntity(entity = "QmAcrEntrada", table = "THGQMGENT")
public class EntradaAnalise {

    @Id
    @Column(name = "CODENTRADA")
    private BigDecimal codEntrada;

    @Column(name = "CODACR")
    private BigDecimal codAcr;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;
}
