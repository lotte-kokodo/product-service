package shop.kokodo.productservice.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.kokodo.productservice.dto.feign.FeignResponse;
import shop.kokodo.productservice.repository.ProductFeignRepository;
import shop.kokodo.productservice.service.interfaces.ProductFeignService;

@Service
public class ProductFeignServiceImpl implements ProductFeignService {

    private final ProductFeignRepository productFeignRepository;

    @Autowired
    public ProductFeignServiceImpl(
        ProductFeignRepository productFeignRepository) {
        this.productFeignRepository = productFeignRepository;
    }

    @Override
    public FeignResponse.Price getProductPrice(Long productId) {

        return productFeignRepository.findById(productId, FeignResponse.Price.class);
    }

    @Override
    public List<FeignResponse.Price> getProductPrices(List<Long> ids) {
        return productFeignRepository.findByIdIn(ids, FeignResponse.Price.class);
    }
}
