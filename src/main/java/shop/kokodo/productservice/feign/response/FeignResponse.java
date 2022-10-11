package shop.kokodo.productservice.feign.response;

public class FeignResponse {

    public interface Price {
        Long getId();
        Integer getPrice();
    }

    public interface ProductOfCart {
        Long getId();
        String getThumbnail();
        String getName();
    }

    public interface Stock {
        Long getId();
        Integer getStock();
    }

    public interface ProductOfOrderSheet {
        Long getId();
        String getThumbnail();
        String getName();
        Integer getPrice();
    }
}
