package shop.kokodo.productservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import shop.kokodo.productservice.dto.OrderSheetProductDto;
import org.springframework.web.bind.annotation.PathVariable;

import shop.kokodo.productservice.dto.PagingProductDto;
import shop.kokodo.productservice.dto.ProductAndProductDetailDto;
import shop.kokodo.productservice.dto.ProductDetailTemplateDto;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.dto.kafka.ProductAndDetailDto;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.feign.response.ProductThumbnailDto;

public interface ProductService {

    /* 상품삭제 */
    public void deleteProduct(long productId);

    /* 단일 상품, 전체 상품 조회 */
    public Optional<Product> findById(long id);
    public List<ProductDto> findAll();

    /* 카테고리 별 상품 */
    public Page<Product> findProductByCategory(long categoryId, int page);

    /* ===메인화면=== */
    /* 신상품 */
    public Page<Product> findProductByNew(int page);
    /* 타임 세일 상품 */
    public Page<Product> findProductBySale(int page);
    /* MD추천 */
    public Page<Product> findProductBySeller(int page);

    /* ===전체 상품 검색===*/
    public Page<Product> findProductByTotalSearch(String productDisplayName,int page);

    /* Feign Client*/
    public ProductAndProductDetailDto findProductDetail(long productId);

    PagingProductDto findBy(String name, Integer status, LocalDateTime startDateTime, LocalDateTime endDateTime
    , Long sellerId, int page);

    PagingProductDto findByProductStockLack(long sellerId, int page);


    public List<Long> getProductSellerId(List<Long> productId);
    public List<ProductDto> findBySellerId(Long sellerId);
    Optional<Product> findProductOpById(Long productId);

    public Map<Long, OrderSheetProductDto> getOrderProducts(List<Long> productIds);

    public void saveProductDetail(ProductAndDetailDto productAndDetailDto);
    public void saveProductTemplate(ProductDetailTemplateDto productDetailTemplateDto);

    public List<ProductDto> findProductDetailByName(String productName, Long sellerId);

    public long sellerProductCount(long sellerId);

}
