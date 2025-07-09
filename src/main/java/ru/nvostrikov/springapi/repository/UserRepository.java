package ru.nvostrikov.springapi.repository;

import ru.nvostrikov.springapi.entity.Status;
import ru.nvostrikov.springapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByStatus(Status status);
    List<User> findByLastStatusChangeAfter(Instant timestamp);
    List<User> findByStatusAndLastStatusChangeAfter(Status status, Instant timestamp);
}
