package multipledatasourcedemo.repo.inv;

import multipledatasourcedemo.entity.inv.ItemMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepos extends JpaRepository<ItemMaster, String> {
}
