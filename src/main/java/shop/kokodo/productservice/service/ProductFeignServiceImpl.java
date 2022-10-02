package shop.kokodo.productservice.service;

import org.springframework.stereotype.Service;
import shop.kokodo.productservice.dto.feign.ProductPriceResponse;
import shop.kokodo.productservice.service.interfaces.ProductFeignService;

@Service
public class ProductFeignServiceImpl implements ProductFeignService {

    @Override
    public ProductPriceResponse getProductUnitPrice(Long productId) {
        return null;
    }
}
