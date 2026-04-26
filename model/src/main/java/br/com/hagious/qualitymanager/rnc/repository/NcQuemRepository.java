package br.com.hagious.qualitymanager.rnc.repository;

import br.com.hagious.qualitymanager.rnc.entity.NcQuem;
import br.com.sankhya.sdk.data.repository.JapeRepository;
import br.com.sankhya.studio.stereotypes.Repository;

import java.math.BigDecimal;

@Repository
public interface NcQuemRepository extends JapeRepository<BigDecimal, NcQuem> {
}
