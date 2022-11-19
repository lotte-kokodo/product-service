package shop.kokodo.productservice.feign.service.interfaces;

import java.util.List;
import java.util.Map;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.dto.ProductFeignDto;
import shop.kokodo.productservice.feign.request.OrderCountRequestDto;
import shop.kokodo.productservice.feign.response.ProductIdResponseDto;
import shop.kokodo.productservice.feign.response.OrderProductDto;
import shop.kokodo.productservice.feign.response.CartProductDto;
import shop.kokodo.productservice.feign.response.ProductStockDto;

public interface ProductFeignService {

    OrderProductDto getSingleOrderProduct(Long productId);
    Map<Long, OrderProductDto> getCartOrderProducts(List<Long> productId);

    List<CartProductDto> getOrderProducts(List<Long> productIds);

    ProductStockDto getProductStock(Long productId);

    List<ProductDto> findProductListById(List<Long> productList);

    Map<Long, ProductFeignDto> findProductListByIdMap(List<Long> productIdList);

    ProductIdResponseDto getSellerOrderProductCount(OrderCountRequestDto orderCountRequestDto);

    List<Long> getSellerProductIds(Long sellerId);

}
