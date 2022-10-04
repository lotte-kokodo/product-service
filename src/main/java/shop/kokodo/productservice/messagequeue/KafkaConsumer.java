package shop.kokodo.productservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import shop.kokodo.productservice.messagequeue.strategy.DecreaseStockHandler;
import shop.kokodo.productservice.messagequeue.strategy.IncreaseStockHandler;

@Service
@Slf4j
public class KafkaConsumer {

    private final DecreaseStockHandler decreaseStockHandler;
    private final IncreaseStockHandler increaseStockHandler;

    public KafkaConsumer(
        DecreaseStockHandler decreaseStockHandler,
        IncreaseStockHandler increaseStockHandler) {
        this.decreaseStockHandler = decreaseStockHandler;
        this.increaseStockHandler = increaseStockHandler;
    }


    @KafkaListener(topics = "kokodo.product.de-cstock")
    public void decreaseStock(String message) {
        log.info("[KafkaConsumer] consume message: {}", message);

        increaseStockHandler.handle(readMessageValue(message));
    }

    @KafkaListener(topics = "kokodo.product.in-cstock")
    public void increaseStock(String message) {
        log.info("[KafkaConsumer] consume message: {}", message);

        decreaseStockHandler.handle(readMessageValue(message));
    }


    private Map<Object, Object> readMessageValue(String message) {
        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(message, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        return map;
    }
}
