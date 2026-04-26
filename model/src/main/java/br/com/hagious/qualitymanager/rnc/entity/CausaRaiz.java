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
@JapeEntity(entity = "ThgRncCausaRaiz", table = "THGQMGCR")
public class CausaRaiz {

    @Id
    @Column(name = "CODCR")
    private BigDecimal codCr;

    @Column(name = "CODRNC")
    private BigDecimal codRnc;

    @Column(name = "MAO_OBRA")
    private String maoObra;

    @Column(name = "METODO")
    private String metodo;

    @Column(name = "MAQUINA")
    private String maquina;

    @Column(name = "MEDIDA")
    private String medida;

    @Column(name = "MATERIAL")
    private String material;

    @Column(name = "MEIO_AMBIENTE")
    private String meioAmbiente;

    @Column(name = "NAOSEAPLICA")
    private String naoSeAplica;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;

    @Column(name = "DHALTER")
    private Timestamp dhAlter;
}
