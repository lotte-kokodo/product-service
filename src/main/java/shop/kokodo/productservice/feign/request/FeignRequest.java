package shop.kokodo.productservice.feign.request;

import lombok.Getter;

public class FeignRequest {

    @Getter
    public static class UpdateStock {
        private Long productId;
        private Integer qty;
    }

}
