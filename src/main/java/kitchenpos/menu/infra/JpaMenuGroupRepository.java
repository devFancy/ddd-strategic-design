package kitchenpos.menu.infra;

import kitchenpos.menu.domain.menu_group.MenuGroup;
import kitchenpos.menu.domain.menu_group.MenuGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaMenuGroupRepository extends MenuGroupRepository, JpaRepository<MenuGroup, UUID> {
}
