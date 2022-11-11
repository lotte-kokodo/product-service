package shop.kokodo.productservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.kokodo.productservice.dto.PagingProductDto;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.entity.Product;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductCustomRepository {

    PagingProductDto findProduct(String productName, Integer status, LocalDateTime startDate,
                                 LocalDateTime endDate, Long sellerId, Pageable pageable);
}
