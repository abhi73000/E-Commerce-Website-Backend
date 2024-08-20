package com.abhi.ecom.controller.customer;

import java.util.List;

import com.abhi.ecom.dto.WishlistDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.abhi.ecom.services.customer.wishlist.WishlistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
@Slf4j
public class WishlistController {
	private final WishlistService wishlistService;

	@PostMapping("/wishlist")
	public ResponseEntity<?> addProductToWishlist(@RequestBody WishlistDto wishlistDto) {
		log.info("Received request to add product to wishlist for user with ID: {}", wishlistDto.getUserId());
		WishlistDto postedWishlistDto = wishlistService.addProductToWishlist(wishlistDto);
		if (postedWishlistDto == null) {
			log.warn("Failed to add product to wishlist for user with ID: {}", wishlistDto.getUserId());
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Something went wrong");
		}
		log.info("Product added to wishlist successfully for user with ID: {}", wishlistDto.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(postedWishlistDto);
	}

	@GetMapping("/wishlist/{userId}")
	public ResponseEntity<List<WishlistDto>> getWishlistByUserId(@PathVariable Long userId) {
		log.info("Received request to get wishlist for user with ID: {}", userId);
		List<WishlistDto> wishlistDtos = wishlistService.getWishlistByUserId(userId);
		return ResponseEntity.ok(wishlistDtos);
	}

	@DeleteMapping("/wishlist/{wishlistId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long wishlistId) {
		log.info("Received request to delete product with ID: {}", wishlistId);
		boolean deleted = wishlistService.deleteWishlist(wishlistId);
		if (deleted) {
			log.info("Product with ID: {} deleted successfully", wishlistId);
			return ResponseEntity.noContent().build();
		} else {
			log.warn("Product with ID: {} not found", wishlistId);
			return ResponseEntity.notFound().build();
		}
	}
}
