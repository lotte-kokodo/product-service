package shop.kokodo.productservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.exception.ExceptionMessage;
import shop.kokodo.productservice.repository.ProductRepository;

@Service
@Slf4j
public class KafkaConsumer {
    private final ProductRepository productRepository;

    @Autowired
    public KafkaConsumer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @KafkaListener(topics = "kokodo.product")
    public void updateQty(String message) {
        log.info("[KafkaConsumer] message: {}", message);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(message, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        Long productId = (Long) map.get("productId");
        Optional<Product> findProduct = productRepository.findById((Long) map.get("productId"));
        if (findProduct.isEmpty()) {
            log.error("[KafkaConsumer] Product(id = '{}') not founded.", productId);
            throw new IllegalArgumentException(ExceptionMessage.PRODUCT_NOT_FOUND);
        }

        Product product = findProduct.get();
        product.setStock((Integer) map.get("qty"));

        productRepository.save(product);

    }
}
