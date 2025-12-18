package jp.co.sss.cytech_ec.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.cytech_ec.model.Address;
import jp.co.sss.cytech_ec.model.User;
import jp.co.sss.cytech_ec.repository.AddressRepository;

@Controller
@RequestMapping("/address")
public class AddressController {

    @Autowired
    AddressRepository addressRepository;

    @GetMapping("/list")
    public String showAddressList(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        


        return "address/list";
    }

    @GetMapping("/add")
    public String showAddForm() {
        return "address/add";
    }

    @PostMapping("/add")
    public String addAddress(
            @RequestParam String postalCode,
            @RequestParam String prefecture,
            @RequestParam String city,
            @RequestParam String detail,
            HttpSession session) {

        User user = (User) session.getAttribute("user");

        Address address = new Address();
        address.setUser(user);
        address.setPostalCode(postalCode);
        address.setPrefecture(prefecture);
        address.setCity(city);
        address.setDetail(detail);
        

        addressRepository.save(address);

        return "redirect:/address/list";
    }
    
    

}
