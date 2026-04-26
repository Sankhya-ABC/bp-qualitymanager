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
@JapeEntity(entity = "ThgGmPergunta", table = "THGQMGGMPERG")
public class PerguntaGm {

    @Id
    @Column(name = "CODPERGGM")
    private BigDecimal codPergGm;

    @Column(name = "CODAVAL")
    private BigDecimal codAval;

    @Column(name = "CODPERG")
    private BigDecimal codPerg;

    @Column(name = "RESPOSTA")
    private String resposta;
}
