package shop.kokodo.productservice.dto.feign;

public class FeignResponse {

    public interface Price {
        Long getId();
        Integer getPrice();
    }

}
