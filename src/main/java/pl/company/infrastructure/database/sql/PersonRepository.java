package pl.company.infrastructure.database.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.company.domain.person.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person,Long> {

}
