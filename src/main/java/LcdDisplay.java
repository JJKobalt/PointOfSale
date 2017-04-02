import java.math.BigDecimal;

/**
 * Created by JanJa on 02.04.2017.
 */
public interface LcdDisplay {
    void displayProduct(Product any);

    void displayNotFound();

    void displayInvalidBarCode();

    void displayTotalPrice(BigDecimal totalPrice);
}
