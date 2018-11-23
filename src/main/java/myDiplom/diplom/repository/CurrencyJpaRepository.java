package myDiplom.diplom.repository;

import myDiplom.diplom.entities.Currency;
import myDiplom.diplom.entities.Rates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyJpaRepository extends JpaRepository<Currency,Long> {
    Currency findByRatesAndName(Rates rates,String name);
}
