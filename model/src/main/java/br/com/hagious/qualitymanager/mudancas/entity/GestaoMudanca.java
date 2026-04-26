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
@JapeEntity(entity = "ThgGmCadastro", table = "THGQMGGM")
public class GestaoMudanca {

    @Id
    @Column(name = "CODGM")
    private BigDecimal codGm;

    @Column(name = "NUMGM")
    private String numGm;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CODFASE")
    private BigDecimal codFase;

    @Column(name = "PROCESSO")
    private String processo;

    @Column(name = "TIPOMUDANCA")
    private String tipoMudanca;

    @Column(name = "SITUACAOATUAL")
    private String situacaoAtual;

    @Column(name = "PROPOSTA")
    private String proposta;

    @Column(name = "JUSTIFICATIVA")
    private String justificativa;

    @Column(name = "CODPARC")
    private BigDecimal codParc;

    @Column(name = "DTPREVISTA")
    private Timestamp dtPrevista;

    @Column(name = "DTIMPL")
    private Timestamp dtImpl;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
