package jp.co.sss.cytech_ec.controller;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.cytech_ec.form.PurchaseForm;
import jp.co.sss.cytech_ec.model.Address;
import jp.co.sss.cytech_ec.model.Card;
import jp.co.sss.cytech_ec.model.CartItem;
import jp.co.sss.cytech_ec.model.Product;
import jp.co.sss.cytech_ec.model.Review;
import jp.co.sss.cytech_ec.model.User;
import jp.co.sss.cytech_ec.repository.AddressRepository;
import jp.co.sss.cytech_ec.repository.CardRepository;
import jp.co.sss.cytech_ec.repository.ProductRepository;
import jp.co.sss.cytech_ec.repository.ReviewRepository;
import jp.co.sss.cytech_ec.repository.UserRepository;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CardRepository cardRepository;


    // ============================
    // 商品一覧
    // ============================
    @GetMapping("/products")
    public String showProductList(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "products/list";
    }


    // ============================
    // 商品詳細
    // ============================
    @GetMapping("/products/detail")
    public String showDetail(@RequestParam("id") Integer id, Model model) {

        Product product = productRepository.findById(id).orElse(null);
        model.addAttribute("product", product);

        List<Review> reviews = reviewRepository.findByProduct_ProductId(id);
        model.addAttribute("reviews", reviews);

        return "products/detail";
    }
    


    // ============================
    // 口コミ登録
    // ============================
    @PostMapping("/reviews/add")
    public String addReview(@ModelAttribute Review review) {

        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);

        return "redirect:/products";
    }

    
    @GetMapping("/reviews/new")
    public String showReviewForm(
            @RequestParam Integer productId,
            Model model) {

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return "redirect:/products";
        }

        Review review = new Review();
        review.setProduct(product);

        model.addAttribute("product", product);
        model.addAttribute("review", review);

        return "reviews/new";
    }



    // ============================
    // 購入情報入力画面
    // ============================
    @GetMapping("/purchase")
    public String showPurchasePage(Model model, HttpSession session) {

        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/login";
        }

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }

        PurchaseForm form = new PurchaseForm();
        session.setAttribute("purchaseForm", form);

        model.addAttribute("purchaseForm", form);
        model.addAttribute("addressList", addressRepository.findByUser(loginUser));
        model.addAttribute("cardList", cardRepository.findByUser(loginUser));

        return "purchase";
    }



    

    @PostMapping("/purchase/start")
    public String startPurchase(
            @RequestParam Integer productId,
            @RequestParam Integer quantity,
            HttpSession session) {

        PurchaseForm form = new PurchaseForm();
        form.setProductId(productId);
        form.setQuantity(quantity);
        
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/login";
        }

        session.setAttribute("purchaseForm", form);

        return "redirect:/purchase";
    }




 // ===============================
 // 住所登録
 // ===============================
 @PostMapping("/purchase/address/save")
 public String saveAddress(@ModelAttribute Address newAddress, HttpSession session) {

     User loginUser = (User) session.getAttribute("user");
     if (loginUser == null) {
         return "redirect:/login";
     }

     newAddress.setUser(loginUser);
     addressRepository.save(newAddress);

     return "redirect:/purchase";
 }

 // ===============================
 // カード登録
 // ===============================
 @PostMapping("/purchase/card/save")
 public String saveCard(@ModelAttribute Card newCard, HttpSession session) {

     User loginUser = (User) session.getAttribute("user");
     if (loginUser == null) {
         return "redirect:/login";
     }

     newCard.setUser(loginUser);
     cardRepository.save(newCard);

     return "redirect:/purchase";
 }



    // ============================
    // 購入確認画面
    // ============================
 @PostMapping("/purchase/confirm")
 public String purchaseConfirm(
         @ModelAttribute PurchaseForm form,
         HttpSession session,
         Model model
 ) {
     User loginUser = (User) session.getAttribute("user");
     if (loginUser == null) {
         return "redirect:/login";
     }

     // 住所文字列を作る
     String address;
     if (form.getAddressId() == 0) {
         address = form.getPostalCode() + " " +
                   form.getPrefecture() + " " +
                   form.getCity() + " " +
                   form.getDetail();
     } else {
         Address addr = addressRepository.findById(form.getAddressId()).orElse(null);
         if (addr == null) {
             return "redirect:/purchase";
         }
         address = addr.fullAddress();
     }

     // カート or 単品
     List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
     CartItem singleItem = (CartItem) session.getAttribute("singleItem");

     int total = 0;

     if (singleItem != null) {
         total = singleItem.getProduct().getPrice() * singleItem.getQuantity();
         model.addAttribute("items", List.of(singleItem));
     } else if (cart != null && !cart.isEmpty()) {
         for (CartItem item : cart) {
             total += item.getProduct().getPrice() * item.getQuantity();
         }
         model.addAttribute("items", cart);
     } else {
         return "redirect:/cart";
     }

     int taxTotal = (int) (total * 1.1);

     model.addAttribute("total", taxTotal);
     model.addAttribute("address", address);

     // complete 用に保存
     session.setAttribute("purchaseForm", form);
     session.setAttribute("addressText", address);

     return "purchase_confirm";
 }


 @GetMapping("/purchase/confirm")
 public String showConfirmPage(HttpSession session, Model model) {

     PurchaseForm form = (PurchaseForm) session.getAttribute("purchaseForm");
     if (form == null) {
         return "redirect:/products";
     }

     // 商品
     Product product = productRepository.findById(form.getProductId()).orElse(null);

     // 合計計算
     int subtotal = product.getPrice() * form.getQuantity();
     int total = (int)(subtotal * 1.1);

     // 住所（新規か既存か）
     String address;
     if (form.getAddressId() == 0) {
         address = form.getPostalCode() + " " + form.getPrefecture() + 
                   " " + form.getCity() + " " + form.getDetail();
     } else {
         Address addr = addressRepository.findById(form.getAddressId()).orElse(null);
         address = addr.fullAddress(); 
     }

     // 値を渡す
     model.addAttribute("purchaseForm", form);
     model.addAttribute("product", product);
     model.addAttribute("subtotal", subtotal);
     model.addAttribute("total", total);
     model.addAttribute("address", address);

     return "purchase_confirm";
 }




//============================
//購入完了
//============================

 @PostMapping("/purchase/complete")
 public String completePurchase(HttpSession session, Model model) {

     User loginUser = (User) session.getAttribute("user");
     if (loginUser == null) {
         return "redirect:/login";
     }
     
  // ============================
  // 新規住所登録
  // ============================
     PurchaseForm form = (PurchaseForm) session.getAttribute("purchaseForm");
     if (form == null) {
         return "redirect:/purchase";
     }

  if (form.getAddressId() == 0) {

      Address newAddr = new Address();
      newAddr.setUser(loginUser); 
      newAddr.setPostalCode(form.getPostalCode());
      newAddr.setPrefecture(form.getPrefecture());
      newAddr.setCity(form.getCity());
      newAddr.setDetail(form.getDetail());

      addressRepository.save(newAddr);
  }

//============================
//新規カード登録
//============================
if (form.getCardId() != null && form.getCardId() == 0) {

   Card newCard = new Card();
   newCard.setUser(loginUser);  
   newCard.setCardNumber(form.getCardNumber());
   newCard.setExpiryMonth(form.getExpiryMonth());
   newCard.setExpiryYear(form.getExpiryYear());

   cardRepository.save(newCard);
}


     List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
     CartItem singleItem = (CartItem) session.getAttribute("singleItem");

     String address = (String) session.getAttribute("addressText");

     if (address == null) {
         return "redirect:/purchase";
     }

     List<CartItem> items;
     if (singleItem != null) {
         items = List.of(singleItem);
     } else if (cart != null && !cart.isEmpty()) {
         items = cart;
     } else {
         return "redirect:/products";
     }

     int total = 0;
     for (CartItem item : items) {
         total += item.getProduct().getPrice() * item.getQuantity();
     }
     int taxTotal = (int) (total * 1.1);

     model.addAttribute("items", items);
     model.addAttribute("total", taxTotal);
     model.addAttribute("address", address);

     // セッション掃除
     session.removeAttribute("cart");
     session.removeAttribute("singleItem");
     session.removeAttribute("purchaseForm");
     session.removeAttribute("addressText");

     return "complete";
 }


    // ============================
    // 商品検索
    // ============================
    @GetMapping("/products/search")
    public String searchProducts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "maker", required = false) String maker,
            Model model) {

        List<Product> products = productRepository.findByKeywordAndMaker(keyword, maker);

        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("maker", maker);

        return "products/list";
    }


    // ============================
    // 単品購入（確認画面へ）
    // ============================
    @PostMapping("/purchase/single")
    public String singlePurchase(
            @RequestParam Integer productId,
            @RequestParam Integer quantity,
            Model model,
            HttpSession session) {
    	
    	User loginUser = (User) session.getAttribute("user");
    	if (loginUser == null) {
    	    return "redirect:/login";
    	}

        Product product = productRepository.findById(productId).orElse(null);


        if (product == null) {
            return "redirect:/products";
        }

        int subtotal = product.getPrice() * quantity;
        int taxIncluded = (int) (subtotal * 1.1);

        //商品情報セット
        PurchaseForm form = new PurchaseForm();
        form.setProductId(productId);
        form.setQuantity(quantity);
        
        if (loginUser != null) {
            form.setName(loginUser.getUserName());
        }
        
        session.setAttribute("purchaseForm", form);

        model.addAttribute("product", product);
        model.addAttribute("quantity", quantity);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("taxIncluded", taxIncluded);

        return "purchase/single_confirm";
    }


}
