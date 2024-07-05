package com.example.mapper;

import com.example.dto.CartDetailDto;
import com.example.entity.CartDetail;

public class CartDetailMapper {
  public static CartDetailDto toDto(CartDetail cartDetail) {
    CartDetailDto cartDetailDto = new CartDetailDto();
    cartDetailDto.setChosen(cartDetail.isChosen());
    cartDetailDto.setQuantityProduct(cartDetail.getQuantityProduct());

    return cartDetailDto;
  }

  public static CartDetail toEntity(CartDetailDto cartDetailDto) {
    CartDetail cartDetail = new CartDetail();
    cartDetail.setChosen(cartDetailDto.isChosen());
    cartDetail.setQuantityProduct(cartDetailDto.getQuantityProduct());
    return cartDetail;
  }

  public static CartDetail toUpdateEntity(CartDetail cartDetail , CartDetailDto cartDetailDto) {
    cartDetail.setChosen(cartDetailDto.isChosen());
    cartDetail.setQuantityProduct(cartDetailDto.getQuantityProduct());
    return cartDetail;
  }
}
