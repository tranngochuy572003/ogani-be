package com.example.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class CartDto {
    private String cartId;
    private String userId;
    private Long totalPrice;
    private List<CartDetailInfoDto> cartDetail;


    public CartDto(String cartId, String userId, Long totalPrice, List<CartDetailInfoDto> cartDetail) {
        this.cartId = cartId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.cartDetail = cartDetail;
    }
}
