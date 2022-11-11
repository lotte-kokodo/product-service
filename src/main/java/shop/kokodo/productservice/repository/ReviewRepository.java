package shop.kokodo.productservice.repository;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.kokodo.productservice.dto.MypageReviewDto;
import shop.kokodo.productservice.entity.Review;

import javax.persistence.Entity;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long>{

    @Query("select r from Review r where r.product.id = :productId order by r.id desc ")
    Page<Review> findByProductIdPaging(@Param("productId") long productId, Pageable pageable);

    @Query("select r from Review r where r.product.id = :productId ")
    List<Review> findByProductId(@Param("productId") long productId);

    @Query("select new shop.kokodo.productservice.dto.MypageReviewDto( r.id, r.createdDate , r.product.id, r.content, r.rating, r.memberId, p.displayName, p.thumbnail ) " +
            "from Review r join r.product p where r.memberId = :memberId")
    Page<MypageReviewDto> findByMemberId(@Param("memberId") long memberId, Pageable pageable);
}
