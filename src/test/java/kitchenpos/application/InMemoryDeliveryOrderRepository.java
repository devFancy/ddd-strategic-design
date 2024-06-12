package kitchenpos.application;

import kitchenpos.deliveryorder.domain.DeliveryOrder;
import kitchenpos.deliveryorder.domain.DeliveryOrderRepository;

import java.util.*;

public class InMemoryDeliveryOrderRepository implements DeliveryOrderRepository {
    private final Map<UUID, DeliveryOrder> deliveryOrders = new HashMap<>();

    @Override
    public DeliveryOrder save(DeliveryOrder order) {
        deliveryOrders.put(order.getId(), order);
        return order;
    }

    @Override
    public Optional<DeliveryOrder> findById(UUID id) {
        return Optional.ofNullable(deliveryOrders.get(id));
    }

    @Override
    public List<DeliveryOrder> findAll() {
        return new ArrayList<>(deliveryOrders.values());
    }
}
