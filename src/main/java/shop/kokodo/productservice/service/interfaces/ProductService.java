package shop.kokodo.productservice.service.interfaces;

import java.util.List;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.entity.Product;

public interface ProductService {

    public Product saveProduct(ProductDto productDto);
    public Product updateProduct(ProductDto productDto);
    public void deleteProduct(long productId);
    public Product findById(long id);
    public List<ProductDto> findAll();
    public List<ProductDto> findProductByCategory(long categoryId);
    public List<ProductDto> findProductByTotalSearch(String productDisplayName);
    public List<ProductDto> findProductByCategorySearch(long categoryId, String productDisplayName);

    public Product findProductDetail(long productId);

    public List<Product> getOrderProducts(List<Long> productIds);
}
