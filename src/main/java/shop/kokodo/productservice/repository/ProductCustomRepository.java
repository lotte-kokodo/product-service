package shop.kokodo.productservice.repository;

import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.entity.Product;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductCustomRepository {

    List<ProductDto> findProduct(String productName, Integer status, LocalDateTime startDate,
                                 LocalDateTime endDate, Long sellerId);
}
