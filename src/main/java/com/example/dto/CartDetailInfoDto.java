package com.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class CartDetailInfoDto {
    private String productId;
    private String name;
    private List<String> imageURL;
    private Long quantity;
    private Long price;
    private boolean isChosen;

    public CartDetailInfoDto(String productId, String name, List<String> imageURL, Long quantity, Long price, boolean isChosen) {
        this.productId = productId;
        this.name = name;
        this.imageURL = imageURL;
        this.quantity = quantity;
        this.price = price;
        this.isChosen = isChosen;
    }
}

