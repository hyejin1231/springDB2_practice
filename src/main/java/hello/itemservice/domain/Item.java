package hello.itemservice.domain;

import lombok.Data;

import java.util.Locale;

/**
 * 상품 객체를 나타내는 Item
 */
@Data
public class Item {

    private Long key;

    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
