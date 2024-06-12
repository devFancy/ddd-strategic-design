package kitchenpos.testfixture;


import kitchenpos.takeoutorder.domain.OrderLineItem;
import kitchenpos.takeoutorder.domain.OrderStatus;
import kitchenpos.takeoutorder.domain.TakeOutOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TakeOutOrderTestFixture {

    public static TakeOutOrder createOrderRequest(OrderStatus status, LocalDateTime orderDateTime, List<OrderLineItem> orderLineItems){
        TakeOutOrder takeOutOrder = new TakeOutOrder();
        takeOutOrder.setStatus(status);
        takeOutOrder.setOrderLineItems(orderLineItems);

        return takeOutOrder;
    }



    public static TakeOutOrder createOrder(OrderStatus status, LocalDateTime orderDateTime, List<OrderLineItem> orderLineItems){
        TakeOutOrder order = new TakeOutOrder();
        order.setId(UUID.randomUUID());
        order.setStatus(status);
        order.setOrderLineItems(orderLineItems);

        return order;
    }
}
