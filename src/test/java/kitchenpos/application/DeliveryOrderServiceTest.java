package kitchenpos.application;

import kitchenpos.deliveryorder.application.DeliveryOrderService;
import kitchenpos.deliveryorder.domain.DeliveryOrder;
import kitchenpos.deliveryorder.domain.DeliveryOrderRepository;
import kitchenpos.deliveryorder.domain.OrderLineItem;
import kitchenpos.deliveryorder.domain.OrderStatus;
import kitchenpos.menu.domain.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static kitchenpos.Fixtures.*;
import static kitchenpos.testfixture.DeliveryOrderTestFixture.createOrderRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class DeliveryOrderServiceTest {

    private DeliveryOrderRepository deliveryOrderRepository;

    private MenuRepository menuRepository;

    private FakeKitchenridersClient kitchenridersClient;

    private DeliveryOrderService deliveryOrderService;

    @BeforeEach
    void setUp() {
        deliveryOrderRepository = new InMemoryDeliveryOrderRepository();
        menuRepository = new InMemoryMenuRepository();
        kitchenridersClient = new FakeKitchenridersClient();
        deliveryOrderService = new DeliveryOrderService(deliveryOrderRepository, menuRepository, kitchenridersClient);
    }

    @DisplayName("1개 이상의 등록된 메뉴로 배달 주문을 등록할 수 있다.")
    @Test
    void createDeliveryOrder() {
        final UUID menuId = menuRepository.save(menu(19_000L, true, menuProduct())).getId();
        final DeliveryOrder expected = createOrderRequest(
                OrderStatus.WAITING,
                LocalDateTime.now(),
                List.of(createOrderLineItemRequest(menuId, 19_000L, 3L)),
                "서울시 송파구 위례성대로 2"
                );
        final DeliveryOrder actual = deliveryOrderService.create(expected);
        assertThat(actual).isNotNull();
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getStatus()).isEqualTo(OrderStatus.WAITING),
                () -> assertThat(actual.getOrderLineItems()).hasSize(1),
                () -> assertThat(actual.getDeliveryAddress()).isEqualTo(expected.getDeliveryAddress())
        );
    }

    @DisplayName("주문 유형이 올바르지 않으면 등록할 수 없다.")
    @NullSource
    @ParameterizedTest
    void create() {
        final UUID menuId = menuRepository.save(menu(19_000L, true, menuProduct())).getId();
        final DeliveryOrder expected = createOrderRequest(
                OrderStatus.WAITING,
                LocalDateTime.now(),
                List.of(createOrderLineItemRequest(menuId, 19_000L, 3L)),
                "서울시 송파구 위례성대로 2"
        );
        assertThatThrownBy(() -> deliveryOrderService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("배달 주소가 올바르지 않으면 배달 주문을 등록할 수 없다.")
    @NullAndEmptySource
    @ParameterizedTest
    void create(final String deliveryAddress) {
        final UUID menuId = menuRepository.save(menu(19_000L, true, menuProduct())).getId();
        
        final DeliveryOrder expected = createOrderRequest(
                OrderStatus.WAITING,
                LocalDateTime.now(),
                List.of(createOrderLineItemRequest(menuId, 19_000L, 3L)),
                deliveryAddress
        );
        assertThatThrownBy(() -> deliveryOrderService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 접수한다.")
    @Test
    void accept() {
        final UUID orderId = deliveryOrderRepository.save(deliveryOrder(OrderStatus.WAITING, "서울시 송파구 위례성대로 2")).getId();
        final DeliveryOrder actual = deliveryOrderService.accept(orderId);
        assertThat(actual.getStatus()).isEqualTo(OrderStatus.ACCEPTED);
    }

    @DisplayName("접수 대기 중인 주문만 접수할 수 있다.")
    @EnumSource(value = OrderStatus.class, names = "WAITING", mode = EnumSource.Mode.EXCLUDE)
    @ParameterizedTest
    void accept(final OrderStatus status) {
        final UUID orderId = deliveryOrderRepository.save(deliveryOrder(status, "서울시 송파구 위례성대로 2")).getId();
        assertThatThrownBy(() -> deliveryOrderService.accept(orderId))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("배달 주문을 접수되면 배달 대행사를 호출한다.")
    @Test
    void acceptDeliveryOrder() {
        final UUID orderId = deliveryOrderRepository.save(deliveryOrder(OrderStatus.WAITING, "서울시 송파구 위례성대로 2")).getId();
        final DeliveryOrder actual = deliveryOrderService.accept(orderId);
        assertAll(
                () -> assertThat(actual.getStatus()).isEqualTo(OrderStatus.ACCEPTED),
                () -> assertThat(kitchenridersClient.getOrderId()).isEqualTo(orderId),
                () -> assertThat(kitchenridersClient.getDeliveryAddress()).isEqualTo("서울시 송파구 위례성대로 2")
        );
    }

    @DisplayName("주문을 서빙한다.")
    @Test
    void serve() {
        final UUID orderId = deliveryOrderRepository.save(deliveryOrder(OrderStatus.ACCEPTED)).getId();
        final DeliveryOrder actual = deliveryOrderService.serve(orderId);
        assertThat(actual.getStatus()).isEqualTo(OrderStatus.SERVED);
    }

    @DisplayName("접수된 주문만 서빙할 수 있다.")
    @EnumSource(value = OrderStatus.class, names = "ACCEPTED", mode = EnumSource.Mode.EXCLUDE)
    @ParameterizedTest
    void serve(final OrderStatus status) {
        final UUID orderId = deliveryOrderRepository.save(deliveryOrder(status)).getId();
        assertThatThrownBy(() -> deliveryOrderService.serve(orderId))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("주문을 배달한다.")
    @Test
    void startDelivery() {
        final UUID orderId = deliveryOrderRepository.save(deliveryOrder(OrderStatus.SERVED, "서울시 송파구 위례성대로 2")).getId();
        final DeliveryOrder actual = deliveryOrderService.startDelivery(orderId);
        assertThat(actual.getStatus()).isEqualTo(OrderStatus.DELIVERING);
    }


    @DisplayName("서빙된 주문만 배달할 수 있다.")
    @EnumSource(value = OrderStatus.class, names = "SERVED", mode = EnumSource.Mode.EXCLUDE)
    @ParameterizedTest
    void startDelivery(final OrderStatus status) {
        final UUID orderId = deliveryOrderRepository.save(deliveryOrder(status, "서울시 송파구 위례성대로 2")).getId();
        assertThatThrownBy(() -> deliveryOrderService.startDelivery(orderId))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("주문을 배달 완료한다.")
    @Test
    void completeDelivery() {
        final UUID orderId = deliveryOrderRepository.save(deliveryOrder(OrderStatus.DELIVERING, "서울시 송파구 위례성대로 2")).getId();
        final DeliveryOrder actual = deliveryOrderService.completeDelivery(orderId);
        assertThat(actual.getStatus()).isEqualTo(OrderStatus.DELIVERED);
    }

    @DisplayName("배달 중인 주문만 배달 완료할 수 있다.")
    @EnumSource(value = OrderStatus.class, names = "DELIVERING", mode = EnumSource.Mode.EXCLUDE)
    @ParameterizedTest
    void completeDelivery(final OrderStatus status) {
        final UUID orderId = deliveryOrderRepository.save(deliveryOrder(status, "서울시 송파구 위례성대로 2")).getId();
        assertThatThrownBy(() -> deliveryOrderService.completeDelivery(orderId))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("주문을 완료한다.")
    @Test
    void complete() {
        final DeliveryOrder expected = deliveryOrderRepository.save(deliveryOrder(OrderStatus.DELIVERED, "서울시 송파구 위례성대로 2"));
        final DeliveryOrder actual = deliveryOrderService.complete(expected.getId());
        assertThat(actual.getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @DisplayName("배달 주문의 경우 배달 완료된 주문만 완료할 수 있다.")
    @EnumSource(value = OrderStatus.class, names = "DELIVERED", mode = EnumSource.Mode.EXCLUDE)
    @ParameterizedTest
    void completeDeliveryOrder(final OrderStatus status) {
        final UUID orderId = deliveryOrderRepository.save(deliveryOrder(status, "서울시 송파구 위례성대로 2")).getId();
        assertThatThrownBy(() -> deliveryOrderService.complete(orderId))
                .isInstanceOf(IllegalStateException.class);
    }


    public static OrderLineItem createOrderLineItemRequest(final UUID menuId, final long price, final long quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(new Random().nextLong());
        orderLineItem.setMenuId(menuId);
        orderLineItem.setPrice(BigDecimal.valueOf(price));
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

}
