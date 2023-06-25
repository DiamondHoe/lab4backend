package com.psw.lab4.repositories;

import com.psw.lab4.entities.Event;
import com.psw.lab4.entities.User;
import com.psw.lab4.entities.UserEvent;
import org.springframework.data.repository.CrudRepository;

public interface UserEventRepository extends CrudRepository<UserEvent, Long> {
    boolean existsByUserAndEvent(User user, Event event);
}
