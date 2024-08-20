package com.abhi.ecom.services.customer.wishlist;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.abhi.ecom.dto.WishlistDto;
import com.abhi.ecom.model.Product;
import com.abhi.ecom.model.User;
import com.abhi.ecom.model.Wishlist;
import com.abhi.ecom.repository.ProductRepository;
import com.abhi.ecom.repository.UserRepository;
import com.abhi.ecom.repository.WishlistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistServiceImpl implements WishlistService{
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final WishlistRepository wishlistRepository;

	public WishlistDto addProductToWishlist(WishlistDto wishlistDto) {
		Optional<Product> optionalProduct = productRepository.findById(wishlistDto.getProductId());
		Optional<User> optionalUser = userRepository.findById(wishlistDto.getUserId());

		if (optionalProduct.isPresent() && optionalUser.isPresent()) {
			Wishlist wishlist = new Wishlist();
			wishlist.setProduct(optionalProduct.get());
			wishlist.setUser(optionalUser.get());

			log.info("Product with ID {} added to wishlist for user with ID {}", wishlistDto.getProductId(), wishlistDto.getUserId());
			return wishlistRepository.save(wishlist).getWishlistDto();
		}
		log.error("Failed to add product to wishlist. Product or user not found.");
		return null;
	}

	@Override
	public boolean deleteWishlist(Long id) {
		return false;
	}

	public List<WishlistDto> getWishlistByUserId(Long userId){
		List<Wishlist> wishlist = wishlistRepository.findAllByUserId(userId);
		log.info("Retrieved wishlist for user with ID {}", userId);
		return wishlist.stream().map(Wishlist::getWishlistDto).collect(Collectors.toList());
	}
}
