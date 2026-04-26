package br.com.hagious.qualitymanager.documentos.repository;

import br.com.hagious.qualitymanager.documentos.entity.HistoricoDocumento;
import br.com.sankhya.sdk.data.repository.JapeRepository;
import br.com.sankhya.studio.stereotypes.Repository;

import java.math.BigDecimal;

@Repository
public interface HistoricoDocumentoRepository extends JapeRepository<BigDecimal, HistoricoDocumento> {
}
