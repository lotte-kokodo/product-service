package shop.kokodo.productservice.exception;

public class NoSellerServiceException extends RuntimeException{

    public NoSellerServiceException(){}

    public NoSellerServiceException(String msg){
        super(msg);
    }
}
