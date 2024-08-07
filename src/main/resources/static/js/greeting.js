document.addEventListener("DOMContentLoaded", function () {
  const firstName = localStorage.getItem("userFirstName");
  document.getElementById("greeting").textContent = `Wellcome ${firstName}`;
});
