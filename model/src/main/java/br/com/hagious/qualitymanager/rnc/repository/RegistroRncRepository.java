package br.com.hagious.qualitymanager.rnc.repository;

import br.com.hagious.qualitymanager.rnc.entity.RegistroRnc;
import br.com.sankhya.sdk.data.repository.JapeRepository;
import br.com.sankhya.sdk.data.repository.criteria.Criteria;
import br.com.sankhya.sdk.data.repository.criteria.Parameter;
import br.com.sankhya.studio.stereotypes.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RegistroRncRepository extends JapeRepository<BigDecimal, RegistroRnc> {

    @Criteria(clause = "this.ORIGEM = :origem AND this.PROCESSO = :processo AND this.STATUS NOT IN ('E','C')")
    List<RegistroRnc> findReincidencias(@Parameter("origem") String origem,
                                         @Parameter("processo") String processo);
}
