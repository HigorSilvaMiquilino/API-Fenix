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
    imageURL:
      "http://localhost:5500/src/main/resources/static/images/default.jpg",
  };

  if (validateForm()) {
    fetch("http://localhost:8080/client", {
      method: "POST",
      headers: new Headers({
        "Content-Type": "application/json; charset=utf8",
        Accept: "application/json",
        "User-Agent": navigator.userAgent,
      }),
      body: JSON.stringify(client),
    })
      .then((response) => {
        const token = response.headers.get("Authorization");
        return response.json().then((data) => ({ data, token }));
      })
      .then(({ data, token }) => {
        console.log("Success:", data.message);
        alert("Welcome: " + data.client.firstName);
        localStorage.setItem("userEmail", data.client.email);
        localStorage.setItem("userFirstName", data.client.firstName);
        localStorage.setItem("userLastName", data.client.lastName);
        localStorage.setItem("Authorization", token);

        window.location.href =
          "http://127.0.0.1:5500/src/main/resources/static/html/home.html";
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  }
});

const firstNameFeedback = document.getElementById("firstNameFeedback");

const lastNameFeedback = document.getElementById("lastNameFeedback");

const ageFeedback = document.getElementById("ageFeedback");

const telephoneFeedback = document.getElementById("telephoneFeedback");

const emailFeedback = document.getElementById("emailFeedback");

const passwordFeedback = document.getElementById("passwordFeedback");

function validateForm() {
  const firstName = document.getElementById("firstName").value;
  const lastName = document.getElementById("lastName").value;
  const age = document.getElementById("age").value;
  const telephone = document.getElementById("telephone").value;
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  firstNameFeedback.textContent = "";
  lastNameFeedback.textContent = "";
  ageFeedback.textContent = "";
  telephoneFeedback.textContent = "";
  emailFeedback.textContent = "";
  passwordFeedback.textContent = "";

  document.getElementById("firstName").style.borderColor = "";
  document.getElementById("lastName").style.borderColor = "";
  document.getElementById("age").style.borderColor = "";
  document.getElementById("telephone").style.borderColor = "";
  document.getElementById("email").style.borderColor = "";
  document.getElementById("password").style.borderColor = "";

  let isValid = true;

  if (firstName === "" || /\d/.test(firstName)) {
    firstNameFeedback.textContent = "Please enter your name propely.";
    document.getElementById("firstName").style.borderColor = "red";
    isValid = false;
  }

  if (lastName === "" || /\d/.test(lastName)) {
    lastNameFeedback.textContent = "Please enter your last name propely.";
    document.getElementById("lastName").style.borderColor = "red";
    isValid = false;
  }

  if (!/^\d+$/.test(age) || age <= 13) {
    ageFeedback.textContent =
      "Please enter your age propely and you have to be at least 13 years old.";
    document.getElementById("age").style.borderColor = "red";
    isValid = false;
  }

  const telephoneRegex = /^\s*\(?\d{2}\)?[-. ]?\d{4,5}[-. ]?\d{4}\s*$/;
  if (!telephoneRegex.test(telephone)) {
    telephoneFeedback.textContent =
      "Please enter your telephone number propely.";
    document.getElementById("telephone").style.borderColor = "red";
    isValid = false;
  }

  const emailRegex = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;
  if (!emailRegex.test(email)) {
    emailFeedback.textContent = "Please enter your e-mail propely.";
    document.getElementById("email").style.borderColor = "red";
    isValid = false;
  }

  const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
  if (!passwordRegex.test(password)) {
    passwordFeedback.textContent =
      "Please enter your password propely, it has to be at least 8 characters length and one digit.";
    document.getElementById("password").style.borderColor = "red";
    isValid = false;
  }

  return isValid;
}

document
  .getElementById("firstName")
  .addEventListener("input", function (event) {
    event.target.style.borderColor = "";
    firstNameFeedback.textContent = "";
  });

document.getElementById("lastName").addEventListener("input", function (event) {
  event.target.style.borderColor = "";
  lastNameFeedback.textContent = "";
});

document.getElementById("age").addEventListener("input", function (event) {
  event.target.style.borderColor = "";
  ageFeedback.textContent = "";
});

document
  .getElementById("telephone")
  .addEventListener("input", function (event) {
    event.target.style.borderColor = "";
    telephoneFeedback.textContent = "";
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
  emailFeedback.textContent = "";
});

document.getElementById("password").addEventListener("input", function (event) {
  event.target.style.borderColor = "";
  passwordFeedback.textContent = "";
});
