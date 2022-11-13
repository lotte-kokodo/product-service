package shop.kokodo.productservice.feign.response;

public interface ProductOfCartDto {
    Long getId();
    String getThumbnail();
    String getName();
    Integer getPrice();
    Long getSellerId();
}
