package br.com.hagious.qualitymanager.mudancas.repository;

import br.com.hagious.qualitymanager.mudancas.entity.PerguntaGm;
import br.com.sankhya.sdk.data.repository.JapeRepository;
import br.com.sankhya.studio.stereotypes.Repository;

import java.math.BigDecimal;

@Repository
public interface PerguntaGmRepository extends JapeRepository<BigDecimal, PerguntaGm> {
}
