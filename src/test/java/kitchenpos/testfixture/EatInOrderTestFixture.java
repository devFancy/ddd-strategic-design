package kitchenpos.testfixture;

import kitchenpos.eatinorder.domain.EatInOrder;
import kitchenpos.eatinorder.domain.OrderLineItem;
import kitchenpos.eatinorder.domain.OrderStatus;
import kitchenpos.eatinorder.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class EatInOrderTestFixture {

    public static EatInOrder createOrderRequest(OrderStatus status, LocalDateTime orderDateTime, List<OrderLineItem> orderLineItems, OrderTable orderTable){
        EatInOrder eatInOrder = new EatInOrder();
        eatInOrder.setStatus(status);
        eatInOrder.setOrderLineItems(orderLineItems);
        eatInOrder.setOrderTable(orderTable);
        eatInOrder.setOrderTableId(orderTable.getId());

        return eatInOrder;
    }

    public static EatInOrder createOrder(OrderStatus status, LocalDateTime orderDateTime, List<OrderLineItem> orderLineItems, OrderTable orderTable){
        EatInOrder eatInOrder = new EatInOrder();
        eatInOrder.setId(UUID.randomUUID());
        eatInOrder.setStatus(status);
        eatInOrder.setOrderLineItems(orderLineItems);
        eatInOrder.setOrderTable(orderTable);
        eatInOrder.setOrderTableId(orderTable.getId());

        return eatInOrder;
    }
}
