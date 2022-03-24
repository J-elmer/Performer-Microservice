package com.example.se_track_performer.service;

import com.example.se_track_performer.controller.DTO.NewPerformerDTO;
import com.example.se_track_performer.controller.DTO.UpdatePerformerDTO;
import com.example.se_track_performer.exception.InvalidPerformerDTOException;
import com.example.se_track_performer.exception.PerformerHasConcertsException;
import com.example.se_track_performer.exception.PerformerNotFoundException;
import com.example.se_track_performer.model.Performer;
import com.example.se_track_performer.repository.PerformerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class PerformerService {

    private final PerformerRepository performerRepository;
    private final WebClient webClient;
    private final String performerCheckUri = "http://host.docker.internal:9090/concert/check-delete-performer?performerId=";

    @Autowired
    public PerformerService(PerformerRepository performerRepository) {
        this.performerRepository = performerRepository;
        this.webClient = WebClient.create();
    }

    /**
     *
     * @return List of performers
     */
    public List<Performer> getAllPerformers() {
        return this.performerRepository.findAll();
    }

    /**
     *
     * @param name to search for
     * @return List of performers matching the search param
     */
    public List<Performer> getPerformerByName(String name) {
        return this.performerRepository.findByNameContainsIgnoreCase(name);
    }

    /**
     *
     * @param id of performer to find
     * @return Performer found
     */
    public Performer getPerformerById(Long id) {
        return this.performerRepository.findPerformerById(id);
    }

    /**
     *
     * @param newPerformerDTO DTO class with information needed
     * @throws DataIntegrityViolationException if performer already exists, this is thrown
     */
    public void createPerformer(NewPerformerDTO newPerformerDTO) throws DataIntegrityViolationException {
        Performer performerToBeSaved = new Performer(newPerformerDTO.getName(), newPerformerDTO.getAge(), newPerformerDTO.getStyle());
        this.performerRepository.save(performerToBeSaved);
    }

    /**
     *
     * @param updatePerformerDTO DTO class with information needed
     * @throws PerformerNotFoundException If performer is not found
     * @throws InvalidPerformerDTOException If DTO doesn't contain an age and a style
     */
    public void updatePerformer(UpdatePerformerDTO updatePerformerDTO) throws PerformerNotFoundException, InvalidPerformerDTOException {
        Performer performerToUpdate = !this.performerRepository.findByNameIgnoreCase(updatePerformerDTO.getName()).isEmpty() ?
                this.performerRepository.findByNameIgnoreCase(updatePerformerDTO.getName()).get(0) : null;
        if (performerToUpdate == null) {
            throw new PerformerNotFoundException();
        }
        if (updatePerformerDTO.getStyle() == null && updatePerformerDTO.getAge() == 0) {
            throw new InvalidPerformerDTOException();
        }
        if (updatePerformerDTO.getAge() > 0 && updatePerformerDTO.getAge() != performerToUpdate.getAge()) {
            performerToUpdate.setAge(updatePerformerDTO.getAge());
        }
        if (updatePerformerDTO.getStyle() != null && !updatePerformerDTO.getStyle().equals(performerToUpdate.getStyle())) {
            performerToUpdate.setStyle(updatePerformerDTO.getStyle());
        }
        this.performerRepository.save(performerToUpdate);
    }

    /**
     *
     * @param id of Performer to delete
     * @throws PerformerNotFoundException if performer is not found
     * @throws DataIntegrityViolationException thrown when performer still has concerts associated with them
     */
    public void delete(Long id) throws PerformerNotFoundException, DataIntegrityViolationException, PerformerHasConcertsException {
        Performer performerToDelete = this.performerRepository.findPerformerById(id);
        if (performerToDelete == null) {
            throw new PerformerNotFoundException();
        }
        if (!checkIfPerformerCanBeDeleted(id)) {
            throw new PerformerHasConcertsException();
        }
        this.performerRepository.delete(performerToDelete);
    }

    /**
     * method to retrieve a performer from the repo
     * @param name of performer
     * @return Performer or null if not found
     */
    public Performer retrievePerformer(String name) {
        return !this.performerRepository.findByNameIgnoreCase(name).isEmpty() ?
                this.performerRepository.findByNameIgnoreCase(name).get(0) : null;
    }

    private boolean checkIfPerformerCanBeDeleted(long performerId) {
        ResponseEntity<Boolean> response = webClient.get().uri(performerCheckUri + performerId).retrieve().toEntity(Boolean.class).block();
        if (response != null) {
            System.out.println(response.getBody());
            return Boolean.TRUE.equals(response.getBody());
        }
        return false;
    }
}
