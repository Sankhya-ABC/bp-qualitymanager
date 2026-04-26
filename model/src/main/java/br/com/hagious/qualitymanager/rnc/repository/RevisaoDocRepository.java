package br.com.hagious.qualitymanager.rnc.repository;

import br.com.hagious.qualitymanager.rnc.entity.RevisaoDoc;
import br.com.sankhya.sdk.data.repository.JapeRepository;
import br.com.sankhya.studio.stereotypes.Repository;

import java.math.BigDecimal;

@Repository
public interface RevisaoDocRepository extends JapeRepository<BigDecimal, RevisaoDoc> {
}
