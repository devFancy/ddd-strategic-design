package kitchenpos.menus.infrastructure;

import kitchenpos.menus.domain.model.MenuGroup;
import kitchenpos.menus.domain.model.MenuGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaMenuGroupRepository extends MenuGroupRepository, JpaRepository<MenuGroup, UUID> {
}
