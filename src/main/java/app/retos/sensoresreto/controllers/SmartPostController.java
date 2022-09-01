package app.retos.sensoresreto.controllers;

import app.retos.sensoresreto.clients.ZonasFeignClient;
import app.retos.sensoresreto.models.Boton;
import app.retos.sensoresreto.models.Microphone;
import app.retos.sensoresreto.models.SmartPost;
import app.retos.sensoresreto.repository.SmartPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/posteinteligente")
public class SmartPostController {
    @SuppressWarnings("rawtypes")
    @Autowired
    private CircuitBreakerFactory cbFactory;
    @Autowired
    SmartPostRepository smartPostRepository;

    @Autowired
    ZonasFeignClient zonasFeignClient;

    @GetMapping("/listar")
    @ResponseStatus(code = HttpStatus.OK)
    public List<SmartPost> listarPostes() throws IOException {
        try {
            return smartPostRepository.findAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error en listar postes: " + e.getMessage());
        }
    }

    @PostMapping("/crear")
    @ResponseStatus(code = HttpStatus.CREATED)
    public String crearPosteInteligente(@RequestBody @Validated SmartPost smartPost) {
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
            return "Poste inteligente creado correctamente";
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al crear poste");
        }
    }

    @GetMapping("/habilitar-deshabilitar/{postId}")
    @ResponseStatus(code = HttpStatus.OK)
    public String modificarEnabled(@PathVariable("postId") Integer postId) {
        if (smartPostRepository.existsByPostId(postId)) {
            SmartPost post = smartPostRepository.findByPostId(postId);
            post.setEnabled(!post.getEnabled());
            smartPostRepository.save(post);
            return "El estado del poste: " + postId + " ha cambiado a: " + post.getEnabled();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El poste: " + postId + " no existe");
    }

    @DeleteMapping("/eliminar/{postId}")
    @ResponseStatus(code = HttpStatus.OK)
    public String eliminarPosteId(@PathVariable("postId") Integer postId) {
        if (smartPostRepository.existsByPostId(postId)) {
            smartPostRepository.deleteByPostId(postId);
            return "Poste: " + postId + " eliminado satisfactoriamente";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El poste: " + postId + " no existe");
    }

    //  ****************************	FUNCIONES TOLERANCIA A FALLOS	***********************************  //
    public Integer errorCreacionMuro(Throwable e) {
        log.info("Error creacion zona: " + e.getMessage());
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Servicio zona no esta disponible");
    }
}
