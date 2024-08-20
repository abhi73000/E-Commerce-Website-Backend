package com.abhi.ecom.services.customer;

import java.util.List;

import com.abhi.ecom.dto.ProductDetailDto;
import com.abhi.ecom.dto.ProductDto;

public interface CustomerProductService {
	List<ProductDto> getAllProducts();

	List<ProductDto> getAllProductsByName(String name);
	
	ProductDetailDto getProductDetailById(Long productId);

}
