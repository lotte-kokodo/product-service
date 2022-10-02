package shop.kokodo.productservice.service.interfaces;

import java.util.List;
import shop.kokodo.productservice.dto.feign.FeignResponse;

public interface ProductFeignService {

    FeignResponse.Price getProductPrice(Long productId);
    List<FeignResponse.Price> getProductPrices(List<Long> productId);

}
