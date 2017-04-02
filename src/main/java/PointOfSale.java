import Exceptions.InvalidBarCode;
import Exceptions.NotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by JanJa on 30.03.2017.
 */
public class PointOfSale {

    private ProductDataBase dataBase;
    LcdDisplay display;

    private Printer printer;
    private List<Product> saleList;


    public PointOfSale(ProductDataBase dataBase, LcdDisplay display, Printer printer) {
        this.dataBase = dataBase;
        this.display = display;
        this.printer = printer;
        saleList = new ArrayList<>();
    }

    public void takeProductCode(String productCode) {


        try {
            isProductCodeInvalid(productCode);
            sell(productCode);
        } catch (NotFoundException e) {
            display.displayNotFound();
        } catch (InvalidBarCode invalidBarCode) {
            display.displayInvalidBarCode();
        }
    }

    private void sell(String productCode) throws InvalidBarCode, NotFoundException {
        Product product = dataBase.search(productCode);
        saleList.add(product);
        display.displayProduct(product);
    }

    public void endSale() {

        printer.print(saleList);
        BigDecimal totalPrice = getTotalPrice();
        display.displayTotalPrice(totalPrice);
    }


    private void isProductCodeInvalid(String productCode) throws InvalidBarCode {

        if (productCode == null) throw new InvalidBarCode();

    }

    public BigDecimal getTotalPrice() {

        Iterator<Product> it = saleList.iterator();

        BigDecimal totalPrice = new BigDecimal(0);


        while (it.hasNext()) {
            Product product = it.next();
            totalPrice = totalPrice.add(product.getPrice());
        }

        return totalPrice;
    }
}
