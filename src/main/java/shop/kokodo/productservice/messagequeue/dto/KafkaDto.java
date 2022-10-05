package shop.kokodo.productservice.messagequeue.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class KafkaDto {

    @AllArgsConstructor
    @ToString
    @Getter
    public static class UpdateStock {
        private Long productId;
        private Long qty;
    }

}
