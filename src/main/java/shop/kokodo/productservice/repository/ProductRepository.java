package shop.kokodo.productservice.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.kokodo.productservice.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    /* 카테고리별 상품 조회 */
    @Query("select p from Product p join p.category c where c.id = :categoryId" )
    Page<Product> findProductByCategory(@Param("categoryId") long categoryId, Pageable pageable);

    /* ======= Main 화면 ======= */
    /* new 상품 [당월 및 전월에 발매된 상품] */
    @Query(value = "select p from Product p " +
            "where substring(p.createdDate,6,2) in (:month, :lastMonth) " +
            "order by p.createdDate desc")
    Page<Product> findProductByNew(@Param("month") String month,@Param("lastMonth") String lastMonth, Pageable pageable);

    /* sale 상품 [다음달 or 유통기한이 안 지난 이번달 상품] */
    @Query(value = "select p from Product p " +
            "where substring(p.deadline,6,2) = :nextMonth or " +
            "( substring(p.deadline,6,2) = :month AND substring(p.deadline,9,2) > :day) " +
            "ORDER BY p.deadline DESC")
    Page<Product> findProductBySale(@Param("nextMonth") String nextMonth,
                                    @Param("month") String month,
                                    @Param("day") String day,
                                    Pageable pageable);

    /* 각 MD별 상품 */
    @Query("select p from Product p group by p.sellerId")
    Page<Product> findProductBySeller(Pageable pageable);

    /* ======= 전체 상품 검색 ======= */
    @Query("select p from Product p where p.displayName like concat('%',:productDisplayName,'%')")
    Page<Product> findProductByTotalSearch(@Param("productDisplayName") String productDisplayName, Pageable pageable);

    /* feign Client */
    @Query("select p.sellerId from Product p where p.id = :pId")
    Long findSellerIdByProductId(Long pId);

    @Query("select p from Product p where p.stock < 10 and p.sellerId = :sellerId")
    Page<Product> findByProductStockLack(@Param("sellerId") long sellerId, Pageable pageable);

    List<Product> findBySellerId(Long sellerId);

    /**
     * @param <T> 조회할 필드 인터페이스 클래스 타입
     * @return
     */
    <T> List<T> findByIdIn(List<Long> ids, Class<T> type);

    List<Product> findByIdIn(List<Long> productIds);

    List<Product> findBySellerIdAndNameContains(Long sellerId, String productName);

}
