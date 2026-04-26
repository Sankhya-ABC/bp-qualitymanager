package br.com.hagious.qualitymanager.fornecedor.entity;

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
@JapeEntity(entity = "ThgFornCriterio", table = "THGQMGCRIT")
public class Criterio {

    @Id
    @Column(name = "CODCRIT")
    private BigDecimal codCrit;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "PESO")
    private BigDecimal peso;

    @Column(name = "ATIVO")
    private String ativo;
}
