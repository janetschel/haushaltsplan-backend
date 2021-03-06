package de.janetschel.haushaltsplan.backend.service;

import de.janetschel.haushaltsplan.backend.entity.TaskEntity;
import de.janetschel.haushaltsplan.backend.enums.Feedback;
import de.janetschel.haushaltsplan.backend.exception.InvalidAuthenticationTokenException;
import de.janetschel.haushaltsplan.backend.exception.LoginExpiredException;
import de.janetschel.haushaltsplan.backend.exception.UserNotLoggedInException;
import de.janetschel.haushaltsplan.backend.repository.TaskRepository;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "application-test.properties")
public class TaskServiceTest {
    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Value("${authentication.user}")
    private String authtoken;
    private TaskEntity taskEntity;

    private static String property;

    @Before
    public void setupTests() {
        ReflectionTestUtils.setField(taskService, "authtoken", authtoken);
        taskEntity = new TaskEntity("1", "monday", "Kochen", "Jan", "Jan", false, Feedback.GOOD);
        property = System.getProperty("login.valid.thru");
        System.setProperty("login.valid.thru", LocalDateTime.now().plusMinutes(10).toString());
    }

    @AfterAll
    public static void afterTests() {
        System.setProperty("login.valid.thru", property);
    }

    @Test
    @SneakyThrows
    public void givenValidAuthtoken_whenGetDocuments_thenNoException() {
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
    @SneakyThrows
    public void givenInvalidAuthtoken_whenGetDocuments_thenInvalidAuthenticationTokenException() {
        taskService.getDocuments("totally_wrong_authtoken");
    }

    @Test(expected = UserNotLoggedInException.class)
    @SneakyThrows
    public void givenNoLogin_whenGetDocuments_thenUserNotLoggedInException() {
        System.clearProperty("login.valid.thru");
        taskService.getDocuments("totally_wrong_authtoken");
    }

    @Test(expected = LoginExpiredException.class)
    @SneakyThrows
    public void givenExpiredLogin_whenGetDocuments_thenLoginExpiredException() {
        System.setProperty("login.valid.thru", LocalDateTime.now().minusMinutes(1).toString());
        taskService.getDocuments("totally_wrong_authtoken");
    }

    @Test
    @SneakyThrows
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
    @SneakyThrows
    public void givenInvalidAuthtoken_whenAddDocument_thenInvalidAuthenticationTokenException() {
        taskService.addDocument(taskEntity, "totally_wrong_authtoken");
    }

    @Test(expected = UserNotLoggedInException.class)
    @SneakyThrows
    public void givenNoLogin_whenAddDocument_thenUserNotLoggedInException() {
        System.clearProperty("login.valid.thru");
        taskService.addDocument(taskEntity, "totally_wrong_authtoken");
    }

    @Test(expected = LoginExpiredException.class)
    @SneakyThrows
    public void givenExpiredLogin_whenAddDocument_thenLoginExpiredException() {
        System.setProperty("login.valid.thru", LocalDateTime.now().minusMinutes(1).toString());
        taskService.addDocument(taskEntity, "totally_wrong_authtoken");
    }

    @Test
    @SneakyThrows
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
    @SneakyThrows
    public void givenInvalidAuthtoken_whenUpdateDocument_thenInvalidAuthenticationTokenException() {
        taskService.updateDocument(taskEntity, "totally_wrong_authtoken");
    }

    @Test(expected = UserNotLoggedInException.class)
    @SneakyThrows
    public void givenNoLogin_whenUpdateDocument_thenUserNotLoggedInException() {
        System.clearProperty("login.valid.thru");
        taskService.updateDocument(taskEntity, "totally_wrong_authtoken");
    }

    @Test(expected = LoginExpiredException.class)
    @SneakyThrows
    public void givenExpiredLogin_whenUpdateDocument_thenLoginExpiredException() {
        System.setProperty("login.valid.thru", LocalDateTime.now().minusMinutes(1).toString());
        taskService.updateDocument(taskEntity, "totally_wrong_authtoken");
    }

    @Test
    @SneakyThrows
    public void givenValidAuthtoken_whenAddFeedbackToDocument_thenNoException() {
        // Testing successful adding of feedback
        taskEntity.setDone(true);
        Mockito.when(taskRepository.findById(taskEntity.getId())).thenReturn(java.util.Optional.ofNullable(taskEntity));

        ResponseEntity<String> responseEntityOnSuccess =
                taskService.addFeedbackToDocument(taskEntity.getId(), taskEntity.getFeedback(), authtoken);
        String bodyOnSuccess = responseEntityOnSuccess.getBody();
        int statusCodeValueOnSuccess = responseEntityOnSuccess.getStatusCodeValue();

        Assertions.assertThat(bodyOnSuccess).isEqualTo("Feedback successfully added to task");
        Assertions.assertThat(statusCodeValueOnSuccess).isEqualTo(200);

        Mockito.verify(taskRepository, Mockito.times(1)).findById("1");
        Mockito.verify(taskRepository, Mockito.times(1)).deleteById("1");
        Mockito.verify(taskRepository, Mockito.times(1)).save(taskEntity);

        // Testing failed adding of feedback
        // 404
        taskEntity.setDone(false);
        Mockito.when(taskRepository.findById(taskEntity.getId())).thenReturn(java.util.Optional.empty());

        ResponseEntity<String> responseEntityOnFailure =
                taskService.addFeedbackToDocument(taskEntity.getId(), taskEntity.getFeedback(), authtoken);
        String bodyOnFailure = responseEntityOnFailure.getBody();
        int statusCodeValueOnFailure = responseEntityOnFailure.getStatusCodeValue();

        Assertions.assertThat(bodyOnFailure)
                .isEqualTo("Could not add feedback to task. Reason: Task with ID '1' does not exist");
        Assertions.assertThat(statusCodeValueOnFailure).isEqualTo(404);

        Mockito.verify(taskRepository, Mockito.times(2)).findById("1");
        Mockito.verify(taskRepository, Mockito.times(1)).deleteById("1");
        Mockito.verify(taskRepository, Mockito.times(1)).save(taskEntity);

        // 406
        Mockito.when(taskRepository.findById(taskEntity.getId())).thenReturn(java.util.Optional.ofNullable(taskEntity));

        ResponseEntity<String> responseEntityOnFailure2 =
                taskService.addFeedbackToDocument(taskEntity.getId(), taskEntity.getFeedback(), authtoken);
        String bodyOnFailure2 = responseEntityOnFailure2.getBody();
        int statusCodeValueOnFailure2 = responseEntityOnFailure2.getStatusCodeValue();

        Assertions.assertThat(bodyOnFailure2)
                .isEqualTo("Could not add feedback to task. Reason: Task with ID '1' is not completed yet");
        Assertions.assertThat(statusCodeValueOnFailure2).isEqualTo(406);

        Mockito.verify(taskRepository, Mockito.times(3)).findById("1");
        Mockito.verify(taskRepository, Mockito.times(1)).deleteById("1");
        Mockito.verify(taskRepository, Mockito.times(1)).save(taskEntity);
    }

    @Test(expected = InvalidAuthenticationTokenException.class)
    @SneakyThrows
    public void givenInvalidAuthtoken_whenAddFeedbackToDocument_thenInvalidAuthenticationTokenException() {
        taskService.addFeedbackToDocument(taskEntity.getId(), taskEntity.getFeedback(), "totally_wrong_authtoken");
    }

    @Test(expected = UserNotLoggedInException.class)
    @SneakyThrows
    public void givenNoLogin_whenAddFeedbackToDocument_thenUserNotLoggedInException() {
        System.clearProperty("login.valid.thru");
        taskService.addFeedbackToDocument(taskEntity.getId(), taskEntity.getFeedback(), "totally_wrong_authtoken");
    }

    @Test(expected = LoginExpiredException.class)
    @SneakyThrows
    public void givenExpiredLogin_whenAddFeedbackToDocument_thenLoginExpiredException() {
        System.setProperty("login.valid.thru", LocalDateTime.now().minusMinutes(1).toString());
        taskService.addFeedbackToDocument(taskEntity.getId(), taskEntity.getFeedback(), "totally_wrong_authtoken");
    }

    @Test
    @SneakyThrows
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
    @SneakyThrows
    public void givenInvalidAuthtoken_whenDeleteDocument_thenInvalidAuthenticationTokenException() {
        taskService.deleteDocument("1", "totally_wrong_authtoken");
    }

    @Test(expected = UserNotLoggedInException.class)
    @SneakyThrows
    public void givenNoLogin_whenDeleteDocument_thenUserNotLoggedInException() {
        System.clearProperty("login.valid.thru");
        taskService.deleteDocument("1", "totally_wrong_authtoken");
    }

    @Test(expected = LoginExpiredException.class)
    @SneakyThrows
    public void givenExpiredLogin_whenDeleteDocument_thenLoginExpiredException() {
        System.setProperty("login.valid.thru", LocalDateTime.now().minusMinutes(1).toString());
        taskService.deleteDocument("1", "totally_wrong_authtoken");
    }

}
