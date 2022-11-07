package shop.kokodo.productservice.feign.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.entity.Product;
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
    public Map<Long, Integer> getProductsPrice(List<Long> ids) {
        List<FeignResponse.Price> prices = productFeignRepository.findByIdIn(ids, Price.class);
        return prices.stream().collect(Collectors.toMap(Price::getId, Price::getPrice));
    }

    @Override
    public List<ProductOfOrder> getOrderProducts(List<Long> productIds) {
        return productFeignRepository.findByIdIn(productIds, ProductOfOrder.class);
    }

    @Override
    public FeignResponse.Stock getProductStock(Long productId) {
        return productFeignRepository.findById(productId, Stock.class);
    }


    @Override
    public List<ProductDto> findProductListById(List<Long> productIdList) {

        List<Product> productList = productFeignRepository.findProductListById(productIdList);

        List<ProductDto> productDtoList = returnProductDtoList(productList);
        return productDtoList;
    }

    public List<ProductDto> returnProductDtoList (List<Product> productList) {
        List<ProductDto> productDtoList = new ArrayList<>();

        for(Product p : productList){
            ProductDto productDto = new ProductDto(p.getId(),p.getCategory().getId(),
                p.getName(),p.getPrice(),p.getDisplayName(),
                p.getStock(),p.getDeadline(),p.getThumbnail(),
                p.getSellerId(),p.getDeliveryFee());

            productDtoList.add(productDto);
        }

        return productDtoList;
    }
}
