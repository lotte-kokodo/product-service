package shop.kokodo.productservice.messagequeue.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.messagequeue.KafkaMessageParser;
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
        Map<Long, Integer> productIdQtyMap = parser.readMessageValue(message,
            new TypeReference<Map<Long, Integer>>() {});

        List<Long> productIds = new ArrayList<>(productIdQtyMap.keySet());
        List<Product> products = productRepository.findByIdIn(productIds);

        products.forEach(product -> {
            product.decreaseStock(productIdQtyMap.get(product.getId())); // get QTY.
        });

        productRepository.saveAll(products);

    }


}
