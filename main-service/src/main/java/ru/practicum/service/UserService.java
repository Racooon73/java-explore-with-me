package ru.practicum.service;

import ru.practicum.model.requests.NewUserRequest;
import ru.practicum.model.User;

import java.util.List;

public interface UserService {
    User addUser(NewUserRequest dto);

    List<User> getUsers(List<Long> usersIds, int from, int size);

    void deleteUser(long userId);
}
