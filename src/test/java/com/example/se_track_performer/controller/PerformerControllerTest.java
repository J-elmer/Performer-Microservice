package com.example.se_track_performer.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.example.se_track_performer.controller.DTO.NewPerformerDTO;
import com.example.se_track_performer.controller.DTO.UpdatePerformerDTO;
import com.example.se_track_performer.model.Performer;
import com.example.se_track_performer.service.PerformerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PerformerController.class)
class PerformerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PerformerService performerService;

    @Test
    void getPerformers() {
        try {
            mockMvc.perform(get("/performer/all")
                            .contentType("application/json"))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    void getPerformerByName() {
        try {
            mockMvc.perform(get("/performer/byName")
                            .contentType("application/json")
                            .param("name", "test"))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    void getById() {
        Mockito.when(this.performerService.getPerformerById(1L)).thenReturn(new Performer());
        try {
            mockMvc.perform(get("/performer/1")
                            .contentType("application/json"))
                    .andExpect(status().isOk());
            mockMvc.perform(get("/performer/2")
                            .contentType("application/json"))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    void createNewPerformer() {
        NewPerformerDTO newPerformer = new NewPerformerDTO("Test", 50, "pop");
        NewPerformerDTO newPerformer2 = new NewPerformerDTO("Test", 0, "pop");

        try {
            mockMvc.perform(post("/performer/new")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(newPerformer)))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            System.out.println(e);
        }

        MvcResult mvcResult = null;
        try {
            mvcResult = mockMvc.perform(post("/performer/new")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(newPerformer2)))
                    .andExpect(status().isBadRequest())
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void updatePerformer() {
        UpdatePerformerDTO updatePerformerDTO = new UpdatePerformerDTO("Test2", 1, "rock");
        try {
            mockMvc.perform(put("/performer/update")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(updatePerformerDTO)))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    void deletePerformer() {
        try {
            mockMvc.perform(delete("/performer/delete")
                            .contentType("application/json")
                            .param("id", "1"))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}