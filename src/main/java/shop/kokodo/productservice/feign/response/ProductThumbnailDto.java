package shop.kokodo.productservice.feign.response;

public interface ProductThumbnailDto {
    Long getId();
    String getName();
    String getDisplayName();
    String getThumbnail();
}
