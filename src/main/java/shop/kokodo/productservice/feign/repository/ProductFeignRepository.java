package shop.kokodo.productservice.feign.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shop.kokodo.productservice.entity.Product;

@Repository
public interface ProductFeignRepository extends JpaRepository<Product, Long> {

    <T> T findById(Long id, Class<T> type);

    <T> List<T> findByIdIn(List<Long> ids, Class<T> type);

    @Query(value="SELECT p FROM Product p " +
        "WHERE p.id IN (:productIdList) ")
    List<Product> findProductListById(List<Long> productIdList);

    Long countByIdInAndSellerId(List<Long> productIds, Long sellerId);

    List<Product> findAllBySellerId(Long sellerId);
}
