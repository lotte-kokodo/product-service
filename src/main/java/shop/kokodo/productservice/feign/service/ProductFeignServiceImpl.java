package shop.kokodo.productservice.feign.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.dto.ProductFeignDto;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.feign.repository.ProductFeignRepository;
import shop.kokodo.productservice.feign.response.OrderProductDto;
import shop.kokodo.productservice.feign.response.CartProductDto;
import shop.kokodo.productservice.feign.response.ProductStockDto;
import shop.kokodo.productservice.feign.response.ProductThumbnailDto;
import shop.kokodo.productservice.feign.service.interfaces.ProductFeignService;


@Slf4j
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
    public List<ProductDto> findProductListById(List<Long> productIdList) {

        List<Product> productList = productFeignRepository.findProductListById(productIdList);

        List<ProductDto> productDtoList = returnProductDtoList(productList);
        return productDtoList;
    }

    @Override
    public Long getSellerOrderProductCount(Long sellerId, List<Long> productIds) {
        return productFeignRepository.countByIdInAndSellerId(productIds, sellerId);
    }

    @Transactional(readOnly = true)
    @Override
    public Map<Long, ProductFeignDto> findProductListByIdMap(List<Long> productIdList) {
        List<Product> productList = productFeignRepository.findProductListById(productIdList);
        log.info("serviceProductList : " + productList);
        Map<Long, ProductFeignDto> productDtoList = returnProductDtoMap(productList);
        log.info("serviceProductDtoList : " + productDtoList);
        return productDtoList;
    }

    public List<ProductDto> returnProductDtoList (List<Product> productList) {
        List<ProductDto> productDtoList = new ArrayList<>();

        for (Product p : productList) {
            ProductDto productDto = new ProductDto(p.getId(), p.getCategory().getId(),
                    p.getName(), p.getPrice(), p.getDisplayName(),
                    p.getStock(), p.getDeadline(), p.getThumbnail(),
                    p.getSellerId(), p.getDeliveryFee());

            productDtoList.add(productDto);
        }

        return productDtoList;
    }

    @Override
    public List<Long> getSellerProductIds(Long sellerId) {
        List<Product> products = productFeignRepository.findAllBySellerId(sellerId);
        return products.stream().map(Product::getId).collect(Collectors.toList());
    }

    public Map<Long, ProductFeignDto> returnProductDtoMap (List<Product> productList) {

        Map<Long, ProductFeignDto> productDtoMap = new HashMap<>();

        for(Product product : productList) {
            log.info("serviceProduct : " + product);
            ProductFeignDto productDto = ProductFeignDto.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .displayName(product.getDisplayName())
                    .thumbnail(product.getThumbnail())
                    .build();

            productDtoMap.put(product.getId(), productDto);
        }
        log.info("serviceProductDtoMap : " + productDtoMap);
        return productDtoMap;
    }
}
