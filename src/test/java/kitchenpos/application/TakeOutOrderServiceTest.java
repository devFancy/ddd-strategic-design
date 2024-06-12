package kitchenpos.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.takeoutorder.application.TakeOutOrderService;
import kitchenpos.takeoutorder.domain.OrderLineItem;
import kitchenpos.takeoutorder.domain.OrderStatus;
import kitchenpos.takeoutorder.domain.TakeOutOrder;
import kitchenpos.takeoutorder.domain.TakeOutOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static kitchenpos.Fixtures.*;
import static kitchenpos.testfixture.TakeOutOrderTestFixture.createOrderRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TakeOutOrderServiceTest {

    private TakeOutOrderRepository takeOutOrderRepository;
    private MenuRepository menuRepository;
    private TakeOutOrderService takeOutOrderService;

    @BeforeEach
    void setUp() {
        takeOutOrderRepository = new InMemoryTakeOutOrderRepository();
        menuRepository = new InMemoryMenuRepository();
        takeOutOrderService = new TakeOutOrderService(takeOutOrderRepository, menuRepository);
    }

    @DisplayName("1개 이상의 등록된 메뉴로 포장 주문을 등록할 수 있다.")
    @Test
    void createTakeoutOrder() {
        final UUID menuId = menuRepository.save(menu(19_000L, true, menuProduct())).getId();
        final TakeOutOrder expected = createOrderRequest(
                OrderStatus.WAITING,
                LocalDateTime.now(),
                List.of(createOrderLineItemRequest(menuId, 19_000L, 3L)));
        final TakeOutOrder actual = takeOutOrderService.create(expected);
        assertThat(actual).isNotNull();
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getStatus()).isEqualTo(OrderStatus.WAITING),
                () -> assertThat(actual.getOrderLineItems()).hasSize(1)
        );
    }


    @DisplayName("포장 및 매장 주문의 경우 서빙된 주문만 완료할 수 있다.")
    @EnumSource(value = OrderStatus.class, names = "SERVED", mode = EnumSource.Mode.EXCLUDE)
    @ParameterizedTest
    void completeTakeoutAndEatInOrder(final OrderStatus status) {
        final UUID orderId = takeOutOrderRepository.save(takeOutOrder(status)).getId();
        assertThatThrownBy(() -> takeOutOrderService.complete(orderId))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("메뉴가 없으면 등록할 수 없다.")
    @MethodSource("orderLineItems")
    @ParameterizedTest
    void create(final List<OrderLineItem> orderLineItems) {
        final TakeOutOrder expected = createOrderRequest(
                OrderStatus.WAITING,
                LocalDateTime.now(),
                orderLineItems);
        assertThatThrownBy(() -> takeOutOrderService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static List<Arguments> orderLineItems() {
        return Arrays.asList(
                null,
                Arguments.of(Collections.emptyList()),
                Arguments.of(Arrays.asList(createOrderLineItemRequest(INVALID_ID, 19_000L, 3L)))
        );
    }

    @DisplayName("숨겨진 메뉴는 주문할 수 없다.")
    @Test
    void createNotDisplayedMenuOrder() {
        final UUID menuId = menuRepository.save(menu(19_000L, false, menuProduct())).getId();
        final TakeOutOrder expected = createOrderRequest(
                OrderStatus.WAITING,
                LocalDateTime.now(),
                List.of(createOrderLineItemRequest(menuId, 19_000L, 3L)));
        assertThatThrownBy(() -> takeOutOrderService.create(expected))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("주문한 메뉴의 가격은 실제 메뉴 가격과 일치해야 한다.")
    @Test
    void createNotMatchedMenuPriceOrder() {
        final UUID menuId = menuRepository.save(menu(19_000L, true, menuProduct())).getId();
        final TakeOutOrder expected = createOrderRequest(
                OrderStatus.WAITING,
                LocalDateTime.now(),
                List.of(createOrderLineItemRequest(menuId, 16_000L, 3L)));
        assertThatThrownBy(() -> takeOutOrderService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
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
