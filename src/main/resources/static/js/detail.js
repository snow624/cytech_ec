document.addEventListener("DOMContentLoaded", () => {
    const qtySelect = document.getElementById("qtySelect");

    // カート用
    const cartQty = document.getElementById("cartQuantity");
    if (cartQty) {
        cartQty.value = qtySelect.value;
    }

    // 今すぐ購入用
    const singleForm = document.querySelector(".single-buy-form");
    if (singleForm) {
        singleForm.addEventListener("submit", () => {
            document.getElementById("singleBuyQuantity").value = qtySelect.value;
        });
    }
});
