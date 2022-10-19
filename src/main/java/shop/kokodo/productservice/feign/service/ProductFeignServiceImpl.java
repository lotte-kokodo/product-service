package shop.kokodo.productservice.feign.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.kokodo.productservice.feign.repository.ProductFeignRepository;
import shop.kokodo.productservice.feign.response.FeignResponse;
import shop.kokodo.productservice.feign.response.FeignResponse.Price;
import shop.kokodo.productservice.feign.response.FeignResponse.ProductOfOrder;
import shop.kokodo.productservice.feign.response.FeignResponse.Stock;
import shop.kokodo.productservice.feign.service.interfaces.ProductFeignService;


@Service
public class ProductFeignServiceImpl implements ProductFeignService{

    private final ProductFeignRepository productFeignRepository;

    @Autowired
    public ProductFeignServiceImpl(
        ProductFeignRepository productFeignRepository) {
        this.productFeignRepository = productFeignRepository;
    }

    @Override
    public FeignResponse.Price getProductPrice(Long productId) {

        return productFeignRepository.findById(productId, Price.class);
    }

    @Override
    public List<FeignResponse.Price> getProductPrices(List<Long> ids) {
        return productFeignRepository.findByIdIn(ids, Price.class);
    }

    @Override
    public List<ProductOfOrder> getOrderProducts(List<Long> productIds) {
        return productFeignRepository.findByIdIn(productIds, ProductOfOrder.class);
    }

    @Override
    public FeignResponse.Stock getProductStock(Long productId) {
        return productFeignRepository.findById(productId, Stock.class);
    }

}
