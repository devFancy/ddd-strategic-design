package kitchenpos.application;

import kitchenpos.eatinorder.domain.EatInOrder;
import kitchenpos.eatinorder.domain.EatInOrderRepository;
import kitchenpos.eatinorder.domain.OrderStatus;
import kitchenpos.eatinorder.domain.OrderTable;

import java.util.*;

public class InMemoryEatInOrderRepository implements EatInOrderRepository {
    private final Map<UUID, EatInOrder> eatInOrders = new HashMap<>();

    @Override
    public EatInOrder save(EatInOrder order) {
        eatInOrders.put(order.getId(), order);
        return order;
    }

    @Override
    public Optional<EatInOrder> findById(UUID id) {
        return Optional.ofNullable(eatInOrders.get(id));
    }

    @Override
    public List<EatInOrder> findAll() {
        return new ArrayList<>(eatInOrders.values());
    }

    @Override
    public boolean existsByOrderTableAndStatusNot(OrderTable orderTable, OrderStatus status) {
        return eatInOrders.values()
                .stream()
                .anyMatch(order -> order.getOrderTable().equals(orderTable) && order.getStatus() != status);
    }
}
