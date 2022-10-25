package shop.kokodo.productservice.messagequeue.handler;

public interface KafkaMessageHandler {

    void handle(String message);

}
