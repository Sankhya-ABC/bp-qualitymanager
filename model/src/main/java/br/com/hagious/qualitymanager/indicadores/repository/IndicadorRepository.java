package br.com.hagious.qualitymanager.indicadores.repository;

import br.com.hagious.qualitymanager.indicadores.entity.Indicador;
import br.com.sankhya.sdk.data.repository.JapeRepository;
import br.com.sankhya.studio.stereotypes.Repository;

import java.math.BigDecimal;

@Repository
public interface IndicadorRepository extends JapeRepository<BigDecimal, Indicador> {
}
