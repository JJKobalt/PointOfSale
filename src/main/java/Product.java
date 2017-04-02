import java.math.BigDecimal;

/**
 * Created by JanJa on 02.04.2017.
 */
public class Product {
    private String name;
    private BigDecimal price;


    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
