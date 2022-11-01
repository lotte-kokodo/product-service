package shop.kokodo.productservice.service.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.entity.Product;

public interface ProductService {

    public Product saveProduct(ProductDto productDto);
    public Product updateProduct(ProductDto productDto);
    public void deleteProduct(long productId);
    public Product findById(long id);
    public List<ProductDto> findAll();
    public List<ProductDto> findProductByCategory(long categoryId);
    public List<ProductDto> findProductByCategorySortingNew(long categoryId);
    public List<ProductDto> findProductBySaleSortingNew();
    public List<ProductDto> findProductBySellerSortingNew();
    public List<ProductDto> findProductByCategorySortingReview(long categoryId);
    public List<ProductDto> findProductBySaleSortingReview();
    public List<ProductDto> findProductBySellerSortingReview();
    public List<ProductDto> findProductByTotalSearch(String productDisplayName);
    public List<ProductDto> findProductByCategorySearch(long categoryId, String productDisplayName);

    public List<ProductDto> findProductByNew();
    public List<ProductDto> findProductBySale();
    public List<ProductDto> findProductBySeller();


    public Product findProductDetail(long productId);
    public List<ProductDto> findBy(String name, Integer status, LocalDateTime startDateTime, LocalDateTime endDateTime
    ,Long sellerId);

    public List<Long> getProductSellerId(List<Long> productId);

    public List<ProductDto> findBySellerId(Long sellerId);

    Optional<Product> findProductOpById(Long productId);
}
