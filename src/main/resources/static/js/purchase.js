
	document.addEventListener("DOMContentLoaded", function() {

	    const addressRadios = document.querySelectorAll("input[name='addressId']");
	    const addressForm = document.getElementById("newAddressForm");

	    addressRadios.forEach(radio => {
	        radio.addEventListener("change", function() {
	            addressForm.style.display = (this.value == 0) ? "block" : "none";
	        });
	    });

	    const cardRadios = document.querySelectorAll("input[name='cardId']");
	    const cardForm = document.getElementById("newCardForm");

	    cardRadios.forEach(radio => {
	        radio.addEventListener("change", function() {
	            cardForm.style.display = (this.value == 0) ? "block" : "none";
	        });
	    });
	});
