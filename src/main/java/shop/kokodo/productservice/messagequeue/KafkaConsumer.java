package shop.kokodo.productservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import shop.kokodo.productservice.dto.kafka.ProductAndDetailDto;
import shop.kokodo.productservice.messagequeue.handler.DecreaseStockHandler;
import shop.kokodo.productservice.messagequeue.handler.SaveProductHandler;

@Service
@Slf4j
public class KafkaConsumer {

    private final DecreaseStockHandler decreaseStockHandler;
    private final ObjectMapper objectMapper;
    private final SaveProductHandler saveProductHandler;

    public KafkaConsumer(DecreaseStockHandler decreaseStockHandler,ObjectMapper objectMapper, SaveProductHandler saveProductHandler) {
        this.decreaseStockHandler = decreaseStockHandler;
        this.objectMapper = objectMapper;
        this.saveProductHandler = saveProductHandler;
    }


    @KafkaListener(topics = "product-decrease-stock")
    public void decreaseStock(String message) {
        log.info("[KafkaConsumer] consume message: {}", message);

        decreaseStockHandler.handle(message);
    }

    @KafkaListener(topics = "product-save")
    public void productSave(String message) {
        log.info("[KafkaConsumer] consume message: {}", message);

        saveProductHandler.saveProduct(message);

    }

    @KafkaListener(topics = "product-save-template")
    public void productSaveTemplate(String message){
        log.info("[KafkaConsumer] consume product-save-template message: {}",message);

        saveProductHandler.saveProductTemplate(message);
    }
}
