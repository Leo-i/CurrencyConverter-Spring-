package myDiplom.diplom.repository;

import myDiplom.diplom.entities.Rates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface RatesJpaRepository extends JpaRepository<Rates, Long> {
    Rates findByDate(Date date);
}
