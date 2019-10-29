package pl.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.company.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person,Long> {

}
