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
@JapeEntity(entity = "QmNcQuem", table = "THGQMGNCQUEM")
public class NcQuem {

    @Id
    @Column(name = "CODQUEM")
    private BigDecimal codQuem;

    @Column(name = "CODABR")
    private BigDecimal codAbr;

    @Column(name = "CODPARC")
    private BigDecimal codParc;

    @Column(name = "ENVIAREMAIL")
    private String enviarEmail;
}
