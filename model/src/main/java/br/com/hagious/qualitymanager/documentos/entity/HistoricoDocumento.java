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
@JapeEntity(entity = "QmDocHistorico", table = "THGQMGHIST")
public class HistoricoDocumento {

    @Id
    @Column(name = "CODHIST")
    private BigDecimal codHist;

    @Column(name = "CODDOC")
    private BigDecimal codDoc;

    @Column(name = "STATUSANT")
    private String statusAnt;

    @Column(name = "STATUSNOVO")
    private String statusNovo;

    @Column(name = "JUSTIFICATIVA")
    private String justificativa;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
