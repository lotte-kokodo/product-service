package shop.kokodo.productservice.messagequeue;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import shop.kokodo.productservice.messagequeue.handler.DecreaseStockHandler;
import shop.kokodo.productservice.messagequeue.handler.DecreaseStockRollbackHandler;
import shop.kokodo.productservice.messagequeue.handler.SaveProductHandler;
import shop.kokodo.productservice.messagequeue.message.KafkaOrderDto;
import shop.kokodo.productservice.messagequeue.message.KafkaOrderDto.KafkaCouponNameDto;

@Service
@Slf4j
public class KafkaConsumer {

    private final KafkaMessageParser parser;
    private final KafkaProducer kafkaProducer;
    private final DecreaseStockHandler decreaseStockHandler;
    private final DecreaseStockRollbackHandler decreaseStockRollbackHandler;
    private final SaveProductHandler saveProductHandler;

    public KafkaConsumer(KafkaMessageParser parser,
        KafkaProducer kafkaProducer,
        DecreaseStockHandler decreaseStockHandler,
        DecreaseStockRollbackHandler decreaseStockRollbackHandler,
        SaveProductHandler saveProductHandler) {
        this.parser = parser;
        this.kafkaProducer = kafkaProducer;
        this.decreaseStockHandler = decreaseStockHandler;
        this.decreaseStockRollbackHandler = decreaseStockRollbackHandler;
        this.saveProductHandler = saveProductHandler;
    }


    @KafkaListener(topics = "product-decrease-stock")
    public void decreaseStock(String message) {
        log.info("[KafkaConsumer] consume message: {}", message);

        try {
            decreaseStockHandler.handle(message);

            if (didUseCoupon(message)) {
                kafkaProducer.send("promotion-coupon-status", message);
            }
        }
        catch (Exception exception) {
            kafkaProducer.send("order-rollback", message);
        }
    }

    private Boolean didUseCoupon(String message) {
        KafkaCouponNameDto couponNameDto = parser.readMessageValue(message, new TypeReference<KafkaOrderDto>(){}).getKafkaCouponNameDto();
        return couponNameDto != null;
    }

    @KafkaListener(topics = "product-decrease-stock-rollback")
    public void decreaseStockRollback(String message) {
        log.info("[KafkaConsumer] consume message: {}", message);

        try {
            decreaseStockRollbackHandler.handle(message);
            kafkaProducer.send("order-rollback", message);
        }
        catch (Exception exception) {
            kafkaProducer.send("order-rollback", message);
        }
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

    @KafkaListener(topics = "product-stock-update")
    public void updateStock(String message){
        log.info("[KafkaConsumer] consume product-stock-update: {}",message);

        saveProductHandler.updateStock(message);
    }
}
