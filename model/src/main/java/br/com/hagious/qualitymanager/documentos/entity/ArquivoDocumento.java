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
@JapeEntity(entity = "QmDocArquivo", table = "THGQMGARQ")
public class ArquivoDocumento {

    @Id
    @Column(name = "CODARQ")
    private BigDecimal codArq;

    @Column(name = "CODDOC")
    private BigDecimal codDoc;

    @Column(name = "VERSAO")
    private String versao;

    @Column(name = "DTVALIDADE")
    private Timestamp dtValidade;

    @Column(name = "COMENTARIO")
    private String comentario;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
