package shop.kokodo.productservice.feign.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.feign.repository.ProductFeignRepository;
import shop.kokodo.productservice.feign.response.OrderProductDto;
import shop.kokodo.productservice.feign.response.CartProductDto;
import shop.kokodo.productservice.feign.response.ProductStockDto;
import shop.kokodo.productservice.feign.response.ProductThumbnailDto;
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
    public OrderProductDto getSingleOrderProduct(Long productId) {

        return productFeignRepository.findById(productId, OrderProductDto.class);
    }

    @Override
    public Map<Long, OrderProductDto> getCartOrderProducts(List<Long> ids) {
        List<OrderProductDto> prices = productFeignRepository.findByIdIn(ids, OrderProductDto.class);
        return prices.stream().collect(Collectors.toMap(OrderProductDto::getId, Function.identity()));
    }

    @Override
    public List<CartProductDto> getOrderProducts(List<Long> productIds) {
        return productFeignRepository.findByIdIn(productIds, CartProductDto.class);
    }

    @Override
    public ProductStockDto getProductStock(Long productId) {
        return productFeignRepository.findById(productId, ProductStockDto.class);
    }


    @Override
    public Map<Long, ProductThumbnailDto> findProductListById(List<Long> productIdList) {
        List<ProductThumbnailDto> productThumbnailDtoList = productFeignRepository.findByIdIn(productIdList, ProductThumbnailDto.class);
        return productThumbnailDtoList.stream().collect(Collectors.toMap(ProductThumbnailDto::getId, Function.identity()));
    }

    @Override
    public Long getSellerOrderProductCount(Long sellerId, List<Long> productIds) {
        return productFeignRepository.countByIdInAndSellerId(productIds, sellerId);
    }
}
