package shop.kokodo.productservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import shop.kokodo.productservice.messagequeue.strategy.KafkaMessageHandlerAdapter;
import shop.kokodo.productservice.messagequeue.strategy.KafkaMessageType;

@Service
@Slf4j
public class KafkaConsumer {

    private final KafkaMessageHandlerAdapter handlerAdapter;

    @Autowired
    public KafkaConsumer(
        KafkaMessageHandlerAdapter handlerAdapter) {
        this.handlerAdapter = handlerAdapter;
    }

    @KafkaListener(topics = "kokodo.product", groupId = "product")
    public void listen(String message) {
        log.info("[KafkaConsumer] message: {}", message);

        Map<Object, Object> map = readMessageValue(message);
        handlerAdapter.handleKafkaMessage((KafkaMessageType) map.get("messageType"), map);
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
