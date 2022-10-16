package shop.kokodo.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.entity.ProductDetail;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail,Long> {

    List<ProductDetail> findByProductId(long productId);
}
