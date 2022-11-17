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
import shop.kokodo.productservice.messagequeue.message.KafkaOrderDto;
import shop.kokodo.productservice.repository.ProductRepository;

/**
 * '프로모션' 에서 쿠폰 상태 변경 실패했을 때 처리
 */
@Component
@Slf4j
public class DecreaseStockRollbackHandler implements KafkaMessageHandler {

    private final ProductRepository productRepository;

    private final KafkaMessageParser parser;

    @Autowired
    public DecreaseStockRollbackHandler(
        ProductRepository productRepository,
        KafkaMessageParser parser) {
        this.productRepository = productRepository;
        this.parser = parser;
    }

    @Override
    public void handle(String message) {
        KafkaOrderDto orderDto = parser.readMessageValue(message,
            new TypeReference<KafkaOrderDto>() {});

        Map<Long, Integer> productStockMap = orderDto.getProductStockMap();
        List<Long> productIds = new ArrayList<>(productStockMap.keySet());
        List<Product> products = productRepository.findByIdIn(productIds);

        products.forEach(product -> {
            product.increaseStock(productStockMap.get(product.getId())); // get QTY.
        });

        productRepository.saveAll(products);

    }


}
