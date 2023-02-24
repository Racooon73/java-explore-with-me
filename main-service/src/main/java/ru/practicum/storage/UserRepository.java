package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.User;
import org.springframework.data.domain.Pageable;
import ru.practicum.model.dto.UserShortDto;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findUsersByIdIn(List<Long> ids, Pageable page);

    @Query(nativeQuery = true)
    UserShortDto findByIdShort(Long id);
}
