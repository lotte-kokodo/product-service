package shop.kokodo.productservice.feign.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.kokodo.productservice.dto.ProductResponse;
import shop.kokodo.productservice.dto.ProductResponse.GetOrderProduct;
import shop.kokodo.productservice.dto.response.Response;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.feign.response.FeignResponse;
import shop.kokodo.productservice.feign.response.FeignResponse.ProductOfCart;
import shop.kokodo.productservice.feign.response.FeignResponse.Stock;
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
    public List<FeignResponse.Price> getProductsPrice(@RequestParam List<Long> productIds) {

        return productFeignService.getProductPrices(productIds);
    }

    // [장바구니 조회] 장바구니 상품 조회
    @GetMapping("/cart")
    public Map<Long, ProductOfCart> getCartProducts(@RequestParam List<Long> productIds) {

        List<ProductOfCart> productOfCarts = productFeignService.getCartProducts(productIds);

        return productOfCarts.stream()
            .collect(Collectors.toMap(ProductOfCart::getId, Function.identity()));
    }

    // [장바구니 상품 수량 변경] 장바구니 상품 재고 조회
    @GetMapping("/stock/{productId}")
    public FeignResponse.Stock getProductStock(@PathVariable Long productId) {
        return productFeignService.getProductStock(productId);
    }

    // [주문서 조회] 주문 상품 조회
    @GetMapping("/orderProducts")
    public List<FeignResponse.ProductOfOrderSheet> getOrderSheetProducts(@RequestParam List<Long> productIds) {
        return productFeignService.getOrderSheetProducts(productIds);
    }
}
