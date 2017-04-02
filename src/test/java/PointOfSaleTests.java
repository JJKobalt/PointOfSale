import Exceptions.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by JanJa on 30.03.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class PointOfSaleTests {


    @Mock
    LcdDisplay display;

    @Mock
    ProductDataBase dataBase;

    @Mock
    Printer printer;


    @Test
    public void shouldManageSaleCorrectly() throws NotFoundException {

        PointOfSale pointOfSale = new PointOfSale(dataBase, display, printer);

        Product product1 = new Product("product1", new BigDecimal(10));
        Product product2 = new Product("product2", new BigDecimal(10.5));
        Product product3 = new Product("product3", new BigDecimal(20));

        List<Product> productList = new LinkedList<>();

        productList.add(product1);
        productList.add(product2);
        productList.add(product3);

        when(dataBase.search(anyString())).thenReturn(product1, product2, product3);
        pointOfSale.takeProductCode("1");
        pointOfSale.takeProductCode("2");
        pointOfSale.takeProductCode("3");
        pointOfSale.endSale();


        Mockito.verify(dataBase, Mockito.times(3)).search(anyString());
        Mockito.verify(display, Mockito.times(3)).displayProduct(Matchers.any(Product.class));
        Mockito.verify(printer, Mockito.times(1)).print(productList);
        Mockito.verify(display,Mockito.times(1)).displayTotalPrice(new BigDecimal(40.5));
    }


    @Test
    public void ErrorsShouldNotInterruptSale() throws NotFoundException {
        PointOfSale pointOfSale = new PointOfSale(dataBase, display, printer);

        Product product1 = new Product("product1", new BigDecimal(10));
        Product product2 = new Product("product2", new BigDecimal(10.5));
        Product product3 = new Product("product3", new BigDecimal(20));

        List<Product> productList = new LinkedList<>();

        productList.add(product1);
        productList.add(product2);
        productList.add(product3);

        when(dataBase.search(anyString())).thenReturn(product1, product2, product3);

        pointOfSale.takeProductCode("1");
        pointOfSale.takeProductCode("2");
        pointOfSale.takeProductCode(null);
        pointOfSale.takeProductCode("3");

        when(dataBase.search("404")).thenThrow(new NotFoundException());
        pointOfSale.takeProductCode("404");

        pointOfSale.endSale();


        Mockito.verify(dataBase, Mockito.times(4)).search(anyString());
        Mockito.verify(display, Mockito.times(3)).displayProduct(Matchers.any(Product.class));
        Mockito.verify(display, Mockito.times(1)).displayNotFound();
        Mockito.verify(display, Mockito.times(1)).displayInvalidBarCode();
        Mockito.verify(printer, Mockito.times(1)).print(productList);
        Mockito.verify(display,Mockito.times(1)).displayTotalPrice(new BigDecimal(40.5));
    }

    @Test
    public void shouldNotThrowsErrorsOnEmptyList() throws NotFoundException {

        PointOfSale pointOfSale = new PointOfSale(dataBase, display, printer);
        pointOfSale.endSale();

        Mockito.verify(dataBase, Mockito.never()).search(anyString());
        Mockito.verify(display, Mockito.never()).displayProduct(Matchers.any(Product.class));
        Mockito.verify(printer, Mockito.times(1)).print(new ArrayList<>());
        Mockito.verify(display,Mockito.times(1)).displayTotalPrice(new BigDecimal(0));

    }

    @Test
    public void shouldDisplayProductIfFound() throws NotFoundException {

        String productCode = "12345678";
        Product searchedProduct = new Product("searchedProduct", new BigDecimal(10));
        when(dataBase.search(productCode)).thenReturn(searchedProduct);

        PointOfSale pointOfSale = new PointOfSale(dataBase, display, printer);
        pointOfSale.takeProductCode(productCode);

        Mockito.verify(dataBase, Mockito.times(1)).search(productCode);
        Mockito.verify(display, Mockito.times(1)).displayProduct(searchedProduct);
    }


    @Test
    public void shouldDisplayNotFoundMessageIfNotFound() throws NotFoundException {
        String productCode = "12345678";

        when(dataBase.search(productCode)).thenThrow(new NotFoundException());

        PointOfSale pointOfSale = new PointOfSale(dataBase, display, printer);
        pointOfSale.takeProductCode(productCode);
        Mockito.verify(dataBase, Mockito.times(1)).search(productCode);
        Mockito.verify(display, Mockito.times(1)).displayNotFound();
        Mockito.verify(display, Mockito.never()).displayProduct(Mockito.any(Product.class));
    }


    @Test
    public void shouldDisplayInvalidBarCodeMessageIfInvalidCode() throws NotFoundException {
        String invalidCode = null;
        PointOfSale pointOfSale = new PointOfSale(dataBase, display, printer);
        pointOfSale.takeProductCode(invalidCode);

        Mockito.verify(dataBase, Mockito.never()).search(anyString());
        Mockito.verify(display, Mockito.times(1)).displayInvalidBarCode();
        Mockito.verify(display, Mockito.never()).displayProduct(Mockito.any(Product.class));
    }







}
