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
    private List<CartDetailDto> cartDetail;


    public CartDto(String cartId, String userId, Long totalPrice, List<CartDetailDto> cartDetail) {
        this.cartId = cartId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.cartDetail = cartDetail;
    }


    @Data
    @NoArgsConstructor
    public static class CartDetailDto {
        private String productId;
        private String name;
        private List<String> imageURL;
        private Long quantity;
        private Long price;
        private Boolean isChosen;


        public CartDetailDto(String productId, String name, List<String> imageURL, Long quantity, Long price, Boolean isChosen) {
            this.productId = productId;
            this.name = name;
            this.imageURL = imageURL;
            this.quantity = quantity;
            this.price = price;
            this.isChosen = isChosen;
        }
    }
}
