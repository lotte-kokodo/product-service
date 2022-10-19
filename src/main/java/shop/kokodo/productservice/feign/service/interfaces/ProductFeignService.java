package shop.kokodo.productservice.feign.service.interfaces;

import java.util.List;
import shop.kokodo.productservice.feign.response.FeignResponse;
import shop.kokodo.productservice.feign.response.FeignResponse.ProductOfOrder;

public interface ProductFeignService {

    FeignResponse.Price getProductPrice(Long productId);
    List<FeignResponse.Price> getProductPrices(List<Long> productId);

    List<ProductOfOrder> getOrderProducts(List<Long> productIds);

    FeignResponse.Stock getProductStock(Long productId);
}
