package app.retos.sensoresreto.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Document(collection = "microphones")
@Data
@NoArgsConstructor
public class Microphone {

    @Id
    @JsonIgnore
    private String id;

    @JsonIgnore
    private Integer postId;
    private String info;

    public Microphone(Integer postId, String info) {
        this.postId = postId;
        this.info = info;
    }
}