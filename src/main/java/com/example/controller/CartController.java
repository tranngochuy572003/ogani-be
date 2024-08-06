package com.example.controller;

import com.example.api.ApiResponse;
import com.example.dto.CartDetailDto;
import com.example.dto.CartDto;
import com.example.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.common.MessageConstant.ITEM_CREATED_SUCCESS;
import static com.example.common.MessageConstant.ITEM_UPDATED_SUCCESS;

@RestController
@RequestMapping("/api/v1/carts")
@Tag(name="Cart Controller")
public class CartController {
    @Autowired
    private CartService cartService;
    @Operation(summary = "Create cart by userId")
    @PreAuthorize("#userId==authentication.principal.id and hasAuthority('ROLE_CUSTOMER')")
    @PostMapping("/createCart/{userId}")
    public ResponseEntity<ApiResponse> createCart(@PathVariable String userId, @RequestBody List<CartDetailDto> cartDetailDto) {
        cartService.createCart(userId,cartDetailDto);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value());
        response.setMessage(ITEM_CREATED_SUCCESS);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update cart by cartId")
    @PreAuthorize("@userServiceImpl.isAuthorizedForCart(#cartId,authentication.principal.id) and hasAuthority('ROLE_CUSTOMER')")
    @PatchMapping  ("/updateCart/{cartId}")
    public ResponseEntity<ApiResponse> updateCart(@PathVariable String cartId, @RequestBody List<CartDetailDto> cartDetailDto) {
        cartService.updateCart(cartId,cartDetailDto);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value());
        response.setMessage(ITEM_UPDATED_SUCCESS);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get cart by userId")
    @PreAuthorize("#userId==authentication.principal.id and hasAuthority('ROLE_CUSTOMER')")
    @GetMapping("/getByUserId/{userId}")
    public ResponseEntity<ApiResponse> getByUserId(@PathVariable String userId) {
        CartDto cartDto = cartService.getByUserId(userId);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value());
        response.setData(cartDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get cart by cartId")
    @PreAuthorize("@userServiceImpl.isAuthorizedForCart(#cartId,authentication.principal.id) and hasAuthority('ROLE_CUSTOMER')")
    @GetMapping("/getByCartId/{cartId}")
    public ResponseEntity<ApiResponse> getByCartId(@PathVariable String cartId) {
        CartDto cartDto = cartService.getByCartId(cartId);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value());
        response.setData(cartDto);
        return ResponseEntity.ok(response);
    }
}
