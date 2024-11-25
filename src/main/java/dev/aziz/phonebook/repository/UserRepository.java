package dev.aziz.phonebook.repository;

import dev.aziz.phonebook.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

}
