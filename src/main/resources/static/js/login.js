document
  .getElementById("formLogin")
  .addEventListener("submit", function (event) {
    event.preventDefault();

    let email = document.getElementById("email").value;
    let password = document.getElementById("password").value;

    fetch("http://localhost:8080/login", {
      method: "POST",
      headers: new Headers({
        "Content-Type": "application/json; charset=utf8",
        Accept: "application/json",
      }),
      body: JSON.stringify({
        email: email,
        password: password,
      }),
    })
      .then((response) => {
        if (!response.ok) {
          return response.json().then((err) => {
            throw new Error(err.message);
          });
        }
        let token = response.headers.get("Authorization");
        return token;
      })
      .then((token) => {
        localStorage.setItem("userEmail", email);
        localStorage.setItem("Authorization", token);
        window.location.href =
          "http://127.0.0.1:5500/src/main/resources/templates/home.html";
      })
      .catch((error) => {
        console.error("Error:", error);
        alert(error.message || "Invalid credentials. Please try again.");
      });
  });
