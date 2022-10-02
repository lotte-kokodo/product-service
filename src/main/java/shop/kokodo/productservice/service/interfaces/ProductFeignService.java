package shop.kokodo.productservice.service.interfaces;

import shop.kokodo.productservice.dto.feign.ProductPriceResponse;

public interface ProductFeignService {

    ProductPriceResponse getProductUnitPrice(Long productId);

}
