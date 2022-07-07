package hello.itemservice.repository;

import lombok.Data;

/**
 * 검색 조건으로 사용되는 객체 (상품명, 최대 가격)
 */
@Data
public class ItemSearchCond {
    private String itemName;
    private Integer maxPrice;

    public ItemSearchCond() {
    }

    public ItemSearchCond(String itemName, Integer maxPrice) {
        this.itemName = itemName;
        this.maxPrice = maxPrice;
    }
}
