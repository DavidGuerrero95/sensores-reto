package app.retos.sensoresreto.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "zonas-reto")
public interface ZonasFeignClient {

    @PostMapping("/zonas/sensores/crear/")
    Integer crearZonesPosts(@RequestParam("idEvents") Integer idPosts,
                            @RequestParam("location") List<Double> location);


}
