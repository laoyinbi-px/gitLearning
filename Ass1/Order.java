import java.util.HashSet;

public class Order {
    private String product;
    private boolean buy;
    private double amount;
    private double price;
    private Trader trader;
    private String id;
    private boolean isClosed;


    public Order(String product, boolean buy, double amount, double price, Trader trader, String id) {
        this.product = product;
        this.buy = buy;
        this.amount = amount;
        this.price = price;
        this.trader = trader;
        this.id = id;
        this.isClosed = false;
    }

    public String getProduct() {
        return this.product;
    }

    public boolean isBuy() {
        return this.buy;
    }

    public double getAmount() {
        return this.amount;
    }

    public Trader getTrader() {
        return this.trader;
    }

    public void close() {
        this.isClosed = true;
    }

    public boolean isClosed() {
        return this.isClosed;
    }

    public double getPrice() {
        return this.price;
    }

    public String getID() {
        return this.id;
    }

    public void adjustAmount(double change) {
        if (this.amount + change >= 0) {
            this.amount += change;
        }
    }

    public String toString() {
        String amount = String.format("%.2f",this.amount);
        String price = String.format("%.2f", this.price);
        if (this.isBuy()) {
            return this.id + ": BUY " + amount + "x" + this.product + " @ $" + price;
        } else {
            return this.id + ": SELL " + amount + "x" + this.product + " @ $" + price;
        }
    }

}