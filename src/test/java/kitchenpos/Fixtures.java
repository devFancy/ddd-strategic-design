package kitchenpos;

import kitchenpos.deliveryorder.domain.DeliveryOrder;
import kitchenpos.eatinorder.domain.EatInOrder;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;

import kitchenpos.eatinorder.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.takeoutorder.domain.TakeOutOrder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public class Fixtures {
    public static final UUID INVALID_ID = new UUID(0L, 0L);

    public static Menu menu() {
        return menu(19_000L, true, menuProduct());
    }

    public static Menu menu(final long price, final MenuProduct... menuProducts) {
        return menu(price, false, menuProducts);
    }

    public static Menu menu(final long price, final boolean displayed, final MenuProduct... menuProducts) {
        final Menu menu = new Menu();
        menu.setId(UUID.randomUUID());
        menu.setName("후라이드+후라이드");
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroup(menuGroup());
        menu.setDisplayed(displayed);
        menu.setMenuProducts(Arrays.asList(menuProducts));
        return menu;
    }

    public static MenuGroup menuGroup() {
        return menuGroup("두마리메뉴");
    }

    public static MenuGroup menuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(UUID.randomUUID());
        menuGroup.setName(name);
        return menuGroup;
    }

    public static MenuProduct menuProduct() {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(new Random().nextLong());
        menuProduct.setProduct(product());
        menuProduct.setQuantity(2L);
        return menuProduct;
    }

    public static MenuProduct menuProduct(final Product product, final long quantity) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(new Random().nextLong());
        menuProduct.setProduct(product);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static DeliveryOrder deliveryOrder(final kitchenpos.deliveryorder.domain.OrderStatus status, final String deliveryAddress) {
        final DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setId(UUID.randomUUID());
        deliveryOrder.setStatus(status);
        deliveryOrder.setOrderLineItems(Arrays.asList(deliveryOrderLineItem()));
        deliveryOrder.setDeliveryAddress(deliveryAddress);
        return deliveryOrder;
    }

    public static DeliveryOrder deliveryOrder(final kitchenpos.deliveryorder.domain.OrderStatus status) {
        final DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setId(UUID.randomUUID());
        deliveryOrder.setStatus(status);
        deliveryOrder.setOrderLineItems(Arrays.asList(deliveryOrderLineItem()));
        return deliveryOrder;
    }

    public static TakeOutOrder takeOutOrder(final kitchenpos.takeoutorder.domain.OrderStatus status) {
        final TakeOutOrder takeOutOrder = new TakeOutOrder();
        takeOutOrder.setId(UUID.randomUUID());
        takeOutOrder.setStatus(status);
        takeOutOrder.setOrderLineItems(Arrays.asList(takeoutOrderLineItem()));
        return takeOutOrder;
    }

    public static EatInOrder eatInOrder(final kitchenpos.eatinorder.domain.OrderStatus status) {
        final EatInOrder eatInOrder = new EatInOrder();
        eatInOrder.setId(UUID.randomUUID());
        eatInOrder.setStatus(status);
        eatInOrder.setOrderLineItems(Arrays.asList(EatInOrderLineItem()));
        return eatInOrder;
    }

    public static EatInOrder eatInOrder(final kitchenpos.eatinorder.domain.OrderStatus status, final OrderTable orderTable) {
        final EatInOrder eatInOrder = new EatInOrder();
        eatInOrder.setId(UUID.randomUUID());
        eatInOrder.setStatus(status);
        eatInOrder.setOrderLineItems(Arrays.asList(EatInOrderLineItem()));
        eatInOrder.setOrderTable(orderTable);
        return eatInOrder;
    }

    public static kitchenpos.eatinorder.domain.OrderLineItem EatInOrderLineItem() {
        final kitchenpos.eatinorder.domain.OrderLineItem orderLineItem = new kitchenpos.eatinorder.domain.OrderLineItem();
        orderLineItem.setSeq(new Random().nextLong());
        orderLineItem.setMenu(menu());
        return orderLineItem;
    }

    public static kitchenpos.deliveryorder.domain.OrderLineItem deliveryOrderLineItem() {
        final kitchenpos.deliveryorder.domain.OrderLineItem orderLineItem = new kitchenpos.deliveryorder.domain.OrderLineItem();
        orderLineItem.setSeq(new Random().nextLong());
        orderLineItem.setMenu(menu());
        return orderLineItem;
    }

    public static kitchenpos.takeoutorder.domain.OrderLineItem takeoutOrderLineItem() {
        final kitchenpos.takeoutorder.domain.OrderLineItem orderLineItem = new kitchenpos.takeoutorder.domain.OrderLineItem();
        orderLineItem.setSeq(new Random().nextLong());
        orderLineItem.setMenu(menu());
        return orderLineItem;
    }

    public static OrderTable orderTable() {
        return orderTable(false, 0);
    }

    public static OrderTable orderTable(final boolean occupied, final int numberOfGuests) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(UUID.randomUUID());
        orderTable.setName("1번");
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setOccupied(occupied);
        return orderTable;
    }

    public static Product product() {
        return product("후라이드", 16_000L);
    }

    public static Product product(final String name, final long price) {
        final Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }
}
