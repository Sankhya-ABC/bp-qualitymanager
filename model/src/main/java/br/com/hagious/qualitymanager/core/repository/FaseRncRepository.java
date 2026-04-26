package br.com.hagious.qualitymanager.core.repository;

import br.com.hagious.qualitymanager.core.entity.FaseRnc;
import br.com.sankhya.sdk.data.repository.JapeRepository;
import br.com.sankhya.studio.stereotypes.Repository;

import java.math.BigDecimal;

@Repository
public interface FaseRncRepository extends JapeRepository<BigDecimal, FaseRnc> {
}
