package com.abhi.ecom.services.customer.wishlist;

import java.util.List;

import com.abhi.ecom.dto.WishlistDto;

public interface WishlistService {
	WishlistDto addProductToWishlist(WishlistDto wishlistDto);
	boolean deleteWishlist(Long id);


	List<WishlistDto> getWishlistByUserId(Long userId);
}
