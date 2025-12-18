package jp.co.sss.cytech_ec.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.cytech_ec.form.PurchaseForm;
import jp.co.sss.cytech_ec.model.CartItem;
import jp.co.sss.cytech_ec.model.Product;
import jp.co.sss.cytech_ec.repository.ProductRepository;

@Controller
public class CartController {

    @Autowired
    private ProductRepository productRepository;

    // カートに追加
    
    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam Integer productId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) Boolean directBuy,
            HttpSession session) {

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return "redirect:/products";
        }

        // 既存商品チェック
        for (CartItem item : cart) {
            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                session.setAttribute("cart", cart);

                return Boolean.TRUE.equals(directBuy)
                        ? "redirect:/purchase"
                        : "redirect:/cart";
            }
        }

        // 新規追加（★ここが今回のポイント）
        CartItem newItem = new CartItem(product, quantity);
        cart.add(newItem);

        session.setAttribute("cart", cart);

        return Boolean.TRUE.equals(directBuy)
                ? "redirect:/purchase"
                : "redirect:/cart";
    }


      
    //カート内商品削除
    @PostMapping("/cart/delete")
    public String deleteCartItem(
            @RequestParam int index,
            HttpSession session) {

        List<CartItem> cart =
                (List<CartItem>) session.getAttribute("cart");

        if (cart != null && cart.size() > index) {
            cart.remove(index);
        }

        session.setAttribute("cart", cart);
        
    

        return "redirect:/cart";
    }
    
    
//カート内数量変更
    @PostMapping("/cart/update")
    public String updateCartItem(
            @RequestParam("index") int index,
            @RequestParam("quantity") int quantity,
            HttpSession session) {

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart != null && index >= 0 && index < cart.size()) {

            CartItem item = cart.get(index);

            // 数量を更新
            item.setQuantity(quantity);

            // 小計も更新
            int subtotal = item.getProduct().getPrice() * quantity;
            item.setSubtotal(subtotal);
        }

        // カートを保存し直す
        session.setAttribute("cart", cart);

        return "redirect:/cart";
    }
    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model) {

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        int total = 0;
        if (cart != null) {
            for (CartItem item : cart) {
                total += item.getSubtotal();
            }
        }

        model.addAttribute("cart", cart);
        model.addAttribute("total", total);

        return "cart";
    }
    
    @PostMapping("/purchase/start-from-cart")
    public String startPurchaseFromCart(HttpSession session) {

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }

        // 今回は「1商品想定」
        CartItem item = cart.get(0);

        PurchaseForm form = new PurchaseForm();
        form.setProductId(item.getProduct().getProductId());
        form.setQuantity(item.getQuantity());

        session.setAttribute("purchaseForm", form);

        return "redirect:/purchase";
    }
    
//    カート詳細画面
    
    @GetMapping("/cart/confirm")
    public String showCartConfirm(HttpSession session, Model model) {

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }

        int subtotal = 0;
        for (CartItem item : cart) {
            subtotal += item.getProduct().getPrice() * item.getQuantity();
        }
        int total = (int)(subtotal * 1.1);

        model.addAttribute("cart", cart);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("total", total);

        return "cart_confirm";
    }




}
