package com.example.se_track_performer.service;

import com.example.se_track_performer.controller.DTO.NewPerformerDTO;
import com.example.se_track_performer.controller.DTO.UpdatePerformerDTO;
import com.example.se_track_performer.exception.InvalidPerformerDTOException;
import com.example.se_track_performer.exception.PerformerHasConcertsException;
import com.example.se_track_performer.exception.PerformerNotFoundException;
import com.example.se_track_performer.model.Performer;
import com.example.se_track_performer.repository.PerformerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PerformerServiceTest {

    @InjectMocks
    private PerformerService performerService;

    @Mock
    private PerformerRepository performerRepository;

    private static Performer performerUnderTest1 = new Performer("test", 50, "pop");
    private static Performer performerUnderTest2 = new Performer("bob", 50, "pop");

    @Test
    void getAllPerformers() {
        Mockito.when(this.performerRepository.findAll()).thenReturn(List.of(performerUnderTest1, performerUnderTest2));
        List<Performer> expected = List.of(performerUnderTest1, performerUnderTest2);
        List<Performer> actual = this.performerService.getAllPerformers();
        assertEquals(expected, actual);
    }

    @Test
    void getPerformerByName() {
        Mockito.when(this.performerRepository.findByNameContainsIgnoreCase("test")).thenReturn(List.of(performerUnderTest1));
        List<Performer> expected = List.of(performerUnderTest1);
        List<Performer> actual = this.performerService.getPerformerByName("test");
        assertEquals(expected, actual);
    }

    @Test
    void getPerformerById() {
        Mockito.when(this.performerRepository.findPerformerById(1L)).thenReturn(performerUnderTest1);
        assertEquals(performerUnderTest1, this.performerService.getPerformerById(1L));
        assertNotEquals(performerUnderTest1, this.performerService.getPerformerById(2L));
    }

    @Test
    void createPerformer() {
        NewPerformerDTO newPerformer = new NewPerformerDTO("test", 50, "pop");
        this.performerService.createPerformer(newPerformer);
        verify(this.performerRepository, times(1)).save(performerUnderTest1);
    }

    @Test
    void updatePerformer() {
        Mockito.when(this.performerRepository.findByNameIgnoreCase("test")).thenReturn(List.of(performerUnderTest1));
        UpdatePerformerDTO updatePerformerDTO = new UpdatePerformerDTO("test", performerUnderTest2.getAge(), performerUnderTest2.getStyle());
        UpdatePerformerDTO updatePerformerDTO2 = new UpdatePerformerDTO("bestaatniet", performerUnderTest2.getAge(), performerUnderTest2.getStyle());
        UpdatePerformerDTO invalidDto = new UpdatePerformerDTO("test", 0, null);
        try {
            this.performerService.updatePerformer(updatePerformerDTO);
        } catch (PerformerNotFoundException | InvalidPerformerDTOException e) {
            e.printStackTrace();
        }
        verify(this.performerRepository, times(1)).save(performerUnderTest1);
        assertThrows(PerformerNotFoundException.class, () -> this.performerService.updatePerformer(updatePerformerDTO2));
        assertThrows(InvalidPerformerDTOException.class, () -> this.performerService.updatePerformer(invalidDto));
    }

    @Test
    void delete() {
        Mockito.when(this.performerRepository.findPerformerById(1L)).thenReturn(performerUnderTest1);
        try {
            this.performerService.delete(1L);
        } catch (PerformerNotFoundException | PerformerHasConcertsException e) {
            e.printStackTrace();
        }
        verify(this.performerRepository, times(1)).delete(performerUnderTest1);
    }

    @Test
    void retrievePerformer() {
        Mockito.when(this.performerRepository.findByNameIgnoreCase("test")).thenReturn(List.of(performerUnderTest1));
        assertEquals(performerUnderTest1, this.performerService.retrievePerformer("test"));
        assertNotEquals(performerUnderTest2, this.performerService.retrievePerformer("test"));
        assertNotEquals(performerUnderTest1, this.performerService.retrievePerformer("bla"));
    }
}