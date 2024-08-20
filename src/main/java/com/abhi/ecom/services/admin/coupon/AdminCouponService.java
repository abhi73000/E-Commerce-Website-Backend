package com.abhi.ecom.services.admin.coupon;

import java.util.List;

import com.abhi.ecom.model.Coupon;

public interface AdminCouponService {
	Coupon createCoupon(Coupon coupon);
	List<Coupon> getAllCoupon();
}
