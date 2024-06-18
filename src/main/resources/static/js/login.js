document
  .getElementById("formLogin")
  .addEventListener("submit", function (event) {
    event.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    fetch("http://localhost:8080/api/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email, password }),
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.success) {
          localStorage.setItem("userEmail", email);
          window.location.href =
            "http://127.0.0.1:5500/src/main/resources/static/html/home.html";
          console.log("Success:", data);
        } else {
          alert("Invalid email or password");
        }
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  });
