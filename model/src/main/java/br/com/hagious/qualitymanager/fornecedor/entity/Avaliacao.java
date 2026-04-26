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
@JapeEntity(entity = "ThgFornAvaliacao", table = "THGQMGFAVAL")
public class Avaliacao {

    @Id
    @Column(name = "CODAVAL")
    private BigDecimal codAval;

    @Column(name = "CODQUAL")
    private BigDecimal codQual;

    @Column(name = "PERIODO")
    private String periodo;

    @Column(name = "NOTA_QUALIDADE")
    private BigDecimal notaQualidade;

    @Column(name = "NOTA_PRAZO")
    private BigDecimal notaPrazo;

    @Column(name = "NOTA_TECNICO")
    private BigDecimal notaTecnico;

    @Column(name = "NOTA_SEGURANCA")
    private BigDecimal notaSeguranca;

    @Column(name = "SCORE_FINAL")
    private BigDecimal scoreFinal;

    @Column(name = "OBSERVACOES")
    private String observacoes;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
