package info.dmerej.todos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class ControllerTest {
    private TasksRepository mockRepository;
    private TasksController controller;

    @BeforeEach
    void setUp() {
        mockRepository = mock(TasksRepository.class);
        controller = new TasksController(mockRepository);
    }

    @Test
    public void mark_task_as_complete() {
        var existingTask = new Task(42L, "some task", false);
        when(mockRepository.findById(42L)).thenReturn(Optional.of(existingTask));

        controller.completeTask(42L);

        verify(mockRepository).findById(42L);
        var expectedSave = new Task(42L, "some task", true);
        verify(mockRepository).save(expectedSave);
    }

}
