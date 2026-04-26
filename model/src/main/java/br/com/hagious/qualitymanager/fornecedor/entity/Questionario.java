package br.com.hagious.qualitymanager.fornecedor.entity;

import br.com.sankhya.studio.persistence.Column;
import br.com.sankhya.studio.persistence.Id;
import br.com.sankhya.studio.persistence.JapeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JapeEntity(entity = "ThgFornQuestionario", table = "THGQMGFQUEST")
public class Questionario {

    @Id
    @Column(name = "CODQUEST")
    private BigDecimal codQuest;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "ORIGEM")
    private String origem;

    @Column(name = "ATIVO")
    private String ativo;
}
