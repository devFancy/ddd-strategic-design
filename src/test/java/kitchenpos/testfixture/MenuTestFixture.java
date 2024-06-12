package kitchenpos.testfixture;


import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class MenuTestFixture {

    public static Menu createMenuRequest(String name, long price, boolean displayed, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setDisplayed(displayed);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    public static Menu createMenu(String name, long price, boolean displayed, List<MenuProduct> menuProducts) {

        Menu menu = new Menu();
        ;
        menu.setId(UUID.randomUUID());
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setDisplayed(displayed);
        menu.setMenuProducts(menuProducts);

        return menu;
    }
}
