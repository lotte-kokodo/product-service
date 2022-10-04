package shop.kokodo.productservice.messagequeue.strategy;

public enum KafkaMessageType {

    DECREASE_STOCK("DECREASE STOCK", 1), INCREASE_STOCK("INCREASE STOCK", 2);

    private final String key;
    private final Integer value;

    KafkaMessageType(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }
    public Integer getValue() {
        return value;
    }
}
