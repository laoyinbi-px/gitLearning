import java.util.List;
import java.io.*;

public class Trade {
    private String product;
    private double amount;
    private double price;
    private Order sellOrder;
    private Order buyOrder;

    public Trade(String product, double amount, double price, Order sellOrder, Order buyOrder) {
        this.product = product;
        this.amount = amount;
        this.price = price;
        this.sellOrder = sellOrder;
        this.buyOrder = buyOrder;
    }

    public String getProduct() {
        return this.product;
    }

    public double getAmount() {
        return this.amount;
    }

    public Order getSellOrder() {
        return this.sellOrder;
    }

    public Order getBuyOrder() {
        return this.buyOrder;
    }

    public double getPrice() {
        return this.price;
    }

    public String toString() {
        String amount = String.format("%.2f",this.getAmount());
        String price = String.format("%.2f",this.getPrice());
        String seller = this.getSellOrder().getTrader().getID();
        String buyer = this.getBuyOrder().getTrader().getID();
        return seller + "->" + buyer + ": " + amount + "x" + getProduct() + " for $" + price + ".";
    }

    public boolean involvesTrader(Trader trader) {
        if (trader == null) {
            return false;
        }
        if (trader.getID().equals(sellOrder.getTrader().getID())) {
            return true;
        } else if (trader.getID().equals(buyOrder.getTrader().getID())) {
            return true;
        } else {
            return false;
        }
    }

    public static void writeTrades(List<Trade> trades, String path) {
        if (trades == null || trades.isEmpty()) {
            return;
        }

        if (path == null || path.isEmpty()) {
            return;
        }

        try {
            PrintWriter pw = new PrintWriter(path);
            for (Trade trade : trades) {
                pw.println(trade.toString());
            }
            pw.close();

        } catch (FileNotFoundException e) {
            return;
        }
    }

    public static void writeTradesBinary(List<Trade> trades, String path) {
        if (trades == null || trades.isEmpty()) {
            return;
        }

        if (path == null || path.isEmpty()) {
            return;
        }

        try{
            FileOutputStream f = new FileOutputStream(path);
            DataOutputStream output = new DataOutputStream(f);

            for (Trade trade : trades) {
                output.writeUTF(trade.toString());
                output.writeUTF("\u001F");
            }
            output.close();

        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }
}