package shop.kokodo.productservice.messagequeue.strategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.exception.ExceptionMessage;
import shop.kokodo.productservice.messagequeue.KafkaMessageType;
import shop.kokodo.productservice.messagequeue.dto.KafkaDto;
import shop.kokodo.productservice.messagequeue.dto.KafkaDto.UpdateStock;
import shop.kokodo.productservice.repository.ProductRepository;

/**
 * '주문취소' 시 상품 재고 증가
 */
@Component
@Slf4j
public class IncreaseStockHandler implements KafkaMessageHandler {

    private final ProductRepository productRepository;

    @Autowired
    public IncreaseStockHandler(
        ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void handle(Map<Object, Object> map) {

        KafkaMessageType type = (KafkaMessageType) map.get("type");

        switch (type) {
            case ORDER_SINGLE_PRODUCT:
                orderSingleProduct(map);
                break;
            case ORDER_CART_PRODUCT:
                orderCartProduct(map);
                break;
        }

    }

    private void orderSingleProduct(Map<Object, Object> map) {
        Long productId = (Long) map.get("productId");

        Optional<Product> findProduct = productRepository.findById((Long) map.get("productId"));
        if (findProduct.isEmpty()) {
            log.error("[IncreaseStockHandler] Product(id = '{}') not founded.", productId);
            throw new IllegalArgumentException(ExceptionMessage.PRODUCT_NOT_FOUND);
        }

        Product product = findProduct.get();
        product.increaseStock((Integer) map.get("qty"));

        productRepository.save(product);
    }

    private void orderCartProduct(Map<Object, Object> map) {
        List<Long> productIds = map.keySet().stream()
            .map(obj -> (Long) obj)
            .collect(Collectors.toList());
        List<Product> products = productRepository.findByIdIn((List<Long>) productIds);

        products.forEach(product -> {
            product.decreaseStock((Integer) map.get(product.getId()));
        });

        productRepository.saveAll(products);
    }
}
