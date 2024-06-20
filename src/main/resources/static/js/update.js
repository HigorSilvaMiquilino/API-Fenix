let id;

document.addEventListener("DOMContentLoaded", function () {
  const email = localStorage.getItem("userEmail");

  if (email) {
    fetch(`http://localhost:8080/client/email/${email}`)
      .then((response) => response.json())
      .then((data) => {
        document.getElementById("firstName").value = data.firstName;
        document.getElementById("lastName").value = data.lastName;
        document.getElementById("age").value = data.age;
        document.getElementById("telephone").value = data.telephone;
        document.getElementById("email").value = data.email;
        document.getElementById("password").value = data.password;
        id = data.id;
      })
      .catch((error) => console.error("Error fetching client data: " + error));
  }
});

document
  .getElementById("apiFormUpdate")
  .addEventListener("submit", function (event) {
    event.preventDefault();

    const formData = new FormData(this);
    const clientUpdated = {
      firstName: formData.get("firstName"),
      lastName: formData.get("lastName"),
      age: formData.get("age"),
      telephone: formData.get("telephone"),
      email: formData.get("email"),
      password: formData.get("password"),
    };

    fetch(`http://localhost:8080/client/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(clientUpdated),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        alert(data.message);
        localStorage.setItem("userEmail", data.client.email);
        localStorage.setItem("userFirstName", data.client.firstName);
        localStorage.setItem("userLastName", data.client.lastName);
        window.location.href =
          "http://127.0.0.1:5500/src/main/resources/static/html/home.html";
      })
      .catch((error) => console.error("Error updating client: " + error));
  });
