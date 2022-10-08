package shop.kokodo.productservice.exception;

public class ExceptionMessage {

    public static final String NOT_ENOUGH_STOCK = "상품 재고 부족";
    public static final String PRODUCT_NOT_FOUND = "등록되지 않은 상품 아이디";

    /* 상품 재고 부족 메시지 생성 */
    // msg: 상품 재고 부족: product_id '상품아이디'
    public static String createProductNotEnoughStockMsg(Long productId) {
        return String.format(NOT_ENOUGH_STOCK + ": product_id '%d'", productId);
    }
}
