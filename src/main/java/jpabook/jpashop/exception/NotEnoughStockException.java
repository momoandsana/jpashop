package jpabook.jpashop.exception;

public class NotEnoughStockException extends RuntimeException
{
    // 모두 오버라이드, RuntimeException 함수들, 메시지들 넘겨주기 좋음
    public NotEnoughStockException() {
        super();
    }

    public NotEnoughStockException(String message) {
        super(message);
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }
}
