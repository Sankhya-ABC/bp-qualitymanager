package br.com.hagious.qualitymanager.fornecedor.repository;

import br.com.hagious.qualitymanager.fornecedor.entity.DocumentoFornecedor;
import br.com.sankhya.sdk.data.repository.JapeRepository;
import br.com.sankhya.studio.stereotypes.Repository;

import java.math.BigDecimal;

@Repository
public interface DocumentoFornecedorRepository extends JapeRepository<BigDecimal, DocumentoFornecedor> {
}
