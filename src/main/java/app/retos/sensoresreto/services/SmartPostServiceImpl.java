package app.retos.sensoresreto.services;

import app.retos.sensoresreto.clients.ZonasFeignClient;
import app.retos.sensoresreto.models.Boton;
import app.retos.sensoresreto.models.Microphone;
import app.retos.sensoresreto.models.SmartPost;
import app.retos.sensoresreto.repository.SmartPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@Service
@Slf4j
public class SmartPostServiceImpl implements ISmartPostService {
    @SuppressWarnings("rawtypes")
    @Autowired
    private CircuitBreakerFactory cbFactory;
    @Autowired
    SmartPostRepository smartPostRepository;

    @Autowired
    ZonasFeignClient zonasFeignClient;

    @Override
    public Boolean crearSmartPost(SmartPost smartPost) {
        smartPost.setPostId(smartPostRepository.findAll().size());
        smartPost.setEnabled(true);
        smartPost.setCameras(new ArrayList<>());
        smartPost.setMicrophones(new Microphone());
        smartPost.setBotons(new Boton());
        smartPost.setZoneCode(cbFactory.create("sensores").run(
                () -> zonasFeignClient.crearZonesPosts(smartPost.getPostId(), smartPost.getLocation()),
                this::errorCreacionMuro));
        try {
            smartPostRepository.save(smartPost);
            return true;
        } catch (Exception e) {
            log.error("Error en la creación: " + e.getMessage());
            return false;
        }
    }

    //  ****************************	FUNCIONES TOLERANCIA A FALLOS	***********************************  //
    private Integer errorCreacionMuro(Throwable e) {
        log.info("Error creacion zona: " + e.getMessage());
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Servicio zona no esta disponible");
    }
}
