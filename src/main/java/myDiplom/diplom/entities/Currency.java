package myDiplom.diplom.entities;

import javax.persistence.*;

@Entity
@Table
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long ID;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double value;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Rates rates;

    public Rates getRates() {
        return rates;
    }

    public void setRates(Rates rates) {
        this.rates = rates;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }


}

