package shop.kokodo.productservice.dto;

public interface OrderSheetProductDto {
    Long getId();
    String getThumbnail();
    String getName();
    Integer getPrice();
    Long sellerId();
}
