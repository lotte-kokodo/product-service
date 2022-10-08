package shop.kokodo.productservice.messagequeue.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.TrayIcon.MessageType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.exception.ExceptionMessage;
import shop.kokodo.productservice.feign.request.FeignRequest.UpdateStock;
import shop.kokodo.productservice.messagequeue.KafkaMessageParser;
import shop.kokodo.productservice.messagequeue.KafkaMessageType;
import shop.kokodo.productservice.messagequeue.request.KafkaRequest;
import shop.kokodo.productservice.messagequeue.request.KafkaRequest.KafkaMessage;
import shop.kokodo.productservice.messagequeue.request.KafkaRequest.ProductUpdateStock;
import shop.kokodo.productservice.messagequeue.request.KafkaRequest.ProductUpdateStockMap;
import shop.kokodo.productservice.repository.ProductRepository;

/**
 * '주문취소' 시 상품 재고 증가
 */
@Component
@Slf4j
public class DecreaseStockHandler implements KafkaMessageHandler {

    private final ObjectMapper objectMapper;

    private final ProductRepository productRepository;

    private final KafkaMessageParser parser;

    @Autowired
    public DecreaseStockHandler(
        ObjectMapper objectMapper,
        ProductRepository productRepository,
        KafkaMessageParser parser) {
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
        this.parser = parser;
    }

    @Override
    public void handle(String message) {
        Map<Object, Object> map = parser.readMessageValue(message, new TypeReference<Map<Object, Object>>() {});

        KafkaMessageType kafkaMessageType = KafkaMessageType.valueOf((String) map.get("type"));

        switch (kafkaMessageType) {
            case ORDER_SINGLE_PRODUCT:
                ProductUpdateStock updateStock = parser.readMessageValue(
                    message, new TypeReference<KafkaMessage<ProductUpdateStock>>() {}).getData();

                orderSingleProduct(updateStock.getProductId(), updateStock.getQty());
                break;
            case ORDER_CART_PRODUCT:
                Map<Long, Integer> updateStockMap = parser.readMessageValue(
                    message, new TypeReference<KafkaMessage<ProductUpdateStockMap>>() {}).getData().getMap();
                orderCartProduct(updateStockMap);
                break;
        }

    }

    private void orderSingleProduct(Long productId, Integer qty) {
        Optional<Product> findProduct = productRepository.findById(productId);
        if (findProduct.isEmpty()) {
            log.error("[IncreaseStockHandler] Product(id = '{}') not founded.", productId);
            throw new IllegalArgumentException(ExceptionMessage.PRODUCT_NOT_FOUND);
        }

        Product product = findProduct.get();
        product.decreaseStock(qty);

        productRepository.save(product);
    }

    private void orderCartProduct(Map<Long, Integer> productIdQtyMap) {
        List<Long> productIds = new ArrayList<>(productIdQtyMap.keySet());
        List<Product> products = productRepository.findByIdIn(productIds);

        products.forEach(product -> {
            product.decreaseStock(productIdQtyMap.get(product.getId())); // get QTY.
        });

        productRepository.saveAll(products);
    }


}
