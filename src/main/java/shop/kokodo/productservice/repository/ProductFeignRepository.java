package shop.kokodo.productservice.repository;

import feign.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shop.kokodo.productservice.dto.feign.ProductPriceResponse;
import shop.kokodo.productservice.entity.Product;

@Repository
public interface ProductFeignRepository extends CrudRepository<Product, Long> {

    @Query(value = "select product.id, product.price " +
    "from Product product " +
    "where product.id = :id")
    ProductPriceResponse getProductUnitPriceByProductId(@Param("id") Long id);

}
