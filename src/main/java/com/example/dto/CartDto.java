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
    private List<CartDetailResponse> cartDetail;


    public CartDto(String cartId, String userId, Long totalPrice, List<CartDetailResponse> cartDetail) {
        this.cartId = cartId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.cartDetail = cartDetail;
    }


    @Data
    @NoArgsConstructor
    public static class CartDetailResponse {
        private String productId;
        private String name;
        private List<String> imageURL;
        private Long quantity;
        private Long price;
        private Boolean isChosen;


        public CartDetailResponse(String productId, String name, List<String> imageURL, Long quantity, Long price, Boolean isChosen) {
            this.productId = productId;
            this.name = name;
            this.imageURL = imageURL;
            this.quantity = quantity;
            this.price = price;
            this.isChosen = isChosen;
        }
    }
}
