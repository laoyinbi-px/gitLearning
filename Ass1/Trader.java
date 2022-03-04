import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.io.*;

public class Trader {
    private String id;
    private double balance;
    private HashMap<String, Double> products;

    public Trader(String id, double balance) {
        this.id = id;
        this.balance = balance;
        this.products = new HashMap<String, Double>();
    }

    public String getID() {
        return this.id;
    }

    public double getBalance() {
        return this.balance;
    }

    public double importProduct(String product, double amount) {
        if (product == null || amount <= 0) {
            return -1.0;
        }

        if (this.products.get(product) == null) {
            this.products.put(product, amount);
        } else {
            double newAmount = this.products.get(product) + amount;
            this.products.replace(product, newAmount);
        }

        return this.products.get(product);
    }

    public double exportProduct(String product, double amount) {
        if (product == null || amount <= 0) {
            return -1.0;
        }

        if (this.products.get(product) == null) {
            return -1.0;
        } else {
            double newAmount = this.products.get(product) - amount;
            if (newAmount > 0) {
                this.products.replace(product, newAmount);
            } else if (newAmount == 0) {
                this.products.remove(product);
            } else {
                return -1.0;
            }
            return newAmount;
        }
    }

    public double getAmountStored(String product) {
        if (product == null || product.length() <= 0) {
            return -1.0;
        }
        if (this.products.get(product) == null) {
            return 0.0;
        }
        double amount = this.products.get(product);
        return amount;
    }

    public List<String> getProductsInInventory() {
        ArrayList<String> productList = new ArrayList<>(this.products.keySet());

        Collections.sort(productList);

        return productList;
    }

    public double adjustBalance(double change) {
        this.balance += change;
        return this.balance;
    }

    public String toString() {
        String output = new String();
        String balance = String.format("%.2f",this.balance);
        output += this.id + ": $" + balance + " {";
        List<String> sortedList = this.getProductsInInventory();

        for (String productName : sortedList) {
            String amount = String.format("%.2f", this.products.get(productName));
            output += productName + ": " + amount + ", ";
        }

        if (output.charAt(output.length() - 2) == ',') {
            output = output.substring(0, output.length() - 2);
        }

        output += "}";

        return output;
    }

    public static void writeTraders(List<Trader> traders, String path) {
        if (traders == null || traders.isEmpty()) {
//            System.out.println("Unable to save logs to file.");
            return;
        }

        if (path == null || path.isEmpty()) {
//            System.out.println("Unable to save logs to file.");
            return;
        }

        try {
            PrintWriter pw = new PrintWriter(path);
            for (Trader trader : traders) {
                pw.println(trader.toString());
            }
            pw.close();

        } catch (FileNotFoundException e) {
            return;
        }
//        System.out.println("Success.");
    }

    public static void writeTradersBinary(List<Trader> traders, String path) {
        if (traders == null || traders.isEmpty()) {
//            System.out.println("Unable to save logs to file.");
            return;
        }

        if (path == null || path.isEmpty()) {
//            System.out.println("Unable to save logs to file.");
            return;
        }

        try{
            FileOutputStream f = new FileOutputStream(path);
            DataOutputStream output = new DataOutputStream(f);

            for (Trader trader : traders) {
                output.writeUTF(trader.toString());
                output.writeUTF("\u001F");
            }
            output.close();

        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            return;
        }
//        System.out.println("Success.");
    }
}