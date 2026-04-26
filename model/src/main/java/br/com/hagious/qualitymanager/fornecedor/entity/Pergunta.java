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
@JapeEntity(entity = "QmFornPergunta", table = "THGQMGFPERG")
public class Pergunta {

    @Id
    @Column(name = "CODPERG")
    private BigDecimal codPerg;

    @Column(name = "CODQUEST")
    private BigDecimal codQuest;

    @Column(name = "PERGUNTA")
    private String pergunta;

    @Column(name = "TIPORESPOSTA")
    private String tipoResposta;

    @Column(name = "ORDEM")
    private BigDecimal ordem;
}
