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
@JapeEntity(entity = "QmIndMedicao", table = "THGQMGMED")
public class Medicao {

    @Id
    @Column(name = "CODMEDICAO")
    private BigDecimal codMedicao;

    @Column(name = "CODIND")
    private BigDecimal codInd;

    @Column(name = "PERIODO")
    private String periodo;

    @Column(name = "VALOR")
    private BigDecimal valor;

    @Column(name = "OBSERVACOES")
    private String observacoes;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
