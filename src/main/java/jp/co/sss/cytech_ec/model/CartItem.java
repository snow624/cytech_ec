package jp.co.sss.cytech_ec.model;

//カートの商品＋個数＋小計を表示
public class CartItem {

    private Product product;
    private int quantity;
    private int subtotal;

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
        int taxPrice = (int) Math.round(product.getPrice() * 1.1); // 税込価格
        return taxPrice * quantity;
    }

    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }
}
