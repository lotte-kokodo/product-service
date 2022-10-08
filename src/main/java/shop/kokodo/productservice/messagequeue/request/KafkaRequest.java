package shop.kokodo.productservice.messagequeue.request;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import shop.kokodo.productservice.messagequeue.KafkaMessageType;

public class KafkaRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class ProductUpdateStock {
        private Long productId;
        private Integer qty;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class ProductUpdateStockMap<K, V> {
        private Map<K, V> map;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class KafkaMessage<T> {
        private KafkaMessageType type;
        private T data;
    }


}
