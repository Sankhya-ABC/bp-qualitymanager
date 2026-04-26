package br.com.hagious.qualitymanager.fornecedor.repository;

import br.com.hagious.qualitymanager.fornecedor.entity.Qualificacao;
import br.com.sankhya.sdk.data.repository.JapeRepository;
import br.com.sankhya.studio.stereotypes.Repository;

import java.math.BigDecimal;

@Repository
public interface QualificacaoRepository extends JapeRepository<BigDecimal, Qualificacao> {
}
