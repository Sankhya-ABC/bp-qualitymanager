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
@JapeEntity(entity = "ThgDocControle", table = "THGQMGDOC")
public class ControleDocumento {

    @Id
    @Column(name = "CODDOC")
    private BigDecimal codDoc;

    @Column(name = "CODIGODOC")
    private String codigoDoc;

    @Column(name = "TITULODOC")
    private String tituloDoc;

    @Column(name = "TIPODOC")
    private String tipoDoc;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "VERSAOATUAL")
    private String versaoAtual;

    @Column(name = "CODPARC")
    private BigDecimal codParc;

    @Column(name = "CODPARC_APROV")
    private BigDecimal codParcAprov;

    @Column(name = "PROCESSO")
    private String processo;

    @Column(name = "NORMAISO")
    private String normaIso;

    @Column(name = "CLAUSULAISO")
    private String clausulaIso;

    @Column(name = "DTVALIDADE")
    private Timestamp dtValidade;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
