document.addEventListener("DOMContentLoaded", function () {
  document.getElementById(
    "greeting"
  ).textContent = `Wellcome ${localStorage.getItem("userFirstName")}`;
});
