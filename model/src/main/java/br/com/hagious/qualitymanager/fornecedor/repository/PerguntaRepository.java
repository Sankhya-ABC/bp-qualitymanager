package br.com.hagious.qualitymanager.fornecedor.repository;

import br.com.hagious.qualitymanager.fornecedor.entity.Pergunta;
import br.com.sankhya.sdk.data.repository.JapeRepository;
import br.com.sankhya.sdk.data.repository.criteria.Criteria;
import br.com.sankhya.sdk.data.repository.criteria.Parameter;
import br.com.sankhya.studio.stereotypes.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PerguntaRepository extends JapeRepository<BigDecimal, Pergunta> {

    @Criteria(clause = "this.CODQUEST = :codQuest")
    List<Pergunta> findByQuestionario(@Parameter("codQuest") BigDecimal codQuest);
}
