package br.com.hagious.qualitymanager.mudancas.repository;

import br.com.hagious.qualitymanager.mudancas.entity.AcaoGm;
import br.com.sankhya.sdk.data.repository.JapeRepository;
import br.com.sankhya.studio.stereotypes.Repository;

import java.math.BigDecimal;

@Repository
public interface AcaoGmRepository extends JapeRepository<BigDecimal, AcaoGm> {
}
