package br.com.hagious.qualitymanager.auditoria.repository;

import br.com.hagious.qualitymanager.auditoria.entity.EvidenciaAuditoria;
import br.com.sankhya.sdk.data.repository.JapeRepository;
import br.com.sankhya.studio.stereotypes.Repository;

import java.math.BigDecimal;

@Repository
public interface EvidenciaAuditoriaRepository extends JapeRepository<BigDecimal, EvidenciaAuditoria> {
}
