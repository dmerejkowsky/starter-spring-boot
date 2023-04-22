package info.dmerej.todos;

import org.springframework.data.repository.CrudRepository;

public interface TasksRepository extends CrudRepository<Task, Long> {
}
