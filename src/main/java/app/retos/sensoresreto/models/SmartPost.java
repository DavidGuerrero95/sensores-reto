package app.retos.sensoresreto.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Document(collection = "smart-posts")
@Data
@NoArgsConstructor
public class SmartPost {

    @Id
    @JsonIgnore
    private String id;

    @Indexed(unique = true)
    private Integer postId;

    @NotBlank(message = "Locacion no puede ser null")
    private List<Double> location;

    private Boolean enabled;
    private Integer zoneCode;
    private List<Camera> cameras;
    private Microphone microphones;
    private Boton botons;

}