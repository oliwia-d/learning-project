package pl.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.company.model.GuineaPig;

@Repository
public interface GuineaPigRepository extends JpaRepository<GuineaPig, Long> {
}
