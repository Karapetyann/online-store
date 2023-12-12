package onlineStore.storeStorage;



import onlineStore.model.Product;

import java.io.Serializable;
import java.util.HashSet;

import java.util.Set;

public class ProductStorage implements Serializable {

    private Set<Product> productSet = new HashSet<>();
    private Exception OutOfStockException;


    public void printProducts() {
        for (Product product : productSet) {
            System.out.println(product);
        }
    }

    public Product getById(String productId) {
        for (Product product : productSet) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    public void deleteStockQty(Product product, int qty) {
        product.setStockQty(product.getStockQty() - qty);
    }

    public boolean removeProductById(String productId) {
        Product product = getById(productId);
        return productSet.remove(product);
    }


    public void addProduct(Product product1) {
        productSet.add(product1);
    }
}
