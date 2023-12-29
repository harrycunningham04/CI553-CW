package catalogue;
import java.util.Currency;
import java.util.Locale;

/**
 * Write a description of class BetterBasket here.
 * Simplifies the implementation of getDetails method
 * 
 * @author  Harry Cunningham
 * @version 1.0
 */
public class BetterBasket extends Basket
{

  private int orderNum = 0; // Order number
    /**
     * Constructor for a basket which is used to represent a customer order/wish list
     */
    public BetterBasket() {
        orderNum = 0;
    }

    /**
     * Returns the customers unique order number
     * @return the customers order number
     */
    public int getOrderNum() {
        return orderNum;
    }

    /**
     * Add a product to the Basket.
     * Product is appended to the end of the existing products in the basket.
     * @param product A product to be added to the basket
     * @return true if successfully adds the product
     */
    @Override
    public boolean add(Product product) {
        return super.add(product); // Call add in ArrayList
    }

    /**
     * Returns a description of the products in the basket suitable for printing.
     * @return a string description of the basket products
     */
    public String getDetails() {
        Locale uk = Locale.UK;
        StringBuilder sb = new StringBuilder(256);
        String csign = Currency.getInstance(uk).getSymbol();
        double total = 0.00;

        if (orderNum != 0) {
            sb.append(String.format("Order number: %03d\n", orderNum));
        }

        if (!isEmpty()) {
            for (Product product : this) {
                int number = product.getQuantity();
                sb.append(String.format("%-7s%-14.14s (%3d) %s%7.2f\n",
                        product.getProductNum(),
                        product.getDescription(),
                        number,
                        csign,
                        product.getPrice() * number));
                total += product.getPrice() * number;
            }
            sb.append("----------------------------\n");
            sb.append(String.format("Total                       %s%7.2f\n", csign, total));
        }

        return sb.toString();
    }
}
