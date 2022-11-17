package shop.kokodo.productservice.feign.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.dto.ProductFeignDto;
import shop.kokodo.productservice.feign.response.OrderProductDto;
import shop.kokodo.productservice.feign.response.CartProductDto;
import shop.kokodo.productservice.feign.response.ProductStockDto;
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
    @GetMapping("/singleOrderProduct")
    public ResponseEntity<OrderProductDto> getSingleProductOrder(@RequestParam Long productId) {
        OrderProductDto orderProductDto = productFeignService.getSingleOrderProduct(productId);
        return ResponseEntity.ok(orderProductDto);
    }

    // [주문 등록] 장바구니 상품 가격 조회
    @GetMapping("/cartOrderProduct")
    public ResponseEntity<Map<Long, OrderProductDto>> getProductsPrice(@RequestParam List<Long> productIds) {
        Map<Long, OrderProductDto> orderProductDtoMap = productFeignService.getCartOrderProducts(productIds);
        return ResponseEntity.ok(orderProductDtoMap);
    }

    // [주문관련 상품 조회] 장바구니 상품 조회
    @GetMapping("/cart")
    public ResponseEntity<Map<Long, CartProductDto>> getOrderProducts(@RequestParam List<Long> productIds) {

        List<CartProductDto> productOfOrders = productFeignService.getOrderProducts(productIds);
        Map<Long, CartProductDto> productOfCartDtoMap = productOfOrders.stream()
            .collect(Collectors.toMap(CartProductDto::getId, Function.identity()));
        return ResponseEntity.ok(productOfCartDtoMap);
    }

    // [장바구니 상품 수량 변경] 장바구니 상품 재고 조회
    @GetMapping("/stock/{productId}")
    public ResponseEntity<ProductStockDto> getProductStock(@PathVariable Long productId) {
        ProductStockDto productStockDto = productFeignService.getProductStock(productId);
        return ResponseEntity.ok(productStockDto);
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
    @GetMapping("/seller/{sellerId}/todayOrderCount")
    public ResponseEntity<Long> getSellerOrderProductCount(@PathVariable Long sellerId, @RequestParam List<Long> productIds) {
        Long todayOrderCount = productFeignService.getSellerOrderProductCount(sellerId, productIds);
        return ResponseEntity.ok(todayOrderCount);
    }

    @GetMapping("/seller/{sellerId}/productId")
    public ResponseEntity<List<Long>> getSellerProductIds(@PathVariable Long sellerId) {
        List<Long> productIds = productFeignService.getSellerProductIds(sellerId);
        return ResponseEntity.ok(productIds);
    }
}
