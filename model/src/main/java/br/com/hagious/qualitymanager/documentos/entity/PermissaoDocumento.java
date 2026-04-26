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
@JapeEntity(entity = "QmDocPermissao", table = "THGQMGPERM")
public class PermissaoDocumento {

    @Id
    @Column(name = "CODPERM")
    private BigDecimal codPerm;

    @Column(name = "CODDOC")
    private BigDecimal codDoc;

    @Column(name = "CODPARC")
    private BigDecimal codParc;

    @Column(name = "PERM_LEITURA")
    private String permLeitura;

    @Column(name = "PERM_REVISAO")
    private String permRevisao;

    @Column(name = "PERM_APROVA")
    private String permAprova;

    @Column(name = "PERM_IMPRIME")
    private String permImprime;
}
