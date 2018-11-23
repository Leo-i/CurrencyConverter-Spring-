package myDiplom.diplom.web.pojos;

import java.util.Date;
import java.util.Map;

public class RatesInfo {
    Date time;
    String base;
    Map<String,Double> rates;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }
}
