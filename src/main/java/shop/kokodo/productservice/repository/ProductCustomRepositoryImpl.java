package shop.kokodo.productservice.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.entity.QProduct;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    private static QProduct product=QProduct.product;

    public ProductCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    @Override
    public List<ProductDto> findProduct(String name, Integer status, LocalDateTime startDate
            , LocalDateTime endDate, Long sellerId, Pageable pageable) {


        product = QProduct.product;
        List<Tuple> results = jpaQueryFactory.select(product.id,product.category.name,product.price,
                        product.displayName, product.thumbnail,product.stock)
                .from(product)
                .where(eqName(name), eqStatus(status),eqDate(startDate, endDate),eqSellerId(sellerId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchAll().fetch();

        List<ProductDto> productDtoList = new ArrayList<>();

        results.stream().forEach(tuple -> productDtoList.add(
                ProductDto.builder()
                .id(tuple.get(0,Long.class))
                .categoryName(tuple.get(1,String.class))
                .price(tuple.get(2,Integer.class))
                .displayName(tuple.get(3,String.class))
                .thumbnail(tuple.get(4,String.class))
                .stock(tuple.get(5,Integer.class))
                        .build())
                );

        return productDtoList;

    }

    private BooleanExpression eqName(String name){
        if(StringUtils.isEmpty(name)) return null;

        return product.name.contains(name);
    }

    private BooleanExpression eqStatus(Integer status){
        if(isNull(status) || status==0) return null; // status가 오지 않거나 0이면 전체 조회 -> 조건 걸지 않는다
        else if(status==1) return product.stock.gt(1);
        else if(status==2) return product.stock.eq(0);
        else return null;
    }

    private BooleanExpression eqRegDate(LocalDateTime regDate){
        if(regDate==null) return null;

        return product.lastModifiedDate.eq(regDate);
    }

    private BooleanExpression eqDate(LocalDateTime startDate, LocalDateTime endDate){
        if(startDate==null && endDate==null) return null;
        else if(startDate!=null && endDate==null) return product.lastModifiedDate.goe(startDate);
        else if(startDate==null && endDate!=null) return product.lastModifiedDate.loe(endDate);
        else return product.lastModifiedDate.goe(startDate).and( product.lastModifiedDate.loe(endDate));
    }

    private BooleanExpression eqSellerId(Long sellerId){
        return product.sellerId.eq(sellerId);
    }
}
