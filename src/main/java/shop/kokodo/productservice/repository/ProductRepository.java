package shop.kokodo.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.kokodo.productservice.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("select p from Product p join fetch p.category c where c.id = :categoryId" )
    List<Product> findProductByCategory(@Param("categoryId") long categoryId);

    @Query("select p from Product p where p.displayName like concat('%',:productDisplayName,'%')")
    List<Product> findProductByTotalSearch(@Param("productDisplayName") String productDisplayName);

    @Query("select p from Product p join fetch p.category c where c.id = :categoryId " +
            "and p.displayName like concat('%',:productDisplayName,'%')" )
    List<Product> findProductByCategorySearch(@Param("categoryId") long categoryId,@Param("productDisplayName") String productDisplayName);
}
