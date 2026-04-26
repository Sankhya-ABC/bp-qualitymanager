package br.com.hagious.qualitymanager.auditoria.repository;

import br.com.hagious.qualitymanager.auditoria.entity.ChecklistAuditoria;
import br.com.sankhya.sdk.data.repository.JapeRepository;
import br.com.sankhya.studio.stereotypes.Repository;

import java.math.BigDecimal;

@Repository
public interface ChecklistAuditoriaRepository extends JapeRepository<BigDecimal, ChecklistAuditoria> {
}
