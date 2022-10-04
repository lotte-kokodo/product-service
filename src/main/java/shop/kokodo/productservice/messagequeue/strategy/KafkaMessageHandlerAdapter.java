package shop.kokodo.productservice.messagequeue.strategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageHandlerAdapter {

    private final Map<KafkaMessageType, Object> handlerMap = new ConcurrentHashMap<>();

    private final DecreaseStockHandler decreaseStockHandler;
    private final IncreaseStockHandler increaseStockHandler;

    @Autowired
    public KafkaMessageHandlerAdapter(
        DecreaseStockHandler decreaseStockHandler,
        IncreaseStockHandler increaseStockHandler) {
        this.decreaseStockHandler = decreaseStockHandler;
        this.increaseStockHandler = increaseStockHandler;

        initHandler();
    }

    private void initHandler() {
        handlerMap.put(KafkaMessageType.DECREASE_STOCK, decreaseStockHandler);
        handlerMap.put(KafkaMessageType.INCREASE_STOCK, increaseStockHandler);
    }

    public void handleKafkaMessage(KafkaMessageType type, Map<Object, Object> map) {
        KafkaMessageHandler handler = (KafkaMessageHandler) handlerMap.get(type);
        handler.handle(map);
    }

}
