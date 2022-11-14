package shop.kokodo.productservice.feign.service.interfaces;

import java.util.List;
import java.util.Map;
import shop.kokodo.productservice.feign.response.OrderProductDto;
import shop.kokodo.productservice.feign.response.CartProductDto;
import shop.kokodo.productservice.feign.response.ProductStockDto;
import shop.kokodo.productservice.feign.response.ProductThumbnailDto;

public interface ProductFeignService {

    OrderProductDto getSingleOrderProduct(Long productId);
    Map<Long, OrderProductDto> getCartOrderProducts(List<Long> productId);

    List<CartProductDto> getOrderProducts(List<Long> productIds);

    ProductStockDto getProductStock(Long productId);

    Map<Long, ProductThumbnailDto> findProductListById(List<Long> productList);

    Long getSellerOrderProductCount(Long sellerId, List<Long> productIds);

    List<Long> getSellerProductIds(Long sellerId);
}
