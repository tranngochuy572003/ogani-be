package com.example.controller;

import com.example.api.ApiResponse;
import com.example.dto.CartDetailDto;
import com.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.common.MessageConstant.ITEM_CREATED_SUCCESS;
@RestController
@RequestMapping("/api/v1/carts")
public class CartController {
    @Autowired
    private CartService cartService;
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PostMapping("/createCart/{userId}")
    public ResponseEntity<ApiResponse> createCart(@PathVariable String userId, @RequestBody List<CartDetailDto> cartDetailDto) {
        cartService.createCart(userId,cartDetailDto);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value());
        response.setMessage(ITEM_CREATED_SUCCESS);
        return ResponseEntity.ok(response);
    }
}
