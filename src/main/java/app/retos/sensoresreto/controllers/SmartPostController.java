package app.retos.sensoresreto.controllers;

import app.retos.sensoresreto.models.SmartPost;
import app.retos.sensoresreto.repository.SmartPostRepository;
import app.retos.sensoresreto.services.ISmartPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/posteinteligente")
public class SmartPostController {

    @Autowired
    SmartPostRepository smartPostRepository;

    @Autowired
    ISmartPostService postService;

    @GetMapping("/listar")
    @ResponseStatus(code = HttpStatus.OK)
    public List<SmartPost> listarPostes() {
        return smartPostRepository.findAll();
    }

    @PostMapping("/crear")
    @ResponseStatus(code = HttpStatus.CREATED)
    public String crearPosteInteligente(@RequestBody @Validated SmartPost smartPost) {
        if (postService.crearSmartPost(smartPost))
            return "Poste inteligente creado correctamente";
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al crear poste");
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

    @GetMapping("/existe/{postId}")
    @ResponseStatus(HttpStatus.FOUND)
    public Boolean posteExiste(@PathVariable("postId") Integer postId) throws IOException {
        try {
            return smartPostRepository.existsByPostId(postId);
        } catch (Exception e2) {
            throw new IOException("Error al encontrar usuario: " + e2.getMessage());
        }
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

    @DeleteMapping("/eliminar/all")
    @ResponseStatus(code = HttpStatus.OK)
    public void eliminarAll(){
        smartPostRepository.deleteAll();
    }

    //  ****************************	FUNCIONES TOLERANCIA A FALLOS	***********************************  //
    public Integer errorCreacionMuro(Throwable e) {
        log.info("Error creacion zona: " + e.getMessage());
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Servicio zona no esta disponible");
    }
}
