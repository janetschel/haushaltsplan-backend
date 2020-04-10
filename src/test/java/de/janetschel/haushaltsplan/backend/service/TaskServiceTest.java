package de.janetschel.haushaltsplan.backend.service;

import de.janetschel.haushaltsplan.backend.entity.TaskEntity;
import de.janetschel.haushaltsplan.backend.enums.Feedback;
import de.janetschel.haushaltsplan.backend.exception.InvalidAuthenticationTokenException;
import de.janetschel.haushaltsplan.backend.repository.TaskRepository;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@TestPropertySource(locations= "application-test.properties")
public class TaskServiceTest {
    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Value("${authentication.user}")
    private String authtoken;
    private TaskEntity taskEntity;

    @Before
    public void setupTests() {
        ReflectionTestUtils.setField(taskService, "authtoken", authtoken);
        taskEntity = new TaskEntity("1", "monday", "Kochen", "Jan", "Jan", false, Feedback.GOOD);
    }

    @Test
    @SneakyThrows(InvalidAuthenticationTokenException.class)
    public void givenValidAuthtoken_whenGetDocument_thenNoException() {
        List<TaskEntity> expectedTaskEntities = Collections.singletonList(new TaskEntity());
        Mockito.when(taskRepository.findAll()).thenReturn(expectedTaskEntities);

        ResponseEntity<List<TaskEntity>> documents = taskService.getDocuments(authtoken);
        List<TaskEntity> body = documents.getBody();
        int statusCodeValue = documents.getStatusCodeValue();

        Assertions.assertThat(body).isEqualTo(expectedTaskEntities);
        Assertions.assertThat(statusCodeValue).isEqualTo(200);

        Mockito.verify(taskRepository, Mockito.times(1)).findAll();
    }

    @Test(expected = InvalidAuthenticationTokenException.class)
    @SneakyThrows(InvalidAuthenticationTokenException.class)
    public void givenInvalidAuthtoken_whenGetDocument_thenInvalidAuthenticationTokenException() {
        taskService.getDocuments("totally_wrong_authtoken");
    }

    @Test
    @SneakyThrows(InvalidAuthenticationTokenException.class)
    public void givenValidAuthtoken_whenAddDocument_thenNoException() {
        // Testing successful add
        Mockito.when(taskRepository.findById(taskEntity.getId())).thenReturn(java.util.Optional.empty());

        ResponseEntity<String> responseEntityOnSuccess = taskService.addDocument(taskEntity, authtoken);
        String bodyOnSuccess = responseEntityOnSuccess.getBody();
        int statusCodeValueOnSuccess = responseEntityOnSuccess.getStatusCodeValue();

        Assertions.assertThat(bodyOnSuccess).isEqualTo("Task added successfully");
        Assertions.assertThat(statusCodeValueOnSuccess).isEqualTo(201);

        Mockito.verify(taskRepository, Mockito.times(1)).findById(taskEntity.getId());
        Mockito.verify(taskRepository, Mockito.times(1)).insert(taskEntity);

        // Testing failed add
        Mockito.when(taskRepository.findById(taskEntity.getId())).thenReturn(java.util.Optional.ofNullable(taskEntity));

        ResponseEntity<String> responseEntityOnFailure = taskService.addDocument(taskEntity, authtoken);
        String bodyOnFailure = responseEntityOnFailure.getBody();
        int statusCodeValueOnFailure = responseEntityOnFailure.getStatusCodeValue();

        Assertions.assertThat(bodyOnFailure).isEqualTo("Task with ID '1' does already exist and could therefore not be created");
        Assertions.assertThat(statusCodeValueOnFailure).isEqualTo(409);

        Mockito.verify(taskRepository, Mockito.times(2)).findById(taskEntity.getId());
        Mockito.verify(taskRepository, Mockito.times(1)).insert(taskEntity);
    }

    @Test(expected = InvalidAuthenticationTokenException.class)
    @SneakyThrows(InvalidAuthenticationTokenException.class)
    public void givenInvalidAuthtoken_whenAddDocument_thenInvalidAuthenticationTokenException() {
        taskService.addDocument(taskEntity, "totally_wrong_authtoken");
    }

    @Test
    @SneakyThrows(InvalidAuthenticationTokenException.class)
    public void givenValidAuthtoken_whenUpdateDocument_thenNoException() {
        // Testing successful update
        Mockito.when(taskRepository.findById(taskEntity.getId())).thenReturn(java.util.Optional.ofNullable(taskEntity));

        ResponseEntity<String> responseEntityOnSuccess = taskService.updateDocument(taskEntity, authtoken);
        String bodyOnSuccess = responseEntityOnSuccess.getBody();
        int statusCodeValueOnSuccess = responseEntityOnSuccess.getStatusCodeValue();

        Assertions.assertThat(bodyOnSuccess).isEqualTo("Task updated successfully");
        Assertions.assertThat(statusCodeValueOnSuccess).isEqualTo(200);

        Mockito.verify(taskRepository, Mockito.times(1)).findById("1");
        Mockito.verify(taskRepository, Mockito.times(1)).deleteById("1");
        Mockito.verify(taskRepository, Mockito.times(1)).save(taskEntity);

        // Testing failed update -> add
        taskEntity.setId("1");
        Mockito.when(taskRepository.findById(taskEntity.getId())).thenReturn(java.util.Optional.empty());

        ResponseEntity<String> responseEntityOnFailure = taskService.updateDocument(taskEntity, authtoken);
        String bodyOnFailure = responseEntityOnFailure.getBody();
        int statusCodeValueOnFailure = responseEntityOnFailure.getStatusCodeValue();

        Assertions.assertThat(bodyOnFailure)
                .isEqualTo("Task with ID '1' does not exist and could therefore not be updated. It was created instead");
        Assertions.assertThat(statusCodeValueOnFailure).isEqualTo(202);

        Mockito.verify(taskRepository, Mockito.times(3)).findById("1");
        Mockito.verify(taskRepository, Mockito.times(1)).insert(taskEntity);
        Mockito.verify(taskRepository, Mockito.times(1)).deleteById("1");
        Mockito.verify(taskRepository, Mockito.times(1)).save(taskEntity);
    }

    @Test(expected = InvalidAuthenticationTokenException.class)
    @SneakyThrows(InvalidAuthenticationTokenException.class)
    public void givenInvalidAuthtoken_whenUpdateDocument_thenInvalidAuthenticationTokenException() {
        taskService.updateDocument(taskEntity, "totally_wrong_authtoken");
    }

    @Test
    @SneakyThrows(InvalidAuthenticationTokenException.class)
    public void givenValidAuthtoken_whenDeleteDocument_thenNoException() {
        // Testing successful delete
        Mockito.when(taskRepository.findById(taskEntity.getId())).thenReturn(java.util.Optional.ofNullable(taskEntity));

        ResponseEntity<String> responseEntityOnSuccess = taskService.deleteDocument(taskEntity.getId(), authtoken);
        String bodyOnSuccess = responseEntityOnSuccess.getBody();
        int statusCodeValueOnSuccess = responseEntityOnSuccess.getStatusCodeValue();

        Assertions.assertThat(bodyOnSuccess).isEqualTo("Task deleted successfully");
        Assertions.assertThat(statusCodeValueOnSuccess).isEqualTo(200);

        Mockito.verify(taskRepository, Mockito.times(1)).delete(taskEntity);

        // Testing failed delete
        Mockito.when(taskRepository.findById(taskEntity.getId())).thenReturn(java.util.Optional.empty());

        ResponseEntity<String> responseEntityOnFailure = taskService.deleteDocument(taskEntity.getId(), authtoken);
        String bodyOnFailure = responseEntityOnFailure.getBody();
        int statusCodeValueOnFailure = responseEntityOnFailure.getStatusCodeValue();

        Assertions.assertThat(bodyOnFailure).isEqualTo("Task with ID '1' does not exist and could therefore not be deleted");
        Assertions.assertThat(statusCodeValueOnFailure).isEqualTo(404);

        Mockito.verify(taskRepository, Mockito.times(1)).delete(taskEntity);
    }

    @Test(expected = InvalidAuthenticationTokenException.class)
    @SneakyThrows(InvalidAuthenticationTokenException.class)
    public void givenInvalidAuthtoken_whenDeleteDocument_thenInvalidAuthenticationTokenException() {
        taskService.deleteDocument("1", "totally_wrong_authtoken");
    }

}
