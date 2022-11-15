package shop.kokodo.productservice.feign.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.dto.ProductFeignDto;
import shop.kokodo.productservice.feign.response.FeignResponse;
import shop.kokodo.productservice.feign.response.FeignResponse.Price;
import shop.kokodo.productservice.feign.response.FeignResponse.ProductOfOrder;
import shop.kokodo.productservice.feign.service.interfaces.ProductFeignService;

@RestController
@RequestMapping("/products/feign")
public class ProductFeignController {

    private final ProductFeignService productFeignService;

    @Autowired
    public ProductFeignController(
        ProductFeignService productFeignService) {
        this.productFeignService = productFeignService;
    }

    // [주문 등록] 단일 상품 가격 조회
    @GetMapping("/unitPrice/{productId}")
    public FeignResponse.Price getProductPrice(@PathVariable Long productId) {

        return productFeignService.getProductPrice(productId);
    }

    // [주문 등록] 장바구니 상품 가격 조회
    @GetMapping("/unitPrice")
    public Map<Long, Integer> getProductsPrice(@RequestParam List<Long> productIds) {
        return productFeignService.getProductsPrice(productIds);
    }

    // [주문관련 상품 조회] 장바구니 상품 조회
    @GetMapping("/order")
    public Map<Long, ProductOfOrder> getOrderProducts(@RequestParam List<Long> productIds) {

        List<ProductOfOrder> productOfOrders = productFeignService.getOrderProducts(productIds);

        return productOfOrders.stream()
            .collect(Collectors.toMap(ProductOfOrder::getId, Function.identity()));
    }

    // [장바구니 상품 수량 변경] 장바구니 상품 재고 조회
    @GetMapping("/stock/{productId}")
    public FeignResponse.Stock getProductStock(@PathVariable Long productId) {
        return productFeignService.getProductStock(productId);
    }

    @GetMapping("/list")
    public List<ProductDto> findProductListById(@RequestParam List<Long> productIdList) {

        List<ProductDto> productList = productFeignService.findProductListById(productIdList);

        return productList;
    }

    @GetMapping("/list/map")
    public Map<Long, ProductFeignDto> findProductListByIdMap(@RequestParam List<Long> productIdList) {

        Map<Long, ProductFeignDto> productList = productFeignService.findProductListByIdMap(productIdList);

        return productList;
    }
}
