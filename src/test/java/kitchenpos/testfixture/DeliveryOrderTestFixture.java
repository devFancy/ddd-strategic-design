package kitchenpos.testfixture;

import kitchenpos.deliveryorder.domain.DeliveryOrder;
import kitchenpos.deliveryorder.domain.OrderLineItem;
import kitchenpos.deliveryorder.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class DeliveryOrderTestFixture {

    public static DeliveryOrder createOrderRequest(OrderStatus status, LocalDateTime orderDateTime, List<OrderLineItem> orderLineItems, String deliveryAddress){
        DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setStatus(status);
        deliveryOrder.setOrderLineItems(orderLineItems);
        deliveryOrder.setDeliveryAddress(deliveryAddress);

        return deliveryOrder;
    }



    public static DeliveryOrder createOrder(OrderStatus status, LocalDateTime orderDateTime, List<OrderLineItem> orderLineItems, String deliveryAddress){
        DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setId(UUID.randomUUID());
        deliveryOrder.setStatus(status);
        deliveryOrder.setOrderLineItems(orderLineItems);
        deliveryOrder.setDeliveryAddress(deliveryAddress);

        return deliveryOrder;
    }
}
