package com.abhi.ecom.services.admin.faq;

import com.abhi.ecom.dto.FAQDto;

public interface FAQService {
	FAQDto postFAQ(Long productId, FAQDto faqDto);
}
