package br.com.hagious.qualitymanager.rnc.listener;

import br.com.hagious.qualitymanager.rnc.entity.RegistroRnc;
import br.com.hagious.qualitymanager.rnc.repository.RegistroRncRepository;
import br.com.sankhya.studio.persistence.PersistenceEventAdapter;
import br.com.sankhya.studio.stereotypes.Component;
import lombok.Log;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

@Log
@Component
public class NcReincidenciaListener extends PersistenceEventAdapter {

    private final RegistroRncRepository registroRncRepository;

    @Inject
    public NcReincidenciaListener(RegistroRncRepository registroRncRepository) {
        this.registroRncRepository = registroRncRepository;
    }

    @Override
    public String getInstanceName() {
        return "QmNcRegistro";
    }

    @Override
    public void afterInsert(Object entity) {
        if (!(entity instanceof RegistroRnc)) {
            return;
        }

        try {
            RegistroRnc novaRnc = (RegistroRnc) entity;
            String origem = novaRnc.getOrigem();
            String processo = novaRnc.getProcesso();

            if (origem == null || processo == null || processo.trim().isEmpty()) {
                return;
            }

            List<RegistroRnc> anteriores = registroRncRepository.findReincidencias(origem, processo);

            for (RegistroRnc anterior : anteriores) {
                if (anterior.getCodRnc().equals(novaRnc.getCodRnc())) {
                    continue;
                }

                novaRnc.setReincidente("S");
                novaRnc.setNcVinculada(anterior.getCodRnc());
                registroRncRepository.save(novaRnc);

                log.info("[QM-NC] Reincidencia detectada: RNC " + novaRnc.getCodRnc()
                        + " vinculada a RNC " + anterior.getCodRnc());
                return;
            }
        } catch (Exception e) {
            log.severe("[QM-NC] Erro ao verificar reincidencia: " + e.getMessage());
        }
    }
}
