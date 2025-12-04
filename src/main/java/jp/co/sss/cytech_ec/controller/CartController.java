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
            HttpSession session) {

        // セッションからカート取得
        List<CartItem> cart =
                (List<CartItem>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
        }

        Product product =
                productRepository.findById(productId).orElse(null);

        cart.add(new CartItem(product, 1));

        session.setAttribute("cart", cart);

        return "redirect:/cart";
    }

    // カート画面表示
    @GetMapping("/cart")
    public String showCart(Model model, HttpSession session) {

        List<CartItem> cart =
                (List<CartItem>) session.getAttribute("cart");

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

}
