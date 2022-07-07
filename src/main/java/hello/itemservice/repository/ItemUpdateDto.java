package hello.itemservice.repository;

import lombok.Data;

/**
 * DTO(Data Transfer Object)
 * : 기능이 없이 단순히 데이터를 전달하는 용도로 사용
 */
@Data
public class ItemUpdateDto {
    private String itemName;
    private Integer price;
    private Integer quantity;

    public ItemUpdateDto() {
    }

    public ItemUpdateDto(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
