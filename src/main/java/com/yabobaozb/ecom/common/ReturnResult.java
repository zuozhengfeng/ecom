package com.yabobaozb.ecom.common;

import lombok.Data;

@Data
public class ReturnResult<T> {

    private int code = 0;

    private String message = "OK";

    private T data;
}
