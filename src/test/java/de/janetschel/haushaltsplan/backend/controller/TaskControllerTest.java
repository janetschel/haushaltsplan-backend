package de.janetschel.haushaltsplan.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.janetschel.haushaltsplan.backend.entity.TaskEntity;
import de.janetschel.haushaltsplan.backend.exception.InvalidAuthenticationTokenException;
import de.janetschel.haushaltsplan.backend.service.TaskService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private TaskEntity taskEntity;
    private String id;

    @BeforeEach
    public void setupTests() {
        taskEntity = new TaskEntity("id", "monday", "Kochen", "Jan", "Jan", false, -1);
        id = "id";
    }

    @Test
    public void givenValidHeader_whenGetDocuments_thenListOfDocuments_andStatus200() throws Exception {
        String authtoken = "authtoken";
        Mockito.when(taskService.getDocuments(authtoken)).thenReturn(ResponseEntity.ok(Collections.singletonList(taskEntity)));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/getDocuments").header("Auth-Token", authtoken))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[" + taskEntityAsJson(taskEntity) + "]"));

        Mockito.verify(taskService, Mockito.times(1)).getDocuments(authtoken);
    }

    @Test
    public void givenInvalidHeader_whenGetDocuments_thenStatus403() throws Exception {
        String authtoken = "wrongauthtoken";
        Mockito.when(taskService.getDocuments(authtoken)).thenThrow(InvalidAuthenticationTokenException.class);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/getDocuments").header("Auth-Token", authtoken))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());

        Mockito.verify(taskService, Mockito.times(1)).getDocuments(authtoken);
    }

    @Test
    public void givenNoHeader_whenGetDocuments_thenStatus400() throws Exception {
        String authtoken = "authtoken";
        Mockito.when(taskService.getDocuments(authtoken)).thenReturn(ResponseEntity.ok(Collections.singletonList(taskEntity)));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/getDocuments"))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(taskService, Mockito.never()).getDocuments(authtoken);
    }

    @Test
    public void givenValidHeader_whenAddDocument_thenSuccessfulMessage_andStatus200() throws Exception {
        String authtoken = "authtoken";
        Mockito.when(taskService.addDocument(taskEntity, authtoken)).thenReturn(ResponseEntity.ok("Update successful"));

        this.mockMvc.perform(MockMvcRequestBuilders
                    .post("/addDocument").contentType(MediaType.APPLICATION_JSON).content(taskEntityAsJson(taskEntity))
                    .header("Auth-Token", authtoken))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Update successful")));

        Mockito.verify(taskService, Mockito.times(1)).addDocument(taskEntity, authtoken);
    }

    @Test
    public void givenInvalidHeader_whenAddDocument_thenStatus403() throws Exception {
        String authtoken = "wrongauthtoken";
        Mockito.when(taskService.addDocument(taskEntity, authtoken)).thenThrow(InvalidAuthenticationTokenException.class);

        this.mockMvc.perform(MockMvcRequestBuilders
                    .post("/addDocument").contentType(MediaType.APPLICATION_JSON).content(taskEntityAsJson(taskEntity))
                    .header("Auth-Token", authtoken))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());

        Mockito.verify(taskService, Mockito.times(1)).addDocument(taskEntity, authtoken);
    }

    @Test
    public void givenNoHeader_whenAddDocument_thenSuccessfulMessage_andStatus400() throws Exception {
        String authtoken = "authtoken";
        Mockito.when(taskService.addDocument(taskEntity, authtoken)).thenReturn(ResponseEntity.ok("Update successful"));

        this.mockMvc.perform(MockMvcRequestBuilders
                    .post("/addDocument").contentType(MediaType.APPLICATION_JSON).content(taskEntityAsJson(taskEntity)))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(taskService, Mockito.never()).addDocument(taskEntity, authtoken);
    }

    @Test
    public void givenNoBody_whenAddDocument_thenStatus400() throws Exception {
        String authtoken = "authtoken";
        Mockito.when(taskService.addDocument(taskEntity, authtoken)).thenReturn(ResponseEntity.ok("Update successful"));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/addDocument").header("Auth-Token", authtoken))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(taskService, Mockito.never()).addDocument(taskEntity, authtoken);
    }

    @Test
    public void givenValidHeader_whenUpdateDocument_thenSuccessfulMessage_andStatus200() throws Exception {
        String authtoken = "authtoken";
        Mockito.when(taskService.updateDocument(taskEntity, authtoken)).thenReturn(ResponseEntity.ok("Update successful"));

        this.mockMvc.perform(MockMvcRequestBuilders
                    .put("/updateDocument").contentType(MediaType.APPLICATION_JSON).content(taskEntityAsJson(taskEntity))
                    .header("Auth-Token", authtoken))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Update successful")));

        Mockito.verify(taskService, Mockito.times(1)).updateDocument(taskEntity, authtoken);
    }

    @Test
    public void givenInvalidHeader_whenUpdateDocument_thenStatus403() throws Exception {
        String authtoken = "wrongauthtoken";
        Mockito.when(taskService.updateDocument(taskEntity, authtoken)).thenThrow(InvalidAuthenticationTokenException.class);

        this.mockMvc.perform(MockMvcRequestBuilders
                    .put("/updateDocument").contentType(MediaType.APPLICATION_JSON).content(taskEntityAsJson(taskEntity))
                    .header("Auth-Token", authtoken))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());

        Mockito.verify(taskService, Mockito.times(1)).updateDocument(taskEntity, authtoken);
    }

    @Test
    public void givenNoHeader_whenUpdateDocument_thenStatus400() throws Exception {
        String authtoken = "authtoken";
        Mockito.when(taskService.updateDocument(taskEntity, authtoken)).thenReturn(ResponseEntity.ok("Update successful"));

        this.mockMvc.perform(MockMvcRequestBuilders
                    .put("/updateDocument").contentType(MediaType.APPLICATION_JSON).content(taskEntityAsJson(taskEntity)))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(taskService, Mockito.never()).updateDocument(taskEntity, authtoken);
    }

    @Test
    public void givenNoBody_whenUpdateDocument_thenStatus400() throws Exception {
        String authtoken = "authtoken";
        Mockito.when(taskService.updateDocument(taskEntity, authtoken)).thenReturn(ResponseEntity.ok("Update successful"));

        this.mockMvc.perform(MockMvcRequestBuilders.put("/updateDocument").header("Auth-Token", authtoken))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(taskService, Mockito.never()).updateDocument(taskEntity, authtoken);
    }

    @Test
    public void givenValidHeader_whenDeleteDocument_thenSuccessfulMessage_andStatus200() throws Exception {
        String authtoken = "authtoken";
        Mockito.when(taskService.deleteDocument(id, authtoken)).thenReturn(ResponseEntity.ok("Delete performed successfully"));

        this.mockMvc.perform(MockMvcRequestBuilders
                    .delete("/deleteDocument").param("id", id)
                    .header("Auth-Token", authtoken))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Delete performed successfully")));

        Mockito.verify(taskService, Mockito.times(1)).deleteDocument(id, authtoken);
    }

    @Test
    public void givenInvalidHeader_whenDeleteDocument_thenStatus403() throws Exception {
        String authtoken = "wrongauthtoken";
        Mockito.when(taskService.deleteDocument(id, authtoken)).thenThrow(InvalidAuthenticationTokenException.class);

        this.mockMvc.perform(MockMvcRequestBuilders
                    .delete("/deleteDocument").param("id", id)
                    .header("Auth-Token", authtoken))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());

        Mockito.verify(taskService, Mockito.times(1)).deleteDocument(id, authtoken);
    }

    @Test
    public void givenNoHeader_whenDeleteDocument_thenStatus400() throws Exception {
        String authtoken = "authtoken";
        Mockito.when(taskService.deleteDocument(id, authtoken)).thenReturn(ResponseEntity.ok("Delete performed successfully"));

        this.mockMvc.perform(MockMvcRequestBuilders
                    .delete("/deleteDocument").param("id", id))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(taskService, Mockito.never()).deleteDocument(id, authtoken);
    }

    @Test
    public void givenNoIdAsRequestParam_whenDeleteDocument_thenStatus400() throws Exception {
        String authtoken = "authtoken";
        Mockito.when(taskService.deleteDocument(id, authtoken)).thenReturn(ResponseEntity.ok("Delete performed successfully"));

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/deleteDocument").header("Auth-Token", authtoken))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(taskService, Mockito.never()).deleteDocument(id, authtoken);
    }


    private String taskEntityAsJson(TaskEntity taskEntity) {
        String stringToReturn = "";
        try {
            stringToReturn = new ObjectMapper().writeValueAsString(taskEntity);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return stringToReturn;
    }
}
