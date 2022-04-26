package com.kwiatowe_imperium.api.models;

import lombok.Data;

@Data
public class OrderItemRequest {

    private Long product;
    private Long quantity;

}
