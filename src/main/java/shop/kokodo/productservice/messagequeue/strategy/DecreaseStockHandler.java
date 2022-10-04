package shop.kokodo.productservice.messagequeue.strategy;

import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.exception.ExceptionMessage;
import shop.kokodo.productservice.repository.ProductRepository;

/**
 * '주문' 시 상품 재고 감소
 */
@Component
@Slf4j
public class DecreaseStockHandler implements KafkaMessageHandler {

    private final ProductRepository productRepository;

    public DecreaseStockHandler(
        ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void handle(Map<Object, Object> map) {
        Long productId = (Long) map.get("productId");
        Optional<Product> findProduct = productRepository.findById((Long) map.get("productId"));
        if (findProduct.isEmpty()) {
            log.error("[DecreaseStockHandler] Product(id = '{}') not founded.", productId);
            throw new IllegalArgumentException(ExceptionMessage.PRODUCT_NOT_FOUND);
        }

        Product product = findProduct.get();
        product.decreaseStock((Integer) map.get("qty"));

        productRepository.save(product);
    }
}
