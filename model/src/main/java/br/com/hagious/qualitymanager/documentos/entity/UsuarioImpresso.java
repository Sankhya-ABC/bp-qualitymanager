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
@JapeEntity(entity = "QmDocUsuImpresso", table = "THGQMGUSIMPR")
public class UsuarioImpresso {

    @Id
    @Column(name = "CODIMPUSU")
    private BigDecimal codImpUsu;

    @Column(name = "CODIMP")
    private BigDecimal codImp;

    @Column(name = "CODPARC")
    private BigDecimal codParc;
}
