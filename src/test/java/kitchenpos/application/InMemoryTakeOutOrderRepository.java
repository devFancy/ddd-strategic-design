package kitchenpos.application;

import kitchenpos.takeoutorder.domain.TakeOutOrder;
import kitchenpos.takeoutorder.domain.TakeOutOrderRepository;

import java.util.*;

public class InMemoryTakeOutOrderRepository implements TakeOutOrderRepository {
    private final Map<UUID, TakeOutOrder> takeOutOrders = new HashMap<>();

    @Override
    public TakeOutOrder save(TakeOutOrder order) {
        takeOutOrders.put(order.getId(), order);
        return order;
    }

    @Override
    public Optional<TakeOutOrder> findById(UUID id) {
        return Optional.ofNullable(takeOutOrders.get(id));
    }

    @Override
    public List<TakeOutOrder> findAll() {
        return new ArrayList<>(takeOutOrders.values());
    }
}
