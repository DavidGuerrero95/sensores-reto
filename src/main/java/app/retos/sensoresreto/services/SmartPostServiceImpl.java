package app.retos.sensoresreto.services;

import app.retos.sensoresreto.clients.ZonasFeignClient;
import app.retos.sensoresreto.models.Boton;
import app.retos.sensoresreto.models.Camera;
import app.retos.sensoresreto.models.Microphone;
import app.retos.sensoresreto.models.SmartPost;
import app.retos.sensoresreto.repository.SmartPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;

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
        smartPost.setLocation(new ArrayList<>(Arrays.asList(
                BigDecimal.valueOf(smartPost.getLocation().get(0)).setScale(5, RoundingMode.HALF_UP).doubleValue(),
                BigDecimal.valueOf(smartPost.getLocation().get(1)).setScale(5, RoundingMode.HALF_UP).doubleValue())));
        smartPost.setPostId(smartPostRepository.findAll().size());
        smartPost.setEnabled(true);
        smartPost.setCameras(new Camera("Camara",smartPost.getPostId()));
        smartPost.setMicrophones(new Microphone(smartPost.getPostId(),""));
        smartPost.setBotons(new Boton(smartPost.getPostId(), 4));
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
