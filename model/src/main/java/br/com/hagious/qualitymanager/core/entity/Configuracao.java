package br.com.hagious.qualitymanager.core.entity;

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
@JapeEntity(entity = "QmConfig", table = "THGQMGCFG")
public class Configuracao {

    @Id
    @Column(name = "CODCFG")
    private BigDecimal codCfg;

    @Column(name = "CODEMP")
    private BigDecimal codEmp;

    @Column(name = "MODNCATIVO")
    private String modNcAtivo;

    @Column(name = "MODFORNATIVO")
    private String modFornAtivo;

    @Column(name = "MODDOCATIVO")
    private String modDocAtivo;

    @Column(name = "MODGMATIVO")
    private String modGmAtivo;

    @Column(name = "MODAUDATIVO")
    private String modAudAtivo;

    @Column(name = "MODINDATIVO")
    private String modIndAtivo;

    @Column(name = "EMAILNOTIF")
    private String emailNotif;

    @Column(name = "DIASAVISO_DOC")
    private BigDecimal diasAvisoDoc;

    @Column(name = "DIASRESP_FORN")
    private BigDecimal diasRespForn;

    @Column(name = "CODUSU")
    private BigDecimal codUsu;

    @Column(name = "DHCREATE")
    private Timestamp dhCreate;
}
