package br.com.hagious.qualitymanager.rnc.entity;

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
@JapeEntity(entity = "QmNcRegFase", table = "THGQMGREGFASE")
public class RegistroFase {

    @Id
    @Column(name = "CODREGFASE")
    private BigDecimal codRegFase;

    @Column(name = "CODRNC")
    private BigDecimal codRnc;

    @Column(name = "CODFASEDE")
    private BigDecimal codFaseDe;

    @Column(name = "CODFASEPARA")
    private BigDecimal codFasePara;

    @Column(name = "DHTRANSICAO")
    private Timestamp dhTransicao;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;
}
