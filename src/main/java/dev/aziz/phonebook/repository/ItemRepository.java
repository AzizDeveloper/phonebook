package dev.aziz.phonebook.repository;

import dev.aziz.phonebook.entity.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<Item, String> {

}
