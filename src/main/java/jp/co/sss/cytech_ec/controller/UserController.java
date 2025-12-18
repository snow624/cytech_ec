package jp.co.sss.cytech_ec.controller;

import java.time.LocalDateTime;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.co.sss.cytech_ec.form.UserForm;
import jp.co.sss.cytech_ec.model.User;
import jp.co.sss.cytech_ec.repository.UserRepository;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // ユーザー一覧
    @GetMapping("/users")
    public String showUserList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }

    // 新規登録画面（GET）
    @GetMapping("/users/add")
    public String showAddForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "users/add";
    }

 // 登録処理（POST）
    @PostMapping("/users/add")
    public String addUser(
    		
    		
            @Valid @ModelAttribute("userForm") UserForm userForm,
            BindingResult result,
            Model model) {
    	System.out.println("--- デバッグ ---");
            System.out.println("userName = " + userForm.getUserName());
            System.out.println("email = " + userForm.getEmail());
            System.out.println("password = " + userForm.getPassword());
            System.out.println("passwordConfirm = " + userForm.getPasswordConfirm());



        // パスワード確認チェック
    	if (!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
            result.rejectValue("passwordConfirm", "error.passwordConfirm", "パスワードが一致しません");
        }

        // エラーがある場合は戻る
        if (result.hasErrors()) {
            return "users/add";
        }

        // DBへ保存
        User user = new User();
        user.setUserName(userForm.getUserName());
        user.setEmail(userForm.getEmail());
        user.setPasswords(userForm.getPassword());
        
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        // 登録後はログイン画面へ
        return "redirect:/login";
    }


    // ログイン画面（GET）
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    // ログイン処理（POST）
    @PostMapping("/login")
    public String login(
            String email,
            String password,
            Model model,
            HttpSession session) {

        User user = userRepository.findByEmailAndPasswords(email, password);

        if (user == null) {
            model.addAttribute("error", "メールアドレスまたはパスワードが違います");
            return "login";
        }

        // セッションにユーザー情報と userId を保存
        session.setAttribute("user", user);
        session.setAttribute("userId", user.getUserId());

        return "redirect:/top";
    }


    // ログアウト
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
