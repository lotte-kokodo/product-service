package shop.kokodo.productservice.messagequeue.strategy;

import java.util.Map;

public interface KafkaMessageHandler {

    void handle(Map<Object, Object> map);

}
