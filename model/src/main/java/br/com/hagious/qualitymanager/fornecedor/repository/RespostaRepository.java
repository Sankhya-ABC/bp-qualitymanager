package br.com.hagious.qualitymanager.fornecedor.repository;

import br.com.hagious.qualitymanager.fornecedor.entity.Resposta;
import br.com.sankhya.sdk.data.repository.JapeRepository;
import br.com.sankhya.sdk.data.repository.criteria.Criteria;
import br.com.sankhya.sdk.data.repository.criteria.Parameter;
import br.com.sankhya.studio.stereotypes.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RespostaRepository extends JapeRepository<BigDecimal, Resposta> {

    @Criteria(clause = "this.CODQUAL = :codQual")
    List<Resposta> findByQualificacao(@Parameter("codQual") BigDecimal codQual);
}
