package kitchenpos.application;

import kitchenpos.eatinorder.application.EatInOrderService;
import kitchenpos.eatinorder.domain.*;
import kitchenpos.menu.domain.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static kitchenpos.Fixtures.*;
import static kitchenpos.testfixture.EatInOrderTestFixture.createOrderRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class EatInOrderServiceTest {
    private EatInOrderRepository eatInOrderRepository;
    private MenuRepository menuRepository;
    private OrderTableRepository orderTableRepository;
    private EatInOrderService eatInOrderService;

    @BeforeEach
    void setUp(){
        eatInOrderRepository = new InMemoryEatInOrderRepository();
        menuRepository = new InMemoryMenuRepository();
        orderTableRepository = new InMemoryOrderTableRepository();
        eatInOrderService = new EatInOrderService(eatInOrderRepository, menuRepository, orderTableRepository);
    }

    @DisplayName("1개 이상의 등록된 메뉴로 매장 주문을 등록할 수 있다.")
    @Test
    void createEatInOrder() {
        final UUID menuId = menuRepository.save(menu(19_000L, true, menuProduct())).getId();
        final OrderTable orderTable = orderTable(true, 4);
        orderTableRepository.save(orderTable).getId();
        final EatInOrder expected = createOrderRequest(
                OrderStatus.WAITING,
                LocalDateTime.now(),
                List.of(createOrderLineItemRequest(menuId, 19_000L, 3L)),
                orderTable
        );
        final EatInOrder actual = eatInOrderService.create(expected);
        assertThat(actual).isNotNull();
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getStatus()).isEqualTo(OrderStatus.WAITING),
                () -> assertThat(actual.getOrderLineItems()).hasSize(1),
                () -> assertThat(actual.getOrderTable().getId()).isEqualTo(expected.getOrderTableId())
        );
    }

    @DisplayName("매장 주문은 주문 항목의 수량이 0 미만일 수 있다.")
    @ValueSource(longs = -1L)
    @ParameterizedTest
    void createEatInOrder(final long quantity) {
        final UUID menuId = menuRepository.save(menu(19_000L, true, menuProduct())).getId();
        final OrderTable orderTable = orderTable(true, 4);
        orderTableRepository.save(orderTable).getId();
        final EatInOrder expected = createOrderRequest(
                OrderStatus.WAITING,
                LocalDateTime.now(),
                List.of(createOrderLineItemRequest(menuId, 19_000L, quantity)),
                orderTable
        );

        assertDoesNotThrow(() -> eatInOrderService.create(expected));
    }

    @DisplayName("빈 테이블에는 매장 주문을 등록할 수 없다.")
    @Test
    void createEmptyTableEatInOrder() {
        final UUID menuId = menuRepository.save(menu(19_000L, true, menuProduct())).getId();
        final OrderTable orderTable = orderTable(false, 0);
        orderTableRepository.save(orderTable).getId();
        final EatInOrder expected = createOrderRequest(
                kitchenpos.eatinorder.domain.OrderStatus.WAITING,
                LocalDateTime.now(),
                List.of(createOrderLineItemRequest(menuId, 19_000L, 3L)),
                orderTable
        );
        assertThatThrownBy(() -> eatInOrderService.create(expected))
                .isInstanceOf(IllegalStateException.class);
    }
    @DisplayName("주문 테이블의 모든 매장 주문이 완료되면 빈 테이블로 설정한다.")
    @Test
    void completeEatInOrder() {
        final OrderTable orderTable = orderTableRepository.save(orderTable(true, 4));
        final EatInOrder expected = eatInOrderRepository.save(eatInOrder(OrderStatus.SERVED, orderTable));
        final EatInOrder actual = eatInOrderService.complete(expected.getId());
        assertAll(
                () -> assertThat(actual.getStatus()).isEqualTo(OrderStatus.COMPLETED),
                () -> assertThat(orderTableRepository.findById(orderTable.getId()).get().isOccupied()).isFalse(),
                () -> assertThat(orderTableRepository.findById(orderTable.getId()).get().getNumberOfGuests()).isEqualTo(0)
        );
    }

    @DisplayName("완료되지 않은 매장 주문이 있는 주문 테이블은 빈 테이블로 설정하지 않는다.")
    @Test
    void completeNotTable() {
        final OrderTable orderTable = orderTableRepository.save(orderTable(true, 4));
        eatInOrderRepository.save(eatInOrder(OrderStatus.ACCEPTED, orderTable));
        final EatInOrder expected = eatInOrderRepository.save(eatInOrder(OrderStatus.SERVED, orderTable));
        final EatInOrder actual = eatInOrderService.complete(expected.getId());
        assertAll(
                () -> assertThat(actual.getStatus()).isEqualTo(OrderStatus.COMPLETED),
                () -> assertThat(orderTableRepository.findById(orderTable.getId()).get().isOccupied()).isTrue(),
                () -> assertThat(orderTableRepository.findById(orderTable.getId()).get().getNumberOfGuests()).isEqualTo(4)
        );
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
