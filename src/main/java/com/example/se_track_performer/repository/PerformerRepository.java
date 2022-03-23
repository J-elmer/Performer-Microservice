package com.example.se_track_performer.repository;

import com.example.se_track_performer.model.Performer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PerformerRepository extends JpaRepository<Performer, Long> {

    List<Performer> findByNameContainsIgnoreCase(String name);

    List<Performer> findByNameIgnoreCase(String name);

    Performer findPerformerById(Long id);

    @Query(
            value = "SELECT * FROM Performer WHERE LENGTH(name) > :length",
            nativeQuery = true
    )
    List<Performer> findPerformerWithNameGreaterThan(@Param("length") int length);

}
