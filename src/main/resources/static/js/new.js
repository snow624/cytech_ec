const textarea = document.querySelector("textarea");
const counter = document.getElementById("count");

textarea.addEventListener("input", () => {
    counter.textContent = textarea.value.length + " / 300";
});