package dev.aziz.phonebook.repository;

import dev.aziz.phonebook.entity.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {

//    @Query("select i from Item i " +
//            "where lower(i.name) like lower(concat('%', :searchTerm, '%')) ")
//    List<Item> searchItem(@Param("searchTerm") String searchTerm);

    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Item> searchItem(String searchTerm);

}
