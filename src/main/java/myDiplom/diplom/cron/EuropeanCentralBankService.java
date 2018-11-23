package myDiplom.diplom.cron;

import myDiplom.diplom.repository.MainRepository;
import myDiplom.diplom.entities.Currency;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@Service
public class EuropeanCentralBankService {

    private static final String QUERY_URL = "https://api.exchangeratesapi.io/latest";
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final String currencies[] = new String[]{
            "CAD", "BRL", "HUF", "DKK", "JPY", "ILS", "INR", "BGN",
            "TRY", "RON", "GBP", "PHP", "HRK", "NOK", "USD", "MXN",
            "AUD", "IDR", "KRW", "HKD", "ZAR", "ISK", "CZK", "THB",
            "MYR", "NZD", "PLN", "SEK", "RUB", "CNY", "SGD", "CHF"};

    @Autowired
    private MainRepository repository;

    @Scheduled(cron = "* */30 * * * ?")
    @Scheduled(initialDelay = 0, fixedDelay = Long.MAX_VALUE)
    public void update() {

        JSONParser parser = new JSONParser();
        try {
            URL url = new URL(QUERY_URL);
            JSONObject object;
            try (InputStreamReader reader = new InputStreamReader(url.openStream())) {
                object = (JSONObject) parser.parse(reader);
            }

            JSONObject rates = (JSONObject) object.get("rates");


            Date date = parseDate(object.get("date"));

            String base = object.get("base").toString();


            ArrayList<Currency> currencyArrayList = new ArrayList<>();

            for (String str : currencies) {
                try {
                    Currency currency = new Currency();
                    currency.setValue(parseDouble(rates.get(str)));
                    currency.setName(str);
                    currencyArrayList.add(currency);
                } catch (Exception ignore) {
                }
            }

            Currency baseCurrency = new Currency();
            baseCurrency.setName(base);
            baseCurrency.setValue(1);
            currencyArrayList.add(baseCurrency);

            repository.addRates(currencyArrayList, date, base);


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private double parseDouble(Object object) {
        return Double.parseDouble(object.toString());
    }


    private Date parseDate(Object object) throws ParseException {
        return dateFormat.parse(object.toString());
    }
}
