package shop.kokodo.productservice.messagequeue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import shop.kokodo.productservice.messagequeue.handler.DecreaseStockHandler;

@Service
@Slf4j
public class KafkaConsumer {

    private final DecreaseStockHandler decreaseStockHandler;

    public KafkaConsumer(DecreaseStockHandler decreaseStockHandler) {
        this.decreaseStockHandler = decreaseStockHandler;
    }


    @KafkaListener(topics = "kokodo.product.de-stock")
    public void decreaseStock(String message) {
        log.info("[KafkaConsumer] consume message: {}", message);

        decreaseStockHandler.handle(message);
    }
}
