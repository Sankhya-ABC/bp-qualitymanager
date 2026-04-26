package br.com.hagious.qualitymanager.fornecedor.entity;

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
@JapeEntity(entity = "ThgFornDocumento", table = "THGQMGFORNDOC")
public class DocumentoFornecedor {

    @Id
    @Column(name = "CODDOCFORN")
    private BigDecimal codDocForn;

    @Column(name = "CODQUAL")
    private BigDecimal codQual;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "NUMERODOC")
    private String numeroDoc;

    @Column(name = "DTVALIDADE")
    private Timestamp dtValidade;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
