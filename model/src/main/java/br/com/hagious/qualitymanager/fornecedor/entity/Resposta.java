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
@JapeEntity(entity = "QmFornResposta", table = "THGQMGFRESP")
public class Resposta {

    @Id
    @Column(name = "CODRESPTA")
    private BigDecimal codRespta;

    @Column(name = "CODQUAL")
    private BigDecimal codQual;

    @Column(name = "CODPERG")
    private BigDecimal codPerg;

    @Column(name = "RESPOSTA")
    private String resposta;
}
