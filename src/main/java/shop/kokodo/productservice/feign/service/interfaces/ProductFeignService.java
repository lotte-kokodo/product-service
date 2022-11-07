package shop.kokodo.productservice.feign.service.interfaces;

import java.util.List;
import java.util.Map;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.feign.response.FeignResponse;
import shop.kokodo.productservice.feign.response.FeignResponse.Price;
import shop.kokodo.productservice.feign.response.FeignResponse.ProductOfOrder;

public interface ProductFeignService {

    FeignResponse.Price getProductPrice(Long productId);
    Map<Long, Integer> getProductsPrice(List<Long> productId);

    List<ProductOfOrder> getOrderProducts(List<Long> productIds);

    FeignResponse.Stock getProductStock(Long productId);

    List<ProductDto> findProductListById(List<Long> productList);

}
