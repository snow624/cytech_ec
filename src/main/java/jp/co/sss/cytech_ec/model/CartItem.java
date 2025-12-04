package jp.co.sss.cytech_ec.model;

//カートの商品＋個数＋小計を表示
public class CartItem {

    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getSubtotal() {
        return product.getPrice() * quantity;
    }
}
