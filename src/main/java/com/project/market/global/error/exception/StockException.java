package com.project.market.global.error.exception;

public class StockException extends RuntimeException {
    public StockException(int stockNumber) {
        super(ErrorCode.NOT_ENOUGH_STOCK.getMessage() + "(현재 재고 수량" + stockNumber + ")");
    }
}
