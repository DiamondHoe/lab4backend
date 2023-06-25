package com.psw.lab4.repositories;

import com.psw.lab4.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT u FROM User u JOIN FETCH u.events WHERE u.id = :id")
    Optional<User> findByIdWithEvents(@Param("id") Long id);
}
