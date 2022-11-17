package shop.kokodo.productservice.messagequeue.message;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KafkaOrderDto {

    private Long orderId;
    private Map<Long, Integer> productStockMap;
    private KafkaCouponNameDto kafkaCouponNameDto;

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KafkaCouponNameDto {

        long memberId;
        List<Long> fixCouponIdList;
        List<String> rateCouponNames;

    }
}
