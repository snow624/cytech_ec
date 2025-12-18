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
@Table(name = "cards")
@Data
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cardId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // ダミーのカード情報
    private String cardNumber;  // 15桁
   
    private String expiryMonth; // 有効期限（月）
    private String expiryYear;  // 有効期限（年）
}
