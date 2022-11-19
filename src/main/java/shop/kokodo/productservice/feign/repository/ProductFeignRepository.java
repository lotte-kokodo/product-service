package shop.kokodo.productservice.feign.repository;

import java.util.List;

import java.util.Map;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shop.kokodo.productservice.entity.Product;

@Repository
public interface ProductFeignRepository extends CrudRepository<Product, Long> {

    <T> T findById(Long id, Class<T> type);

    <T> List<T> findByIdIn(List<Long> ids, Class<T> type);

    @Query(value="select p from Product p " +
        "where p.id in :productIdList ")
    List<Product> findProductListById(@Param("productIdList") List<Long> productIdList);

    @Query("SELECT p.id "
        + "FROM Product p "
        + "WHERE p.sellerId = :sellerId "
        + "AND p.id IN :productIds")
    List<Long> countByIdInAndSellerId(List<Long> productIds, Long sellerId);

    List<Product> findAllBySellerId(Long sellerId);
}
