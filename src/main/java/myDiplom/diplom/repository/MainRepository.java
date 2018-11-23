package myDiplom.diplom.repository;

import myDiplom.diplom.entities.Currency;
import myDiplom.diplom.entities.Rates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class MainRepository {

    @Autowired
    private RatesJpaRepository ratesJpaRepository;

    @Autowired
    private CurrencyJpaRepository currencyJpaRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean addRates(ArrayList<Currency> rates, Date date,String base) {

        Rates rate = ensureRates(date,base);
        boolean update = false;

        for (Currency currency : rates) {

            if(currencyJpaRepository.findByRatesAndName(rate,currency.getName())!=null)
                continue;
            update = true;
            currency.setRates(rate);
            currencyJpaRepository.save(currency);
        }
        if(!update)
            return false;
        ratesJpaRepository.save(rate);
        return true;
    }

    private Rates ensureRates(Date date,String base) {
        Rates rates = ratesJpaRepository.findByDate(date);
        if (rates == null) {
            rates = new Rates();
            rates.setBase(base);
            rates.setDate(date);
            ratesJpaRepository.save(rates);
        }
        return rates;
    }

    @Transactional(readOnly = true)
    public List<Currency> getAllCurrency(){
        return currencyJpaRepository.findAll();
    }
    @Transactional(readOnly = true)
    public List<Rates> getAllRates() {
        return ratesJpaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Rates getRate(Date date) {
        return ratesJpaRepository.findByDate(date);
    }


}
