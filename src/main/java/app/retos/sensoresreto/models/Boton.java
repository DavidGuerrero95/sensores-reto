package app.retos.sensoresreto.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Document(collection = "botons")
@Data
@NoArgsConstructor
public class Boton {

    @Id
    @JsonIgnore
    private String id;

    @JsonIgnore
    private Integer postId;

    @JsonIgnore
    private String userId;

    @JsonIgnore
    private String botonId;
    private Integer type;

    public Boton(Integer postId, Integer type) {
        this.postId = postId;
        this.type = type;
    }
}