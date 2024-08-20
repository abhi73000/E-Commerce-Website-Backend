package com.abhi.ecom.controller;

import com.abhi.ecom.dto.CartItemsDto;
import com.abhi.ecom.dto.OrderDto;
import com.abhi.ecom.enums.OrderStatus;
import com.abhi.ecom.filters.JwtRequestFilter;
import com.abhi.ecom.services.customer.cart.CartService;
import com.abhi.ecom.services.jwt.UserDetailsServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TrackingController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class TrackingControllerTest {

    @MockBean
    private JwtRequestFilter jwtRequestFilter;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CartService cartService;

    private OrderDto orderDto;

    @BeforeEach
    void setUp() {
        orderDto = OrderDto.builder()
                .id(1L)
                .orderDescription("Test order")
                .date(new Date())
                .amount(100L)
                .address("Test address")
                .payment("Paid")
                .orderStatus(OrderStatus.Pending)
                .totalAmount(100L)
                .discount(10L)
                .trackingId(UUID.randomUUID())
                .userName("Test User")
                .cartItems(Arrays.asList(
                        CartItemsDto.builder().id(1L).quantity(2L).build(),
                        CartItemsDto.builder().id(2L).quantity(3L).build()
                ))
                .couponName("Test Coupon")
                .couponCode("TESTCODE")
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void searchOrderByTrackingId() throws Exception {
        when(cartService.searchOrderByTrackingId(any())).thenReturn(orderDto);
        this.mockMvc.perform(get("/order/c0fa7bf7-5f9a-4c44-801f-32e2e0c4e81a")).andDo(print()).andExpect(status().isOk());
    }
}