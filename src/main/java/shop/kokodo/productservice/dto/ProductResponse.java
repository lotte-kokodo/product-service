package shop.kokodo.productservice.dto;

import lombok.Builder;
import lombok.Getter;

public class ProductResponse {

    @Getter @Builder
    public static class GetOrderProduct {
        private Long id;
        private String thumbnail;
        private String name;
        private Integer price;
    }

}
