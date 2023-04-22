package info.dmerej.todos;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class TasksController {
    private final TasksRepository repository;

    public TasksController(TasksRepository repository) {
        this.repository = repository;
    }

    @PostMapping(path = "/task")
    public Task addTask(@RequestBody String description) {
        var task = new Task(null, description, false);
        return repository.save(task);
    }

    @GetMapping(path = "/task/{id}")
    public Optional<Task> getTask(@PathVariable Long id) {
        var res = repository.findById(id);
        return res;
    }

    @PostMapping(path = "/task/{id}/complete")
    public void completeTask(@PathVariable Long id) {
        var task = repository.findById(id).get();
        task.setDone(true);
        repository.save(task);
    }
}
