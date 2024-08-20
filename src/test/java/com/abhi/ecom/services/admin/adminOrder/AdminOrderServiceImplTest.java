package com.abhi.ecom.services.admin.adminOrder;

import com.abhi.ecom.dto.AnalyticsResponse;
import com.abhi.ecom.dto.OrderDto;
import com.abhi.ecom.enums.OrderStatus;
import com.abhi.ecom.enums.UserRole;
import com.abhi.ecom.model.*;
import com.abhi.ecom.model.Order;
import com.abhi.ecom.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@Slf4j
class AdminOrderServiceImplTest {

    AutoCloseable autoCloseable;
    @Mock
    OrderRepository orderRepository;

    private AdminOrderService adminOrderService;

    private User user;
    private Category category;
    private Product product;
    private Coupon coupon;
    private List<CartItems> cartItems;

    @BeforeEach
    void setUp() throws IOException {
        autoCloseable = MockitoAnnotations.openMocks(this);
        adminOrderService = new AdminOrderServiceImpl(orderRepository);
        initializeTestEntities();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    private void initializeTestEntities() throws IOException {
        user = createUser();
        category = createCategory();
        product = createProduct();
        cartItems = createCartItems();
        coupon = createCoupon();
    }

    private User createUser() {
        return User.builder()
                .email("demoEmail@mail.com")
                .name("demoName")
                .password(new BCryptPasswordEncoder().encode("demoPassword"))
                .role(UserRole.CUSTOMER)
                .build();
    }

    private Category createCategory() {
        return Category.builder()
                .name("demoCategory")
                .description("demoDescription")
                .build();
    }

    private Product createProduct() throws IOException {
        MultipartFile mockMultipartFile = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", "test image".getBytes());
        return Product.builder()
                .name("demoName")
                .price(200L)
                .img(mockMultipartFile.getBytes())
                .category(category)
                .description("demoDescription")
                .build();
    }

    private List<CartItems> createCartItems() {
        CartItems cartItem = CartItems.builder()
                .product(product)
                .user(user)
                .quantity(2L)
                .build();
        return new ArrayList<>(Collections.singletonList(cartItem));
    }

    private Coupon createCoupon() {
        return Coupon.builder()
                .name("demoName")
                .code("FLAT50")
                .discount(50L)
                .expirationDate(new Date())
                .build();
    }

    private com.abhi.ecom.model.Order createMockOrder(Long id, Date date, OrderStatus status) {
        return com.abhi.ecom.model.Order.builder()
                .id(id)
                .orderDescription("demoDescription")
                .date(date)
                .amount(500L)
                .address("demoAddress")
                .payment("Done")
                .orderStatus(status)
                .totalAmount(500L)
                .discount(50L)
                .trackingId(UUID.randomUUID())
                .user(user)
                .coupon(coupon)
                .cartItems(cartItems)
                .build();
    }

    @Test
    void getAllPlacedOrders() {
        com.abhi.ecom.model.Order savedOrder = createMockOrder(1L, new Date(), OrderStatus.Placed);
        List<com.abhi.ecom.model.Order> orderList = Collections.singletonList(savedOrder);

        when(orderRepository.findAllByOrderStatusIn(any())).thenReturn(orderList);

        List<OrderDto> placedOrders = adminOrderService.getAllPlacedOrders();
        assertEquals(orderList.size(), placedOrders.size());
        Assertions.assertEquals(orderList.get(0).getOrderDto(), placedOrders.get(0));
    }

    @Test
    void changeOrderStatusToShipped() {
        com.abhi.ecom.model.Order order = createMockOrder(1L, new Date(), OrderStatus.Placed);
        com.abhi.ecom.model.Order savedOrder = createMockOrder(1L, new Date(), OrderStatus.Shipped);

        when(orderRepository.findById(any())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(com.abhi.ecom.model.Order.class))).thenReturn(savedOrder);

        OrderDto result = adminOrderService.changeOrderStatus(1L, "Shipped");
        assertNotNull(result);
        Assertions.assertEquals(OrderStatus.Shipped, result.getOrderStatus());
    }

    @Test
    void changeOrderStatusToDelivered() {
        com.abhi.ecom.model.Order order = createMockOrder(1L, new Date(), OrderStatus.Placed);
        com.abhi.ecom.model.Order savedOrder = createMockOrder(1L, new Date(), OrderStatus.Delivered);

        when(orderRepository.findById(any())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(com.abhi.ecom.model.Order.class))).thenReturn(savedOrder);

        OrderDto result = adminOrderService.changeOrderStatus(1L, "Delivered");
        assertNotNull(result);
        Assertions.assertEquals(OrderStatus.Delivered, result.getOrderStatus());
    }

    @Test
    void changeOrderStatusOrderNotFound() {
        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        OrderDto result = adminOrderService.changeOrderStatus(1L, "Shipped");
        assertNull(result);
    }

    @Test
    void calculateAnalytics() {
        // Used Current month and year because implementation uses LocalDate.now()
        Date[] currentMonthRange = getDateRangeForMonth(Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.YEAR));
        Date[] previousMonthRange = getDateRangeForMonth(Calendar.getInstance().get(Calendar.MONTH)-1, Calendar.getInstance().get(Calendar.YEAR));

        com.abhi.ecom.model.Order deliveredOrderCurrentMonth = createMockOrder(1L, currentMonthRange[0], OrderStatus.Delivered);
        com.abhi.ecom.model.Order deliveredOrderPreviousMonth = createMockOrder(2L, previousMonthRange[0], OrderStatus.Delivered);
        List<com.abhi.ecom.model.Order> currentMonthOrders = Collections.singletonList(deliveredOrderCurrentMonth);
        List<com.abhi.ecom.model.Order> previousMonthOrders = Collections.singletonList(deliveredOrderPreviousMonth);

        when(orderRepository.findByDateBetweenAndOrderStatus(eq(currentMonthRange[0]), eq(currentMonthRange[1]), eq(OrderStatus.Delivered))).thenReturn(currentMonthOrders);
        when(orderRepository.findByDateBetweenAndOrderStatus(eq(previousMonthRange[0]), eq(previousMonthRange[1]), eq(OrderStatus.Delivered))).thenReturn(previousMonthOrders);
        when(orderRepository.countByOrderStatus(OrderStatus.Placed)).thenReturn(0L);
        when(orderRepository.countByOrderStatus(OrderStatus.Delivered)).thenReturn(2L); // both orders are delivered
        when(orderRepository.countByOrderStatus(OrderStatus.Shipped)).thenReturn(1L); // one order is shipped

        AnalyticsResponse response = adminOrderService.calculateAnalytics();

        assertEquals(2L, response.getDelivered());
        assertEquals(0L, response.getPlaced());
        assertEquals(1L, response.getCurrentMonthOrders());
        assertEquals(1L, response.getPreviousMonthOrders());
        assertEquals(500L, response.getCurrentMonthEarnings());
        assertEquals(500L, response.getPreviousMonthEarnings());
        assertEquals(currentMonthOrders.get(0).getAmount(), response.getCurrentMonthEarnings());
        assertEquals(previousMonthOrders.get(0).getAmount(), response.getPreviousMonthEarnings());
    }


    @Test
    void getTotalOrdersForMonths() {
        int year = 2024;
        Date[] dateRange = getDateRangeForMonth(Calendar.MARCH, year);

        com.abhi.ecom.model.Order orderInMarch = createMockOrder(1L, new Date(dateRange[0].getTime() + (24 * 3600 * 4 * 1000)), OrderStatus.Delivered);
        List<com.abhi.ecom.model.Order> orders = Collections.singletonList(orderInMarch);

        when(orderRepository.findByDateBetweenAndOrderStatus(dateRange[0], dateRange[1], OrderStatus.Delivered)).thenReturn(orders);

        long totalOrders = adminOrderService.getTotalOrdersForMonths(Calendar.MARCH + 1, year);
        assertEquals(1L, totalOrders, "Should return 1 order for given month");
    }

    @Test
    void getTotalEarningsForMonth() {
        int year = 2024;
        Date[] dateRange = getDateRangeForMonth(Calendar.MARCH, year);

        com.abhi.ecom.model.Order orderInMarch = createMockOrder(1L, new Date(dateRange[0].getTime() + (24 * 3600 * 4 * 1000)), OrderStatus.Delivered);
        List<Order> orders = Collections.singletonList(orderInMarch);

        when(orderRepository.findByDateBetweenAndOrderStatus(dateRange[0], dateRange[1], OrderStatus.Delivered)).thenReturn(orders);

        long totalAmount = adminOrderService.getTotalEarningsForMonth(Calendar.MARCH + 1, year);
        assertEquals(500L, totalAmount, "Should return 500 for given month");
    }

    private Date[] getDateRangeForMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month); // Target month
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfMonth = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endOfMonth = calendar.getTime();

        return new Date[]{startOfMonth, endOfMonth};
    }
}
