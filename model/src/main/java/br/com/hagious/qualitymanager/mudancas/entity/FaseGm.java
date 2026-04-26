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
@JapeEntity(entity = "ThgGmFase", table = "THGQMGGMFASE")
public class FaseGm {

    @Id
    @Column(name = "CODFASEGM")
    private BigDecimal codFaseGm;

    @Column(name = "DESCFASEGM")
    private String descFaseGm;

    @Column(name = "ATIVO")
    private String ativo;
}
