
package jp.co.sss.cytech_ec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.cytech_ec.model.Product;
import jp.co.sss.cytech_ec.repository.ProductRepository;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    public String showProductList(Model model) {

        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        return "product/list";
    }
    
//    詳細表示
    @GetMapping("/products/detail")
    public String showProductDetail(Integer id, Model model) {

        Product product = productRepository.findById(id).orElse(null);
        model.addAttribute("product", product);

        return "product/detail";
    }
//画面表示用    
    @GetMapping("/purchase")
    public String showPurchase() {
        return "purchase";
    }

//    購入確認画面
    @PostMapping("/purchase/confirm")
    public String showPurchaseConfirm(
            @RequestParam String name,
            @RequestParam String address,
            @RequestParam String apartment,
            @RequestParam String card,
            Model model) {

        model.addAttribute("name", name);
        model.addAttribute("address", address);
        model.addAttribute("apartment", apartment);
        model.addAttribute("card", card);
        model.addAttribute("total", 10000); // 仮の合計金額

        return "purchase_confirm";
    }

//    購入完了画面

    @PostMapping("/purchase/complete")
    public String showComplete() {
        return "complete";
    }
    
    @GetMapping("/purchase/complete")
    public String showCompletePage() {
        return "complete";
    }


}
