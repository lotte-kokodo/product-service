package shop.kokodo.productservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.kokodo.productservice.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    // 상품 리스트 조회
    List<Product> findByIdIn(List<Long> productIds);

    @Query("select p from Product p join fetch p.category c where c.id = :categoryId" )
    List<Product> findProductByCategory(@Param("categoryId") long categoryId);

    @Query("select p from Product p where p.displayName like concat('%',:productDisplayName,'%')")
    List<Product> findProductByTotalSearch(@Param("productDisplayName") String productDisplayName);

    /*
    new 상품 [당월에 발매된 상품]
     */
    @Query(value = "select * from product p " +
            "where substring(p.created_date,6,2) = date_format(now(), '%m') " +
            "order by p.created_date desc", nativeQuery = true)
    List<Product> findProductByNew();

    /*
    sale 상품 [다음달 or 유통기한이 안 지난 이번달 상품
     */
    @Query(value = "select * from product p  " +
            "where substring(p.deadline,6,2) = date_format(date_add(now(), interval + 1 MONTH), '%m') or " +
            "( substring(p.deadline,6,2) = date_format(now(), '%m') AND substring(p.deadline,8,2) < date_format(now(), '%d')) " +
            "ORDER BY p.deadline DESC", nativeQuery = true )
    List<Product> findProductBySale();

    /*
    각 MD별 상품
     */
    @Query("select p from Product p group by p.sellerId")
    List<Product> findProductBySeller();

    /*
    카테고리 별 리뷰 많은 순으로 정렬
     */
    @Query(value = "select p.* from product p inner join " +
            "( select r.product_id, count(r.product_id) as cnt from review r " +
            "group by product_id order by cnt desc ) " +
            "r on p.product_id = r.product_id where p.category_id = :categoryId " +
            "union select * from product p1 where p1.category_id = :categoryId ", nativeQuery = true)
    List<Product> findProductByCategorySortingReview(@Param("categoryId") long categoryId);

    /*
    세일 상품 리뷰 많은 순으로 정렬
     */
    @Query(value = "( select p.* from " +
            "(select * from product p " +
            "where substring(p.deadline,6,2) = date_format(date_add(now(), interval + 1 MONTH), '%m') or " +
            "( substring(p.deadline,6,2) = date_format(now(), '%m') AND substring(p.deadline,8,2) < date_format(now(), '%d')) ) p " +
            "inner join " +
            "( select r.product_id, count(r.product_id) as cnt from review r " +
            "group by product_id order by cnt desc ) " +
            "r on p.product_id = r.product_id ) " +
            "union " +
            "( select * from product p " +
            "where substring(p.deadline,6,2) = date_format(date_add(now(), interval + 1 MONTH), '%m') or " +
            "( substring(p.deadline,6,2) = date_format(now(), '%m') AND substring(p.deadline,8,2) < date_format(now(), '%d')) " +
            "ORDER BY p.deadline DESC ) ", nativeQuery = true)
    List<Product> findProductBySaleSortingReview();

    /*
    MD 추천 상품 리뷰 많은 순으로 정렬
     */
    @Query(value="select p.* from (select * from product group by seller_id) p inner join " +
            "( select r.product_id, count(r.product_id) as cnt from review r " +
            "group by product_id order by cnt desc ) " +
            "r on p.product_id = r.product_id " +
            "union select * from product group by seller_id ", nativeQuery = true)
    List<Product> findProductBySellerSortingReview();

    @Query("select p.sellerId from Product p where p.id = :pId")
    Long findSellerIdByProductId(Long pId);

    List<Product> findBySellerId(Long sellerId);
}
