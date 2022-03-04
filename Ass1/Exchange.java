import java.util.*;
import java.io.*;
import java.lang.String;

public class Exchange {
    private HashMap<String, Trader> traders;
    private Market market;
    private Scanner scan;
    private String orderId;

    public Exchange () {
        this.traders = new HashMap<String, Trader>();
        this.market = new Market();
        this.scan = new Scanner(System.in);
        this.orderId = "0000";
    }

    public void addHex() {
        int value = Integer.parseInt(this.orderId, 16);
        Integer.toHexString(value);
        value++;
        String hexString = Integer.toHexString(value).toUpperCase();
        String str = String.format("%4s",hexString);
        this.orderId = str.replace(' ','0');
    }


    public void addCommand(String id, String strBalance) {
        double balance = Double.parseDouble(strBalance);

        if (traders.get(id) != null) {
            System.out.println("Trader with given ID already exists.");
            return;
        }

        if (balance < 0) {
            System.out.println("Initial balance cannot be negative.");
            return;
        }

        Trader trader = new Trader(id, balance);

        this.traders.put(id, trader);
        System.out.println("Success.");
    }

    public void balance(String id) {
        Trader trader = this.traders.get(id);

        if (trader == null) {
            System.out.println("No such trader in the market.");
            return;
        }

        String balance = String.format("%.2f",trader.getBalance());
        System.out.println("$" + balance);
    }

    public void inventory(String id) {
        Trader trader = this.traders.get(id);

        if (trader == null) {
            System.out.println("No such trader in the market.");
            return;
        }

        List<String> productList = trader.getProductsInInventory();

        if (productList.isEmpty() || productList == null) {
            System.out.println("Trader has an empty inventory.");
            return;
        }

        for (String product : productList) {
            System.out.println(product);
        }
    }

    public void amount(String id, String product) {
        Trader trader = this.traders.get(id);

        if (trader == null) {
            System.out.println("No such trader in the market.");
            return;
        }

        double amount = trader.getAmountStored(product);

        if (amount == -1.0 || amount == 0) {
            System.out.println("Product not in inventory.");
            return;
        }

        String amountString = String.format("%.2f",amount);
        System.out.println(amountString);

    }

    public void sell(String id, String product, String strAmount, String strPrice) {
        double amount = Double.parseDouble(strAmount);
        double price = Double.parseDouble(strPrice);
        Trader trader = this.traders.get(id);

        if (trader == null) {
            System.out.println("No such trader in the market.");
            return;
        }

//        if (amount <= 0 || price <= 0) {
//            System.out.println("Order could not be placed onto the market.");
//            return;
//        }

        Order sellOrder = new Order(product, false, amount, price, trader, this.orderId);
        addHex();

        List<Trade> trades = this.market.placeSellOrder(sellOrder);

        if (sellOrder.isClosed()) {
            System.out.println("Product sold in entirety, trades as follows:");
            for (Trade trade : trades) {
                System.out.println(trade);
            }
        } else {
            if (trades == null) {
                System.out.println("Order could not be placed onto the market.");
            } else if (trades.isEmpty()) {
                System.out.println("No trades could be made, order added to sell book.");
            } else {
                System.out.println("Product sold in part, trades as follows:");
                for (Trade trade : trades) {
                    System.out.println(trade);
                }
            }
        }
    }

    public void buy(String id, String product, String strAmount, String strPrice) {
        double amount = Double.parseDouble(strAmount);
        double price = Double.parseDouble(strPrice);
        Trader trader = this.traders.get(id);

        if (trader == null) {
            System.out.println("No such trader in the market.");
            return;
        }

//        if (amount <= 0 || price <= 0) {
//            System.out.println("Order could not be placed onto the market.");
//            return;
//        }

        Order buyOrder = new Order(product, true, amount, price, trader, this.orderId);
        addHex();

        List<Trade> trades = this.market.placeBuyOrder(buyOrder);

        if (buyOrder.isClosed()) {
            System.out.println("Product bought in entirety, trades as follows:");
            for (Trade trade : trades) {
                System.out.println(trade);
            }
        } else {
            if (trades == null) {
                System.out.println("Order could not be placed onto the market.");
            } else if (trades.isEmpty()) {
                System.out.println("No trades could be made, order added to buy book.");
            } else {
                System.out.println("Product bought in part, trades as follows:");
                for (Trade trade : trades) {
                    System.out.println(trade);
                }
            }
        }
    }

    public void importProduct(String id, String product, String strAmount) {
        double amount = Double.parseDouble(strAmount);
        Trader trader = this.traders.get(id);

        if (trader == null) {
            System.out.println("No such trader in the market.");
            return;
        }

        if (trader.importProduct(product, amount) < 0) {
            System.out.println("Could not import product into market.");
            return;
        }

        String amountString = String.format("%.2f",trader.getAmountStored(product));

        System.out.println("Trader now has " + amountString + " units of " + product + ".");
    }

    public void exportProduct(String id, String product, String strAmount) {
        double amount = Double.parseDouble(strAmount);
        Trader trader = this.traders.get(id);

        if (trader == null) {
            System.out.println("No such trader in the market.");
            return;
        }

        double flag = trader.exportProduct(product, amount);

        if (flag < 0) {
            System.out.println("Could not export product out of market.");
            return;
        }

        if (flag == 0.0) {
            System.out.printf("Trader now has no units of %s.\n", product);
            return;
        }

        String amountString = String.format("%.2f",trader.getAmountStored(product));
        System.out.println("Trader now has " + amountString + " units of " + product + ".");
    }

    public void cancelSell(String orderId) {

        if (market.cancelSellOrder(orderId)) {
            System.out.println("Order successfully cancelled.");
        } else {
            System.out.println("No such order in sell book.");
        }

    }

    public void cancelBuy(String orderId) {

        if (market.cancelBuyOrder(orderId)) {
            System.out.println("Order successfully cancelled.");
        } else {
            System.out.println("No such order in buy book.");
        }
    }

    public void orderCommand(String orderId) {
        this.market.orderExist(orderId);
    }

    public void traderCommand() {
        ArrayList<String> traderList = new ArrayList<>(this.traders.keySet());

        if (traderList.isEmpty()) {
            System.out.println("No traders in the market.");
            return;
        }

        Collections.sort(traderList);

        for (String traderId : traderList) {
            System.out.println(traderId);
        }
    }

    public void tradesCommand() {
        List<Trade> trades = this.market.getTrades();

        if (trades.isEmpty()) {
            System.out.println("No trades have been completed.");
            return;
        }

        for (Trade trade : trades) {
            System.out.println(trade);
        }
    }

    public void tradesTrader(String id) {
        Trader trader = this.traders.get(id);

        if (trader == null) {
            System.out.println("No such trader in the market.");
            return;
        }

        List<Trade> trades = this.market.getTrades();

        List<Trade> involvedTrades = Market.filterTradesByTrader(trades, trader);

        if (involvedTrades == null || involvedTrades.isEmpty()) {
            System.out.println("No trades have been completed by trader.");
            return;
        }

        for (Trade trade : involvedTrades) {
            System.out.println(trade);
        }
    }

    public void tradesProduct(String product) {
        List<Trade> trades = this.market.getTrades();

        List<Trade> involvedTrades = Market.filterTradesByProduct(trades, product);

        if (involvedTrades == null || involvedTrades.isEmpty()) {
            System.out.println("No trades have been completed with given product.");
            return;
        }

        for (Trade trade : involvedTrades) {
            System.out.println(trade);
        }
    }

    public void bookSell() {
        List<Order> sellBook = this.market.getSellBook();

        if (sellBook.isEmpty()) {
            System.out.println("The sell book is empty.");
            return;
        }

        for (Order order : sellBook) {
            System.out.println(order);
        }
    }

    public void bookBuy() {
        List<Order> buyBook = this.market.getBuyBook();

        if (buyBook.isEmpty()) {
            System.out.println("The buy book is empty.");
            return;
        }

        for (Order order : buyBook) {
            System.out.println(order);
        }
    }

    public List<Trader> sortTrader() {
        ArrayList<String> traderIdList = new ArrayList<>(this.traders.keySet());

        Collections.sort(traderIdList);

        ArrayList<Trader> traderList = new ArrayList<>();

        for (String traderId : traderIdList) {
            traderList.add(this.traders.get(traderId));
        }

        return traderList;
    }

    public void save(String traderPath, String tradesPath) {
        List<Trader> traderList = sortTrader();

        Trader.writeTraders(traderList, traderPath);

        List<Trade> trades = this.market.getTrades();

        Trade.writeTrades(trades, tradesPath);
        System.out.println("Success.");
    }

    public void binary(String traderPath, String tradesPath) {
        List<Trader> traderList = sortTrader();

        Trader.writeTradersBinary(traderList, traderPath);

        List<Trade> trades = this.market.getTrades();

        Trade.writeTradesBinary(trades, tradesPath);
        System.out.println("Success.");
    }

    public void run() {
        while (true) {
            System.out.printf("$ ");
            String[] command = this.scan.nextLine().split(" ");

            if (command[0].toUpperCase().equals("EXIT")) {
                System.out.println("Have a nice day.");
                break;
            } else if (command[0].toUpperCase().equals("ADD")) {
                addCommand(command[1], command[2]);
            } else if (command[0].toUpperCase().equals("BALANCE")) {
                balance(command[1]);
            } else if (command[0].toUpperCase().equals("INVENTORY")) {
                inventory(command[1]);
            } else if (command[0].toUpperCase().equals("AMOUNT")) {
                amount(command[1], command[2]);
            } else if (command[0].toUpperCase().equals("SELL")) {
                sell(command[1], command[2],command[3], command[4]);
            } else if (command[0].toUpperCase().equals("BUY")) {
                buy(command[1], command[2],command[3], command[4]);
            } else if (command[0].toUpperCase().equals("IMPORT")) {
                importProduct(command[1], command[2],command[3]);
            } else if (command[0].toUpperCase().equals("EXPORT")) {
                exportProduct(command[1], command[2],command[3]);
            } else if (command[0].toUpperCase().equals("CANCEL")) {
                if (command[1].toUpperCase().equals("SELL")) {
                    cancelSell(command[2]);
                } else {
                    cancelBuy(command[2]);
                }
            } else if (command[0].toUpperCase().equals("ORDER")) {
                orderCommand(command[1]);
            } else if (command[0].toUpperCase().equals("TRADERS")) {
                traderCommand();
            } else if (command[0].toUpperCase().equals("TRADES")) {
                if (command.length > 1) {
                    if (command[1].toUpperCase().equals("TRADER")) {
                        tradesTrader(command[2]);
                    } else if (command[1].toUpperCase().equals("PRODUCT")) {
                        tradesProduct(command[2]);
                    }
                }
                 else {
                    tradesCommand();
                }
            } else if (command[0].toUpperCase().equals("BOOK")) {
                if (command[1].toUpperCase().equals("SELL")) {
                    bookSell();
                } else {
                    bookBuy();
                }
            } else if (command[0].toUpperCase().equals("SAVE")) {
                save(command[1], command[2]);
            } else if (command[0].toUpperCase().equals("BINARY")) {
                binary(command[1], command[2]);
            } else {
                continue;
            }
        }
    }

//    public void getOrderID() {
//        System.out.println(this.orderId);
//    }

    public static void main(String[] args) {
        Exchange exchange = new Exchange();
        exchange.run();
    }
}