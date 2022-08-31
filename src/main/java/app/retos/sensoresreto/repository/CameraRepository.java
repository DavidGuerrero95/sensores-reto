package app.retos.sensoresreto.repository;


import app.retos.sensoresreto.models.Camera;
import app.retos.sensoresreto.models.SmartPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface CameraRepository extends MongoRepository<Camera, String> {

    @RestResource(path = "find-postId")
    List<Camera> findByPostId(@Param("postId") Integer postId);

    @RestResource(path = "find-postId-name")
    Camera findByPostIdAndName(@Param("postId") Integer postId, @Param("name") String name);

    @RestResource(path = "delete-postId")
    void deleteByPostId(@Param("postId") Integer postId);

}