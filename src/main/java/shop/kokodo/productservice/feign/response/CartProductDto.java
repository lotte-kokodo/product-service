package shop.kokodo.productservice.feign.response;

public interface CartProductDto {
    Long getId();
    String getThumbnail();
    String getName();
    Integer getPrice();
    Long getSellerId();
}
