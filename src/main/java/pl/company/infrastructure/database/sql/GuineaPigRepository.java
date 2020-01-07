package pl.company.infrastructure.database.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.company.domain.guineapig.GuineaPig;

@Repository
public interface GuineaPigRepository extends JpaRepository<GuineaPig, Long> {
}
