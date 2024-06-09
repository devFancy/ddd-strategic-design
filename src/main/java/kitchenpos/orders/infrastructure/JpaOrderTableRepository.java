package kitchenpos.orders.infrastructure;

import kitchenpos.orders.domain.model.OrderTableRepository;
import kitchenpos.orders.domain.model.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaOrderTableRepository extends OrderTableRepository, JpaRepository<OrderTable, UUID> {
}
