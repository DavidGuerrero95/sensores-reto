package app.retos.sensoresreto.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
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

    @NotEmpty(message = "locacion no puedde esta vacia")
    @Size(min=2,max = 2, message = "Debe tener dos valores")
    private List<Double> location;

    private Boolean enabled;
    private Integer zoneCode;
    private List<Camera> cameras;
    private Microphone microphones;
    private Boton botons;

}