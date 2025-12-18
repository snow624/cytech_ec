package jp.co.sss.cytech_ec.form;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class PurchaseForm {

    @NotBlank(message = "名前を入力してください")
    private String name;

    // ========= 住所 =========
    private Integer addressId; // 0 = 新規

    private String postalCode;
    private String prefecture;
    private String city;
    private String detail;

    // ========= カード =========
    private Integer cardId; // 0 = 新規

    private String cardNumber;
    private String expiryMonth;
    private String expiryYear;

    // ========= 商品 =========
    private Integer productId;
    private Integer quantity;

    // ===============================
    // ▼ 新規住所チェック
    // ===============================
    @AssertTrue(message = "新しい住所を入力してください")
    public boolean isValidNewAddress() {
        if (addressId == null || addressId != 0) {
            return true; // 既存住所ならOK
        }
        return notBlank(postalCode)
            && notBlank(prefecture)
            && notBlank(city)
            && notBlank(detail);
    }

    // ===============================
    // ▼ 新規カードチェック
    // ===============================
    @AssertTrue(message = "新しいカード情報を正しく入力してください")
    public boolean isValidNewCard() {
        if (cardId == null || cardId != 0) {
            return true; // 既存カードならOK
        }
        return notBlank(cardNumber)
            && notBlank(expiryMonth)
            && notBlank(expiryYear);
    }

    // 共通チェック
    private boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }
}
