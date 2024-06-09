package kitchenpos.orders.infrastructure;

import kitchenpos.orders.domain.model.Order;
import kitchenpos.orders.domain.model.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaOrderRepository extends OrderRepository, JpaRepository<Order, UUID> {
}
