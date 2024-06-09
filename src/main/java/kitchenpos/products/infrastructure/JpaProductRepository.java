package kitchenpos.products.infrastructure;

import kitchenpos.products.model.Product;
import kitchenpos.products.model.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaProductRepository extends ProductRepository, JpaRepository<Product, UUID> {
}
