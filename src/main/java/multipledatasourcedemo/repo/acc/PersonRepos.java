package multipledatasourcedemo.repo.acc;

import multipledatasourcedemo.entity.acc.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepos extends JpaRepository<Person, String> {
}
