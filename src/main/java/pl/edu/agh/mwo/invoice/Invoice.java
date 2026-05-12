package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private int invoiceNumber;
    private static int staticNumber = 0;


    public Invoice() {
        staticNumber ++;
        invoiceNumber = staticNumber;
    }

    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    private Map<Product, Integer> products = new HashMap<Product, Integer>();

    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException();
        }

        Integer q = products.getOrDefault(product, 0);
        products.put(product, q + quantity);
    }

    public Object getProductQuantity(Product product) {
        return products.getOrDefault(product, 0);
    }

    public BigDecimal getNetTotal() {
        BigDecimal totalNet = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalNet = totalNet.add(product.getPrice().multiply(quantity));
        }
        return totalNet;
    }

    public BigDecimal getTaxTotal() {
        return getGrossTotal().subtract(getNetTotal());
    }

    public BigDecimal getGrossTotal() {
        BigDecimal totalGross = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity));
        }
        return totalGross;
    }

    public String printProducts() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();

            sb.append(product.getName());
            sb.append(" | quantity: ").append(quantity);
            sb.append(" | price: ").append(product.getPrice());
            sb.append(" | priceWithTax: ").append(product.getPriceWithTax());
            sb.append("\n");
        }

        return sb.toString();
    }
}
