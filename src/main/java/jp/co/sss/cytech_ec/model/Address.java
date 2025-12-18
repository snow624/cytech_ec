package jp.co.sss.cytech_ec.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "addresses")
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    public String fullAddress() {
        return postalCode + " " + prefecture + " " + city + " " + detail;
    }

    private String postalCode;   // 郵便番号
    private String prefecture;   // 都道府県
    private String city;         // 市区町村
    private String detail;       // 番地・建物名
    private String phoneNumber;  // 電話番号
}
