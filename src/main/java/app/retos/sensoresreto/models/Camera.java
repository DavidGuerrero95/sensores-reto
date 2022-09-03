package app.retos.sensoresreto.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Document(collection = "cameras")
@Data
@NoArgsConstructor
public class Camera {

    @Id
    @JsonIgnore
    private String id;

    private String name;
    @JsonIgnore
    private Integer postId;

    public Camera(String name, Integer postId) {
        this.name = name;
        this.postId = postId;
    }
}