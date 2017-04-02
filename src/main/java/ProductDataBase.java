import Exceptions.NotFoundException;

/**
 * Created by JanJa on 02.04.2017.
 */
public interface ProductDataBase {
    Product search(String s) throws NotFoundException;
}
