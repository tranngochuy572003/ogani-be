package com.example.controller;

import com.example.api.ApiResponse;
import com.example.dto.BillDto;
import com.example.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.common.MessageConstant.ORDER_CONFIRM_SUCCESS;
import static com.example.common.MessageConstant.ORDER_SUCCESS;


@RestController
@RequestMapping("/api/v1/bills")
@Tag(name="Bill Controller")
public class BillController {
    @Autowired
    private BillService billService;

    @Operation(summary = "Order by userId")
    @PreAuthorize("#userId==authentication.principal.id and hasAuthority('ROLE_CUSTOMER')")
    @PostMapping ("/order/{userId}")
    public ResponseEntity<ApiResponse> order(@PathVariable String userId) {
        billService.order(userId);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value());
        response.setMessage(ORDER_SUCCESS);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Confirm order")
    @PostMapping("/confirmOrder/{billId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> confirmOrder(@PathVariable String billId) {
        billService.confirmOrder(billId);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value());
        response.setMessage(ORDER_CONFIRM_SUCCESS);
        return ResponseEntity.ok(response);
    }

    //???
    @Operation(summary = "Get bill by billId")
    @PreAuthorize("(@billServiceImpl.isAuthorizedToGetBill(#billId,authentication.principal.id) and hasAuthority('ROLE_CUSTOMER')) or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/getBillById/{billId}")
    public ResponseEntity<ApiResponse> getBillById(@PathVariable String billId) {
        BillDto billDto = billService.getBillById(billId);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value());
        response.setData(billDto);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Get list bill by userId")
    @PreAuthorize("(#userId==authentication.principal.id and hasAuthority('ROLE_CUSTOMER')) or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/getBillByUserId/{userId}")
    public ResponseEntity<ApiResponse> getBillByUserId(@PathVariable String userId) {
        List<BillDto> billDtoList = billService.getBillByUserId(userId);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value());
        response.setData(billDtoList);
        return ResponseEntity.ok(response);
    }
}
