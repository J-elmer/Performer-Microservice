package com.example.se_track_performer.controller;

import com.example.se_track_performer.controller.DTO.JsonResponseDTO;
import com.example.se_track_performer.controller.DTO.NewPerformerDTO;
import com.example.se_track_performer.controller.DTO.UpdatePerformerDTO;
import com.example.se_track_performer.exception.InvalidPerformerDTOException;
import com.example.se_track_performer.exception.PerformerNotFoundException;
import com.example.se_track_performer.model.Performer;
import com.example.se_track_performer.service.PerformerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("performer")
public class PerformerController {

    private final PerformerService performerService;

    @Autowired
    public PerformerController(PerformerService performerService) {
        this.performerService = performerService;
    }

    /**
     * endpoint to get all performers
     *
     * @return List of all performers in db
     */
    @GetMapping(value = "/all")
    public List<Performer> getPerformers() {
        return this.performerService.getAllPerformers();
    }

    /**
     * endpoint to search performers by name
     *
     * @param name search parameter
     * @return List of performers matching the criteria
     */
    @GetMapping(value = "/byName")
    public List<Performer> getPerformerByName(@RequestParam String name) {
        return this.performerService.getPerformerByName(name);
    }

    /**
     * endpoint to get a performer by id
     *
     * @param id of performer
     * @return Performer
     */
    @GetMapping("/{id}")
    public Performer getById(@PathVariable Long id) {
        Performer performer = this.performerService.getPerformerById(id);
        if (performer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Performer with id " + id + " not found");
        }
        return performer;
    }

    /**
     * endpoint to create new performer; validation is done in DTO class
     *
     * @param newPerformerDTO object in Json
     * @return Httpstatus 201 if all went well, Httpstatus 409 if there already is a performer with that name
     */
    @PostMapping(value = "/new")
    public ResponseEntity<?> createNewPerformer(@Validated @RequestBody NewPerformerDTO newPerformerDTO) {
        try {
            this.performerService.createPerformer(newPerformerDTO);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Performer with name " + newPerformerDTO.getName() + " already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    /**
     * endpoint to update performer.
     *
     * @param updatePerformerDTO dto class containing the necessary info
     * @return HttpStatus 200 if all went well, otherwise it will return 400
     */
    @PutMapping(value = "/update")
    public ResponseEntity<?> updatePerformer(@Validated @RequestBody UpdatePerformerDTO updatePerformerDTO) {
        try {
            this.performerService.updatePerformer(updatePerformerDTO);
        } catch (PerformerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new JsonResponseDTO("No performer found with name " + updatePerformerDTO.getName()));
        } catch (InvalidPerformerDTOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new JsonResponseDTO("No name or style provided, can't update performer"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * endpoint to delete a performer by id. Could also be done by name, since these are unique. Can be added later
     *
     * @param id of Performer
     * @return HttpStatus 200 if all went well, otherwise it returns 400
     */
    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deletePerformer(@RequestParam Long id) {
        try {
            this.performerService.delete(id);
        } catch (PerformerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new JsonResponseDTO("No performer found with id " + id));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new JsonResponseDTO("Could not delete performer with id " + id + " for it is still associated with concerts"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
