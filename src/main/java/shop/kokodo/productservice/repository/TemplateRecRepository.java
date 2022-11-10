package shop.kokodo.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.kokodo.productservice.entity.Review;
import shop.kokodo.productservice.entity.TemplateRec;

public interface TemplateRecRepository extends JpaRepository<TemplateRec,Long> {
}
