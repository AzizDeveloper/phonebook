package dev.aziz.phonebook.service;

import dev.aziz.phonebook.entity.User;
import dev.aziz.phonebook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String createUser(User user){
        return userRepository.save(user).getId();
    }

    public void deleteUserById(String id){
        userRepository.deleteById(id);
    }

    public User updateUser(String id, User user){
        User userById = getUserById(id);
        if (userById !=null) {
            user.setId(id);
        }
        return userRepository.save(user);
    }

}
