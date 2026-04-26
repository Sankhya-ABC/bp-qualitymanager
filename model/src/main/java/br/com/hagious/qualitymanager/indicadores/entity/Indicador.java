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
@JapeEntity(entity = "QmIndIndicador", table = "THGQMGIND")
public class Indicador {

    @Id
    @Column(name = "CODIND")
    private BigDecimal codInd;

    @Column(name = "CODIGO")
    private String codigo;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "PROCESSO")
    private String processo;

    @Column(name = "TIPO")
    private String tipo;

    @Column(name = "NORMAISO")
    private String normaIso;

    @Column(name = "CLAUSULAISO")
    private String clausulaIso;

    @Column(name = "UNIDADE")
    private String unidade;

    @Column(name = "FREQUENCIA")
    private String frequencia;

    @Column(name = "FORMULA")
    private String formula;

    @Column(name = "CODPARC")
    private BigDecimal codParc;

    @Column(name = "METAPADRAO")
    private BigDecimal metaPadrao;

    @Column(name = "SENTIDO")
    private String sentido;

    @Column(name = "ATIVO")
    private String ativo;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
