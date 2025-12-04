package jp.co.sss.cytech_ec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.cytech_ec.model.User;
import jp.co.sss.cytech_ec.repository.UserRepository;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public String showUserList(Model model) {

        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        return "users";
    }
    
//    登録画面
    @GetMapping("/users/add")
    public String showAddForm() {
        return "users/add";
    }
    
//    登録後は一覧へ戻る
    @PostMapping("/users/add")
    public String addUser(User user) {
        userRepository.save(user);
        return "redirect:/users";
    }
    
 /// ログイン画面表示
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    // ログイン処理
    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            Model model) {

        User user = userRepository.findByEmailAndPasswords(email, password);

        if (user == null) {
            model.addAttribute("error", "メールアドレスまたはパスワードが違います");
            return "login";
        }

        return "redirect:/top";
    }


}
