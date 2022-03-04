import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Market {
    private HashMap<String, Order> sellBook;
    private HashMap<String, Order> buyBook;
    private ArrayList<Trade> trades;

    public Market() {
        this.sellBook = new HashMap<String, Order>();
        this.buyBook = new HashMap<String, Order>();
        this.trades = new ArrayList<Trade>();
    }

    public void orderExist(String orderId) {
        if (this.sellBook.isEmpty() && this.buyBook.isEmpty()) {
            System.out.println("No orders in either book in the market.");
            return;
        }

        if (this.sellBook.get(orderId) != null) {
            System.out.println(this.sellBook.get(orderId));
            return;
        }

        if (this.buyBook.get(orderId) != null) {
            System.out.println(this.buyBook.get(orderId));
            return;
        }

        System.out.println("Order is not present in either order book.");
    }

    public List<Order> sellPriceTimePriority (List<Order> orderList) {
        Collections.sort(orderList, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {

                Order orderOne = (Order) o1;
                Order orderTwo = (Order) o2;

                if (orderOne.getPrice() < orderTwo.getPrice()) {
                    return 1;
                } else if (orderOne.getPrice() == orderTwo.getPrice()) {
                    if (orderOne.getID().compareTo(orderTwo.getID()) > 0){
                        return 1;
                    }
                    return -1;
                } else {
                    return -1;
                }
            }
        });
        return orderList;
    }

    public List<Order> buyPriceTimePriority (List<Order> orderList) {
        Collections.sort(orderList, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {

                Order orderOne = (Order) o1;
                Order orderTwo = (Order) o2;

                if (orderOne.getPrice() > orderTwo.getPrice()) {
                    return 1;
                } else if (orderOne.getPrice() == orderTwo.getPrice()) {
                    if (orderOne.getID().compareTo(orderTwo.getID()) > 0){
                        return 1;
                    }
                    return -1;
                } else {
                    return -1;
                }
            }
        });
        return orderList;
    }

    public Trade trading(Order buyOrder, Order sellOrder, boolean isPlaceSell) {
        boolean sellBigger = true;
        double amount;
        double price;
        double unitPrice;

        if (sellOrder.getAmount() >= buyOrder.getAmount()) {
            amount = buyOrder.getAmount();

        } else {
            sellBigger = false;
            amount = sellOrder.getAmount();
        }

        if (isPlaceSell) {
            price = amount * buyOrder.getPrice();
            unitPrice = buyOrder.getPrice();
        } else {
            price = amount * sellOrder.getPrice();
            unitPrice = sellOrder.getPrice();
        }

        String product = sellOrder.getProduct();

        sellOrder.adjustAmount((-1) * amount);
        sellOrder.getTrader().adjustBalance(price);

        buyOrder.adjustAmount((-1) * amount);
        buyOrder.getTrader().importProduct(product, amount);
        buyOrder.getTrader().adjustBalance((-1) * price);

        Trade trade = new Trade(product, amount, unitPrice, sellOrder, buyOrder);
        this.trades.add(trade);

        if (sellBigger) {
            buyOrder.close();
            buyBook.remove(buyOrder.getID(), buyOrder);
            if (sellOrder.getAmount() == 0) {
                sellOrder.close();
                sellBook.remove(sellOrder.getID(), sellOrder);
            }

        } else {
            sellOrder.close();
            sellBook.remove(sellOrder.getID(), sellOrder);
        }

        return trade;
    }

    public List<Trade> placeSellOrder(Order order) {
        if (order == null || order.isBuy()) {
            return null;
        }

        String product = order.getProduct();

        if (order.getAmount() > order.getTrader().getAmountStored(product)) {
            return null;
        }

        order.getTrader().exportProduct(product, order.getAmount());

        List<Order> buyOrderList = new ArrayList<>();
        ArrayList<Trade> tradeList = new ArrayList<Trade>();

        for (String orderId : buyBook.keySet()) {
            if (buyBook.get(orderId).getProduct().equals(product)) {
                buyOrderList.add(buyBook.get(orderId));
            }
        }

        if (buyOrderList == null || buyOrderList.size() == 0) {
            sellBook.put(order.getID(), order);
            return tradeList;
        }

        sellPriceTimePriority(buyOrderList);

        for (Order buyOrder : buyOrderList) {
            if (buyOrder.getPrice() >= order.getPrice()) {
                tradeList.add(trading(buyOrder, order, true));

                if (order.isClosed()) {
                    return tradeList;
                }
            } else {
                break;
            }
        }

        sellBook.put(order.getID(), order);

        return tradeList;
    }

    public List<Trade> placeBuyOrder(Order order) {
        if (order == null || !order.isBuy()) {
            return null;
        }

        String product = order.getProduct();
        List<Order> sellOrderList = new ArrayList<>();
        ArrayList<Trade> tradeList = new ArrayList<Trade>();

        for (String orderId : sellBook.keySet()) {
            if (sellBook.get(orderId).getProduct().equals(product)) {
                sellOrderList.add(sellBook.get(orderId));
            }
        }

        if (sellOrderList == null || sellOrderList.size() == 0) {
            buyBook.put(order.getID(), order);
            return tradeList;
        }

        buyPriceTimePriority(sellOrderList);

        for (Order sellOrder : sellOrderList) {
            if (sellOrder.getPrice() <= order.getPrice()) {
                tradeList.add(trading(order, sellOrder, false));
                if (order.isClosed()) {
                    return tradeList;
                }
            } else {
                break;
            }
        }

        buyBook.put(order.getID(), order);

        return tradeList;
    }

    public boolean cancelBuyOrder(String order) {
        Order cancelOrder = buyBook.get(order);

        if (cancelOrder == null) {
            return false;
        }

        buyBook.remove(order, cancelOrder);

        cancelOrder.close();

        return cancelOrder.isClosed();
    }

    public boolean cancelSellOrder(String order) {
        Order cancelOrder = sellBook.get(order);

        if (cancelOrder == null) {
            return false;
        }

        sellBook.remove(order, cancelOrder);
        cancelOrder.getTrader().importProduct(cancelOrder.getProduct(), cancelOrder.getAmount());

        cancelOrder.close();

        return cancelOrder.isClosed();
    }

    public List<Order> getSellBook() {
        ArrayList<Order> orderList = new ArrayList<>();
        for (String order : sellBook.keySet()) {
            orderList.add(sellBook.get(order));
        }
        return orderList;
    }

    public List<Order> getBuyBook() {
        ArrayList<Order> orderList = new ArrayList<>();
        for (String order : buyBook.keySet()) {
            orderList.add(buyBook.get(order));
        }
        return orderList;
    }

    public List<Trade> getTrades() {
        return this.trades;
    }

    public static List<Trade> filterTradesByTrader(List<Trade> trades, Trader trader) {
        if (trades == null || trades.isEmpty() || trader == null) {
            return null;
        }

        ArrayList<Trade> tradeInvolved = new ArrayList<Trade>();

        for (Trade trade : trades) {
            if (trade.involvesTrader(trader)) {
                tradeInvolved.add(trade);
            }
        }

        return tradeInvolved;
    }

    public static List<Trade> filterTradesByProduct(List<Trade> trades, String product) {
        if (trades == null || trades.isEmpty() || product == null) {
            return null;
        }

        ArrayList<Trade> tradeInvolved = new ArrayList<Trade>();

        for (Trade trade : trades) {
            if (trade.getProduct().equals(product)) {
                tradeInvolved.add(trade);
            }
        }

        return tradeInvolved;
    }
}