package br.com.hagious.qualitymanager.core.entity;

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
@JapeEntity(entity = "QmDomPrioridade", table = "THGQMGPRIO")
public class Prioridade {

    @Id
    @Column(name = "CODPRIO")
    private BigDecimal codPrio;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "PRAZODIAS")
    private BigDecimal prazoDias;

    @Column(name = "ATIVO")
    private String ativo;
}
