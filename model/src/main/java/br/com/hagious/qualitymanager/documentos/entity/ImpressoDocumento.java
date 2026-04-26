package br.com.hagious.qualitymanager.documentos.entity;

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
@JapeEntity(entity = "ThgDocImpresso", table = "THGQMGIMP")
public class ImpressoDocumento {

    @Id
    @Column(name = "CODIMP")
    private BigDecimal codImp;

    @Column(name = "CODDOC")
    private BigDecimal codDoc;

    @Column(name = "VERSAO")
    private String versao;

    @Column(name = "QTDECOPIAS")
    private BigDecimal qtdeCopias;

    @Column(name = "FINALIDADE")
    private String finalidade;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
