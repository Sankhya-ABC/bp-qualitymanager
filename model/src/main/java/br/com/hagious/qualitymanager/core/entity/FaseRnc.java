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
@JapeEntity(entity = "ThgRncFase", table = "THGQMGRNCFASE")
public class FaseRnc {

    @Id
    @Column(name = "CODFASE")
    private BigDecimal codFase;

    @Column(name = "DESCFASE")
    private String descFase;

    @Column(name = "ATIVO")
    private String ativo;
}
