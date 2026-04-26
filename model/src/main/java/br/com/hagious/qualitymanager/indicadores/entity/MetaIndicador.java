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
@JapeEntity(entity = "ThgIndMeta", table = "THGQMGMETA")
public class MetaIndicador {

    @Id
    @Column(name = "CODMETA")
    private BigDecimal codMeta;

    @Column(name = "CODIND")
    private BigDecimal codInd;

    @Column(name = "PERIODO")
    private String periodo;

    @Column(name = "META")
    private BigDecimal meta;

    @Column(name = "REALIZADO")
    private BigDecimal realizado;

    @Column(name = "ATINGIMENTO")
    private BigDecimal atingimento;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "OBSERVACOES")
    private String observacoes;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;
}
