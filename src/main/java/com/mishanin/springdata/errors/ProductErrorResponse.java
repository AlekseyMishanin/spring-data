package com.mishanin.springdata.errors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductErrorResponse {

    private int status;
    private String message;
    private long timestamp;
}
