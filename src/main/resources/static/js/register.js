document.getElementById("apiForm").addEventListener("submit", function (event) {
  event.preventDefault();

  const formData = new FormData(this);
  const client = {
    firstName: formData.get("firstName"),
    lastName: formData.get("lastName"),
    age: formData.get("age"),
    telephone: formData.get("telephone"),
    email: formData.get("email"),
    password: formData.get("password"),
  };

  fetch("http://localhost:8080/client", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(client),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.json();
    })
    .then((data) => {
      console.log("Success:", data);
      alert("Welcome: " + data.client.firstName);
      localStorage.setItem("userEmail", data.client.email);
      localStorage.setItem("userFirstName", data.client.firstName);
      localStorage.setItem("userLastName", data.client.lastName);
      window.location.href =
        "http://127.0.0.1:5500/src/main/resources/static/html/home.html";
    })
    .catch((error) => {
      console.error("Error:", error);
    });
});
