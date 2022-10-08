package shop.kokodo.productservice.messagequeue;

public enum KafkaMessageType {

    ORDER_SINGLE_PRODUCT("ORDER SINGLE PRODUCT"),
    ORDER_CART_PRODUCT("ORDER CART PRODUCT");

    private final String key;

    KafkaMessageType(String key) {
        this.key = key;
    }

    public String getName() { return name(); }

    public String getKey() {
        return this.key;
    }

}
