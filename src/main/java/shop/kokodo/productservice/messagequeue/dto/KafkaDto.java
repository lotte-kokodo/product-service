package shop.kokodo.productservice.messagequeue.dto;

import lombok.AllArgsConstructor;
import lombok.ToString;

public class KafkaDto {

    @AllArgsConstructor
    @ToString
    public static class UpdateStock {
        private Long productId;
        private Long qty;
    }

}
