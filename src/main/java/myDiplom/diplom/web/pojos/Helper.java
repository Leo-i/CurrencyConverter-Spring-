package myDiplom.diplom.web.pojos;

import myDiplom.diplom.entities.Currency;
import myDiplom.diplom.entities.Rates;
import java.util.HashMap;
import java.util.Map;

public class Helper {

    public static RatesInfo convert(Rates rates) {
        RatesInfo info = new RatesInfo();
        info.setTime(rates.getDate());
        info.setBase(rates.getBase());
        Map<String, Double> map = new HashMap<>();
        for (Currency currency : rates.getCurrencies())
            map.put(currency.getName(), currency.getValue());
        info.setRates(map);
        return info;
    }

    public static Double convertCurrency(RatesInfo info,String from,String to, String count){
        double fromCurrency = info.getRates().get(from);
        double toCurrency = info.getRates().get(to);
        return Double.parseDouble(count)*toCurrency/fromCurrency;
    }

}
