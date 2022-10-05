package shop.kokodo.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.kokodo.productservice.dto.ProductInquireResponseDto;
import shop.kokodo.productservice.entity.ProductInquire;

import java.util.List;

@Repository
public interface ProductInquireRepository extends JpaRepository<ProductInquire,Long> {

    @Query("select p from ProductInquire p where p.product.id = :productId")
    public List<ProductInquire> findByProductId(long productId);

    @Query("select i from ProductInquire i " +
            "where i.product.id  in ( select p from Product p where sellerId = :sellerId ) " +
            "and i.answer is null ")
    public List<ProductInquire> findNotAnswerInquireBySellerId(long sellerId);

    @Query("select i from ProductInquire i " +
            "where i.product.id  in ( select p from Product p where sellerId = :sellerId ) " +
            "and i.answer is not null ")
    public List<ProductInquire> findAnswerInquireBySellerId(long sellerId);

}
