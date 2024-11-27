package dev.aziz.phonebook.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {

    @Id
    private String id;

    private String name;

    private Integer amount;

    private String unitOfMeasure;

    private BigDecimal price;

}
