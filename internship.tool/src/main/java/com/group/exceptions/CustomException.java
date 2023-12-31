package com.group.exceptions;

import org.springframework.http.HttpStatus;

//fiind subclasa a lui RuntimeException va avea toate atributele sale
//la care mai adaugam un HTTP status (CTRL+click pe HttpStatus pentru a vedea fiecare status)
//
public class CustomException extends RuntimeException {

    private final HttpStatus httpStatus;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public CustomException(HttpStatus httpStatus)
    {
        this(httpStatus,null,null);
    }

    public CustomException(HttpStatus httpStatus,String reason)
    {
        this(httpStatus,reason,null);
    }

    public CustomException(HttpStatus httpStatus,String reason,Throwable cause)
    {
        super(reason,cause);
        this.httpStatus=httpStatus;
    }

}