package shop.kokodo.productservice.feign.service.interfaces;

import java.util.List;
import shop.kokodo.productservice.feign.response.FeignResponse;

public interface ProductFeignService {

    FeignResponse.Price getProductPrice(Long productId);
    List<FeignResponse.Price> getProductPrices(List<Long> productId);

    List<FeignResponse.ProductOfCart> getCartProducts(List<Long> productIds);

    FeignResponse.Stock getProductStock(Long productId);

    List<FeignResponse.ProductOfOrderSheet> getOrderSheetProducts(List<Long> productIds);
}
