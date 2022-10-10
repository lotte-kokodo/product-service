package shop.kokodo.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.kokodo.productservice.entity.Product;

import java.util.List;

@Repository
public interface ProductMainRepository extends JpaRepository<Product, Long> {

}
