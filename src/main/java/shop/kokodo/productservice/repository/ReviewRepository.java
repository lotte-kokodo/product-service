package shop.kokodo.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import shop.kokodo.productservice.dto.MypageReviewDto;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.entity.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

    @Query("select r from Review r where r.product.id = :productId")
    public List<Review> findByProductId(long productId);

    @Query("select new shop.kokodo.productservice.dto.MypageReviewDto( r.id, r.createdDate , r.product.id, r.content, r.rating, r.memberId, p.displayName, p.thumbnail ) " +
            "from Review r join r.product p where r.memberId = :memberId")
    public List<MypageReviewDto> findByMemberId(@Param("memberId") long memberId);
}
