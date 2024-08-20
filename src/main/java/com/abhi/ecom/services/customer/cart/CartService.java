package com.abhi.ecom.services.customer.cart;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.abhi.ecom.dto.AddProductInCartDto;
import com.abhi.ecom.dto.OrderDto;
import com.abhi.ecom.dto.PlaceOrderDto;
import com.abhi.ecom.model.User;
import org.springframework.http.ResponseEntity;

public interface CartService {
	ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto);
	Optional<User> getCustomerById(Long customerId);

	OrderDto getCartByUserId(Long userId);
	
	OrderDto applyCoupon(Long userId,String code);
	
	OrderDto increaseProductQuantity(AddProductInCartDto addProductInCartDto);
	
	OrderDto decreaseProductQuantity(AddProductInCartDto addProductInCartDto);
	
	OrderDto placedOrder(PlaceOrderDto placeOrderDto);
	
	List<OrderDto> getMyPlacedOrders(Long userId);
	
	OrderDto searchOrderByTrackingId(UUID trackingId);
}
