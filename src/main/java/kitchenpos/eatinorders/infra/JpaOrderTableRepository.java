package kitchenpos.eatinorders.infra;

import kitchenpos.eatinorders.domain.order_table.OrderTable;
import kitchenpos.eatinorders.domain.order_table.OrderTableRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaOrderTableRepository extends OrderTableRepository, JpaRepository<OrderTable, UUID> {
}
