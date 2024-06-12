package kitchenpos.testfixture;

import kitchenpos.menu.domain.Menu;

public class OrderLineItemTestFixture {

    public static kitchenpos.deliveryorder.domain.OrderLineItem createDeliveryOrderLine(long seq, int quantity, Menu menu){
        kitchenpos.deliveryorder.domain.OrderLineItem orderLineItem = new kitchenpos.deliveryorder.domain.OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setQuantity(quantity);
        orderLineItem.setMenu(menu);
        orderLineItem.setPrice(menu.getPrice());
        orderLineItem.setMenuId(menu.getId());

        return orderLineItem;
    }

    public static kitchenpos.eatinorder.domain.OrderLineItem createEatInOrderLine(long seq, int quantity, Menu menu){
        kitchenpos.eatinorder.domain.OrderLineItem orderLineItem = new kitchenpos.eatinorder.domain.OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setQuantity(quantity);
        orderLineItem.setMenu(menu);
        orderLineItem.setPrice(menu.getPrice());
        orderLineItem.setMenuId(menu.getId());

        return orderLineItem;
    }

    public static kitchenpos.takeoutorder.domain.OrderLineItem createTakeoutOrderLine(long seq, int quantity, Menu menu){
        kitchenpos.takeoutorder.domain.OrderLineItem orderLineItem = new kitchenpos.takeoutorder.domain.OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setQuantity(quantity);
        orderLineItem.setMenu(menu);
        orderLineItem.setPrice(menu.getPrice());
        orderLineItem.setMenuId(menu.getId());

        return orderLineItem;
    }
}
