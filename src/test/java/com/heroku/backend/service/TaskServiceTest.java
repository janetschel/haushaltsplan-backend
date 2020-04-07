package com.heroku.backend.service;

import com.heroku.backend.repository.TaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @Test
    public void whenFindAll_thenNoException() {
        taskRepository.findAll();
        Mockito.verify(taskRepository, Mockito.times(1)).findAll();
    }

}
