package com.abhi.ecom.services.customer.review;

import java.io.IOException;

import com.abhi.ecom.dto.OrderedProductsResponseDto;
import com.abhi.ecom.dto.ReviewDto;

public interface ReviewService {
	OrderedProductsResponseDto getOrderedProductsDetailsByOrderId(Long orderId);
	
	ReviewDto giveReview(ReviewDto reviewDto) throws IOException ;
}
