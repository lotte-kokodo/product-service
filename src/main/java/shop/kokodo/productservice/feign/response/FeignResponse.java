package shop.kokodo.productservice.feign.response;

public class FeignResponse {

    public interface Price {
        Long getId();
        Integer getPrice();
    }

    public interface ProductOfOrder {
        Long getId();
        String getThumbnail();
        String getName();
        Integer getPrice();
        Long getSellerId();
    }

    public interface Stock {
        Long getId();
        Integer getStock();
    }
}
