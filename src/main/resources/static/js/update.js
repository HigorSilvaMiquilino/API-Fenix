let id;
let email;

document.addEventListener("DOMContentLoaded", function () {
  email = localStorage.getItem("userEmail");

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

const firstNameUpdateFeedback = document.getElementById(
  "firstNameUpdateFeedback"
);

const lastNameUpdateFeedback = document.getElementById(
  "lastNameUpdateFeedback"
);

const ageUpdateFeedback = document.getElementById("ageUpdateFeedback");

const telephoneUpdateFeedback = document.getElementById(
  "telephoneUpdateFeedback"
);

const emailUpdateFeedback = document.getElementById("emailUpdateFeedback");

const passwordUpdateFeedback = document.getElementById(
  "passwordUpdateFeedback"
);

function validateUpdateForm() {
  const firstName = document.getElementById("firstName").value;
  const lastName = document.getElementById("lastName").value;
  const age = document.getElementById("age").value;
  const telephone = document.getElementById("telephone").value;
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  firstNameUpdateFeedback.textContent = "";
  lastNameUpdateFeedback.textContent = "";
  ageUpdateFeedback.textContent = "";
  telephoneUpdateFeedback.textContent = "";
  emailUpdateFeedback.textContent = "";
  passwordUpdateFeedback.textContent = "";

  document.getElementById("firstName").style.borderColor = "";
  document.getElementById("lastName").style.borderColor = "";
  document.getElementById("age").style.borderColor = "";
  document.getElementById("telephone").style.borderColor = "";
  document.getElementById("email").style.borderColor = "";
  document.getElementById("password").style.borderColor = "";

  let isValid = true;

  if (firstName === "" || /\d/.test(firstName)) {
    firstNameUpdateFeedback.textContent = "Please enter your name propely.";
    document.getElementById("firstName").style.borderColor = "red";
    isValid = false;
  }

  if (lastName === "" || /\d/.test(lastName)) {
    lastNameUpdateFeedback.textContent = "Please enter your last name propely.";
    document.getElementById("lastName").style.borderColor = "red";
    isValid = false;
  }

  if (!/^\d+$/.test(age) || age <= 13) {
    ageUpdateFeedback.textContent =
      "Please enter your age propely and you have to be at least 13 years old.";
    document.getElementById("age").style.borderColor = "red";
    isValid = false;
  }

  const telephoneRegex = /^\s*\(?\d{2}\)?[-. ]?\d{4,5}[-. ]?\d{4}\s*$/;
  if (!telephoneRegex.test(telephone)) {
    telephoneUpdateFeedback.textContent =
      "Please enter your telephone number propely.";
    document.getElementById("telephone").style.borderColor = "red";
    isValid = false;
  }

  const emailRegex = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;
  if (!emailRegex.test(email)) {
    emailUpdateFeedback.textContent = "Please enter your e-mail propely.";
    document.getElementById("email").style.borderColor = "red";
    isValid = false;
  }

  const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
  if (!passwordRegex.test(password)) {
    passwordUpdateFeedback.textContent =
      "Please enter your password propely, it has to be at least 8 characters length.";
    document.getElementById("password").style.borderColor = "red";
    isValid = false;
  }

  return isValid;
}

document
  .getElementById("firstName")
  .addEventListener("input", function (event) {
    event.target.style.borderColor = "";
    firstNameUpdateFeedback.textContent = "";
  });

document.getElementById("lastName").addEventListener("input", function (event) {
  event.target.style.borderColor = "";
  lastNameUpdateFeedback.textContent = "";
});

document.getElementById("age").addEventListener("input", function (event) {
  event.target.style.borderColor = "";
  ageUpdateFeedback.textContent = "";
});

document
  .getElementById("telephone")
  .addEventListener("input", function (event) {
    event.target.style.borderColor = "";
    telephoneUpdateFeedback.textContent = "";
    phoneMaskBrazil(event);
  });

function phoneMaskBrazil(event) {
  var key = event.key;
  var element = event.target;
  var isAllowed = /\d|Backspace|Tab/;
  if (!isAllowed.test(key)) event.preventDefault();

  var inputValue = element.value;
  inputValue = inputValue.replace(/\D/g, "");
  inputValue = inputValue.substring(0, 11);
  inputValue = inputValue.replace(/(^\d{2})(\d)/, "($1) $2");
  inputValue = inputValue.replace(/(\d{4,5})(\d{4}$)/, "$1-$2");

  element.value = inputValue;
}

document.getElementById("email").addEventListener("input", function (event) {
  event.target.style.borderColor = "";
  emailUpdateFeedback.textContent = "";
});

document.getElementById("password").addEventListener("input", function (event) {
  event.target.style.borderColor = "";
  passwordUpdateFeedback.textContent = "";
});

document.getElementById("homwBtn").addEventListener("click", function () {
  localStorage.setItem("userEmail", email);
  window.location.href =
    "http://127.0.0.1:5500/src/main/resources/static/html/home.html";
});
