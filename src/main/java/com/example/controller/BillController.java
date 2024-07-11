package com.example.controller;

import com.example.api.ApiResponse;
import com.example.dto.BillDto;
import com.example.dto.OrderDto;
import com.example.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.common.MessageConstant.ORDER_CONFIRM_SUCCESS;


@RestController
@RequestMapping("/api/v1/bills")
public class BillController {
    @Autowired
    private BillService billService;

    @GetMapping("/getBillById/{billId}")
    public ResponseEntity<ApiResponse> getBillById(@PathVariable String billId) {
        BillDto billDto = billService.getBillById(billId);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value());
        response.setData(billDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirmOrder/{userId}")
    public ResponseEntity<ApiResponse> getBillById(@RequestBody OrderDto orderDto ,@PathVariable String userId) {
        billService.confirmOrder(orderDto,userId);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value());
        response.setMessage(ORDER_CONFIRM_SUCCESS);
        return ResponseEntity.ok(response);
    }
}
