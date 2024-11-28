package dev.aziz.phonebook.service;

import dev.aziz.phonebook.entity.Item;
import dev.aziz.phonebook.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final MongoTemplate mongoTemplate;

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

    public List<Item> getItemsByFilterAllFields(String name,
                                                Double minAmount,
                                                Double maxAmount,
                                                String unitOfMeasure,
                                                Double minPrice,
                                                Double maxPrice) {
        Query query = new Query();

        if (name != null && !name.isEmpty()) {
            query.addCriteria(Criteria.where("name").regex(".*" + name + ".*", "i"));
        }

        if (minAmount != null && maxAmount == null) {
            Integer minAmountInt = minAmount.intValue();
            System.out.println("Only min Amount is given");
            query.addCriteria(Criteria.where("amount")
                    .gte(minAmountInt));
        } else if (minAmount == null && maxAmount != null) {
            Integer maxAmountInt = maxAmount.intValue();
            System.out.println("Only max Amount is given");
            query.addCriteria(Criteria.where("amount")
                    .lte(maxAmountInt));
        } else if (minAmount != null && maxAmount != null) {
            Integer minAmountInt = minAmount.intValue();
            Integer maxAmountInt = maxAmount.intValue();
            System.out.println("Both Amounts are given");
            query.addCriteria(Criteria.where("amount")
                    .gte(minAmountInt)
                    .lte(maxAmountInt));
        }

        if (unitOfMeasure != null && !unitOfMeasure.isEmpty()) {
            query.addCriteria(Criteria.where("unitOfMeasure").regex(".*" + unitOfMeasure + ".*", "i"));
        }
        if (minPrice != null && maxPrice == null) {
            query.addCriteria(Criteria.where("price")
                    .gte(minPrice));
        } else if (minPrice == null && maxPrice != null) {
            query.addCriteria(Criteria.where("price")
                    .lte(maxPrice));
        } else if (minPrice != null && maxPrice != null) {
            query.addCriteria(Criteria.where("price")
                    .gte(minPrice)
                    .lte(maxPrice));
        }
        System.out.println(query);
        return mongoTemplate.find(query, Item.class);
    }

}
