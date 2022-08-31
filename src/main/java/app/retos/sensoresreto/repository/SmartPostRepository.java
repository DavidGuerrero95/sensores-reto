package app.retos.sensoresreto.repository;


import app.retos.sensoresreto.models.SmartPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface SmartPostRepository extends MongoRepository<SmartPost, String> {

    @RestResource(path = "find-postId")
    SmartPost findByPostId(@Param("postId") String postId);

    @RestResource(path = "find-zoneCode")
    List<SmartPost> findByZoneCode(@Param("zoneCode") String zoneCode);

    @RestResource(path = "find-enabled")
    List<SmartPost> findByEnabled(@Param("enabled") Boolean enabled);

    @RestResource(path = "delete-postId")
    void deleteByPostId(@Param("postId") String postId);

}