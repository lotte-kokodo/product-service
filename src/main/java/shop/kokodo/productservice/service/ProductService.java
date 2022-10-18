package shop.kokodo.productservice.service;

import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.entity.Product;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<ProductDto> findProductListById(List<Long> productList);
    public List<Long> getProductSellerId(List<Long> productId);

    public List<ProductDto> findBySellerId(Long sellerId);
}