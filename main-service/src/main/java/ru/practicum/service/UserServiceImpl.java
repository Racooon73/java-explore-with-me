package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.model.requests.NewUserRequest;
import ru.practicum.model.User;
import ru.practicum.storage.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User addUser(NewUserRequest dto) {
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .build());
    }

    @Override
    public List<User> getUsers(List<Long> usersIds, int from, int size) {
        PageRequest pageRequest = PageRequest.of((from / size), size, Sort.by(Sort.Direction.ASC, "id"));
        return userRepository.findUsersByIdIn(usersIds, pageRequest);
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }
}
