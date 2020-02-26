package Entity;


import javax.persistence.*;

@Entity
@Table(name="goods", schema = "schema")
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    public String warehouse_name;
    public long code;
    public String name;
    int amount;
    @Column(name = "buys")
    public double lastBuyPrice;
    @Column(name="sells")
    public double lastSellPrice;

    public Goods(long code, String name, int amount, double lastBuyPrice, double lastSellPrice) {
        this.code = code;
        this.name = name;
        this.amount = amount;
        this.lastBuyPrice = lastBuyPrice;
        this.lastSellPrice = lastSellPrice;
    }

    public Goods(){}

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getName() { return name; }

    public String getWarehouse_name() {
        return warehouse_name;
    }

    public int getId() {
        return id;
    }

    public void setWarehouse_name(String warehouse_name) {
        this.warehouse_name = warehouse_name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLastBuyPrice() {
        return lastBuyPrice;
    }

    public void setLastBuyPrice(double lastBuyPrice) {
        this.lastBuyPrice = lastBuyPrice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getLastSellPrice() {
        return lastSellPrice;
    }

    public void setLastSellPrice(double lastSellPrice) {
        this.lastSellPrice = lastSellPrice;
    }

    public static String toJson(Goods obj)
    {
        return "{\"code\" : \"" + obj.code + "\"," +
                "\"name\" : \"" + obj.name + "\","+
                "\"buyPrice\" : \"" + obj.lastBuyPrice + "\","+
                "\"SellPrice\" : \"" + obj.lastSellPrice + "\""+
                "}";
    }

    @Override
    public String toString() {
        return "Goods{" +
                "code=" + code +
                ", amount=" + amount +
                ", name='" + name + '\'' +
                ", lastBuyPrice=" + lastBuyPrice +
                ", lastSellPrice=" + lastSellPrice +
                ", warehouse_name=" + warehouse_name +
                '}';
    }
}
