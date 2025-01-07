package com.example.ogani.controller;

import com.example.controller.BillController;
import com.example.service.impl.BillServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.example.common.MessageConstant.ORDER_CONFIRM_SUCCESS;
import static com.example.common.MessageConstant.ORDER_SUCCESS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BillControllerTest {
    @InjectMocks
    private BillController billController;
    private MockMvc mockMvc;
    @Mock
    private BillServiceImpl billService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(billController).build();
    }

    @Test
    void testConfirmOrder() throws Exception {
        billService.confirmOrder("billId");
        mockMvc.perform(post("/api/v1/bills/confirmOrder/{billId}", "billId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message").value(ORDER_CONFIRM_SUCCESS));
    }

    @Test
    void testOrderByUserId() throws Exception {
        billService.order("userId");
        mockMvc.perform(post("/api/v1/bills/order/{userId}", "userId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message").value(ORDER_SUCCESS));
    }

}
