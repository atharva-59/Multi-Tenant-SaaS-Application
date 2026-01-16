package com.taskflow.backend.repository;

//import com.taskflow.domain.Task;
import com.taskflow.backend.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // No need to add "findByTenantId" here!
    // The Hibernate Filter handles it automatically.
}