import java.util.HashMap;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        Trader trader1 = new Trader("001", 500);
        Trader trader2 = new Trader("002", 500);
        Trader trader3 = new Trader("003", 500);

        trader1.importProduct("Book1", 30);
        trader1.importProduct("Book2", 30);

        // amount price
        Order sellOrder1 = new Order("Book1", false, 29, 10, trader1, "0000");
        Order buyOrder1 = new Order("Book1", true, 10, 10, trader2, "0001");
        Order buyOrder2 = new Order("Book1", true, 10, 15, trader3, "0002");

        Order buyOrder3 = new Order("Book1", true, 10, 30, trader2, "0003");

        Order sellOrder2 = new Order("Book2", false, 19, 10, trader1, "0004");

        Order buyOrder4 = new Order("Book2", true, 10, 10, trader3, "0005");
        Order buyOrder5 = new Order("Book2", true, 10, 20, trader2, "0006");
        Order buyOrder6 = new Order("Book3", true, 10, 10, trader2, "0007");

        /**
         * sellbook
         *
         * buybook 0001 0007
         * **/


        Market market = new Market();
        List<Trade> tradesSell1 = market.placeSellOrder(sellOrder1);
        List<Trade> trades1 = market.placeBuyOrder(buyOrder1);
        List<Trade> trades2 = market.placeBuyOrder(buyOrder2);



        List<Trade> trades3 = market.placeBuyOrder(buyOrder3);

        List<Trade> tradesSell2 = market.placeSellOrder(sellOrder2);

        List<Trade> trades4 = market.placeBuyOrder(buyOrder4);

        List<Trade> trades5 = market.placeBuyOrder(buyOrder5);

        List<Trade> trades6 = market.placeBuyOrder(buyOrder6);

        for (String orderName : market.sellBook.keySet()) {
            System.out.println(market.sellBook.get(orderName));
            market.sellBook.remove(orderName);
        }


        System.out.println("Trades1:");
        if (trades1 != null) {
            for (Trade trade : trades1) {
                System.out.println(trade);
            }
        }


        System.out.println("Trades2:");
        if (trades2 != null) {
            for (Trade trade : trades2) {
                System.out.println(trade);
            }
        }

        System.out.println("tradesSell1:");
        if (tradesSell1 != null) {
            for (Trade trade : tradesSell1) {
                System.out.println(trade);
            }
        }

        System.out.println("Trades3:");
        if (trades3 != null) {
            for (Trade trade : trades3) {
                System.out.println(trade);
            }
        }

        System.out.println("Trades4:");
        if (trades4 != null) {
            for (Trade trade : trades4) {
                System.out.println(trade);
            }
        }

        System.out.println("tradesSell2:");
        if (tradesSell2 != null) {
            for (Trade trade : tradesSell2) {
                System.out.println(trade);
            }
        }

        System.out.println("Trades5:");
        if (trades5 != null) {
            for (Trade trade : trades5) {
                System.out.println(trade);
            }
        }

        System.out.println("Trades6:");
        if (trades6 != null) {
            for (Trade trade : trades6) {
                System.out.println(trade);
            }
        }


        System.out.println("SellBook:");
        market.getSellBook();
//        for (String orderName : market.sellBook.keySet()) {
//            System.out.println(market.sellBook.get(orderName));
//        }

        System.out.println("BuyBook:");
        market.getBuyBook();
//        for (String orderName : market.buyBook.keySet()) {
//            System.out.println(market.buyBook.get(orderName));
//        }


    }
}