package shop.kokodo.productservice.dto;

public interface ProductOfOrder {
    Long getId();
    String getThumbnail();
    String getName();
    Integer getPrice();
    Long getSellerId();
}
