let id;
let key = "Authorization";
let password;
let email;
let authorization;

document.addEventListener("DOMContentLoaded", function () {
  let userInfoCookie = getCookie("userInfo");
  if (userInfoCookie) {
    try {
      let decodedValue = decodeURIComponent(userInfoCookie);
      let userInfo = JSON.parse(decodedValue);

      email = userInfo.email;

      authorization = userInfo.Authorization;
    } catch (e) {
      console.error("Error parsing JSON from cookie:", e);
    }
  } else {
    console.log("User info cookie not found!");
  }

  if (email) {
    fetch(`http://localhost:8080/client/email/${email}`, {
      method: "GET",
      headers: new Headers({
        Authorization: authorization,
        "Content-Type": "application/json",
      }),
    })
      .then((response) => response.json())
      .then((data) => {
        document.getElementById("firstName").value = data.firstName;
        document.getElementById("lastName").value = data.lastName;
        document.getElementById("age").value = data.age;
        document.getElementById("telephone").value = data.telephone;
        document.getElementById("email").value = data.email;
        password = data.password;
        id = data.id;

        const userIDInfo = {
          idClient: id,
        };

        const jsonValue = encodeURIComponent(JSON.stringify(userIDInfo));

        setCookie("userIDInfo", jsonValue, 7);

        document
          .getElementById("formProfile")
          .addEventListener("submit", updateProfilePicture);
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
    };

    if (validateUpdateForm()) {
      fetch(`http://localhost:8080/client/${id}`, {
        method: "PUT",
        headers: new Headers({
          Authorization: authorization,
          "Content-Type": "application/json",
        }),
        body: JSON.stringify(clientUpdated),
      })
        .then((response) => {
          return response.json();
        })
        .then((data) => {
          alert(data.message);
          const userInfo = {
            email: data.client.email,
            firstName: data.client.firstName,
            lastName: data.client.lastName,
            Authorization: authorization,
          };

          const jsonValue = encodeURIComponent(JSON.stringify(userInfo));
          setCookie("userInfo", jsonValue, 7);

          window.location.href = "/home";
        })
        .catch((error) => console.error("Error updating client: " + error));
    }
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

function validateUpdateForm() {
  const firstName = document.getElementById("firstName").value;
  const lastName = document.getElementById("lastName").value;
  const age = document.getElementById("age").value;
  const telephone = document.getElementById("telephone").value;
  const email = document.getElementById("email").value;

  firstNameUpdateFeedback.textContent = "";
  lastNameUpdateFeedback.textContent = "";
  ageUpdateFeedback.textContent = "";
  telephoneUpdateFeedback.textContent = "";
  emailUpdateFeedback.textContent = "";

  document.getElementById("firstName").style.borderColor = "";
  document.getElementById("lastName").style.borderColor = "";
  document.getElementById("age").style.borderColor = "";
  document.getElementById("telephone").style.borderColor = "";
  document.getElementById("email").style.borderColor = "";

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

document.getElementById("homeBtn").addEventListener("click", function () {
  window.location.href = "/home";
});

document.getElementById("passwordeBtn").addEventListener("click", function () {
  window.location.href = "/changePassword";
});

document.getElementById("profileBtn").addEventListener("click", function () {
  window.location.href = "/updateProfilePicture";
});

function getCookie(name) {
  let matches = document.cookie.match(
    new RegExp(
      "(?:^|; )" + name.replace(/([.$?*|{}()[\]\\/+^])/g, "\\$1") + "=([^;]*)"
    )
  );
  return matches ? decodeURIComponent(matches[1]) : undefined;
}

let userInfoCookie = getCookie("userInfo");

if (userInfoCookie) {
  let decodedValue = decodeURIComponent(userInfoCookie);
  let userInfo = JSON.parse(decodedValue);
} else {
  console.log("User info cookie not found!");
}

function getAllCookieNames() {
  let cookies = document.cookie.split(";");
  let cookieNames = [];

  cookies.forEach((cookie) => {
    let name = cookie.split("=")[0].trim();
    cookieNames.push(name);
  });

  return cookieNames;
}

function setCookie(name, value, hours) {
  let expires = "";
  if (hours) {
    const date = new Date();
    date.setTime(date.getTime() + hours * 60 * 1000);
    expires = "; expires=" + date.toUTCString();
  }
  document.cookie = name + "=" + (value || "") + expires + "; path=/";
}
