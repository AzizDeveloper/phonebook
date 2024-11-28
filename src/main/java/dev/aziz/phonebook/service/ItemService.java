package dev.aziz.phonebook.service;

import dev.aziz.phonebook.entity.Item;
import dev.aziz.phonebook.entity.User;
import dev.aziz.phonebook.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemById(String id) {
        return itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public String createItem(Item item) {
        return itemRepository.save(item).getId();
    }

    public void deleteItemById(String id) {
        itemRepository.deleteById(id);
    }

    public Item updateItem(String id, Item item) {
        Item itemById = getItemById(id);
        if (itemById != null) {
            item.setId(id);
        }
        return itemRepository.save(item);
    }

    public List<Item> getItemsByFilter(String search) {
        System.out.println("Service layer search: " + search);
        return itemRepository.searchItem(search);
    }

}
