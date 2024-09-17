let authorization;
let idClientFound;

document
  .getElementById("formChangePassword")
  .addEventListener("submit", function (event) {
    event.preventDefault();
    let userIDInfoCookie = getCookie("userIDInfo");
    if (userIDInfoCookie) {
      try {
        let decodedValue = decodeURIComponent(userIDInfoCookie);

        let userInfo = JSON.parse(decodedValue);

        idClientFound = userInfo.idClient;
      } catch (e) {
        console.error("Error parsing JSON from cookie:", e);
      }
    } else {
      console.log("User info cookie not found!");
    }

    let userInfoCookie = getCookie("userInfo");
    if (userInfoCookie) {
      try {
        let decodedValue = decodeURIComponent(userInfoCookie);

        let userInfo = JSON.parse(decodedValue);

        authorization = userInfo.Authorization;
      } catch (e) {
        console.error("Error parsing JSON from cookie:", e);
      }
    } else {
      console.log("User info cookie not found!");
    }

    const formData = new FormData(this);

    const clientPassword = {
      password: formData.get("password"),
      id: idClientFound,
    };

    if (validateForm()) {
      fetch("http://localhost:8080/client/changePassword", {
        method: "PUT",
        headers: new Headers({
          "Content-Type": "application/json; charset=utf8",
          Accept: "application/json",
          Authorization: authorization,
        }),
        body: JSON.stringify(clientPassword),
      })
        .then((response) => {
          return response.json();
        })
        .then((data) => {
          console.log("Success:", data.message);
          document.getElementById("password").style.borderColor = "green";
          document.getElementById("confirmPassword").style.borderColor =
            "green";
          responseFromBack.textContent = data.message;
        })
        .catch((error) => {
          console.error("Error:", error);
        });
    }
  });

const passwordFeedback = document.getElementById("passwordFeedback");
const confirmPasswordFeedback = document.getElementById(
  "confirmPasswordFeedback"
);
const responseFromBack = document.getElementById("responseFromBack");

function validateForm() {
  const password = document.getElementById("password").value;
  const confirmPassword = document.getElementById("confirmPassword").value;

  passwordFeedback.textContent = "";
  confirmPasswordFeedback.textContent = "";

  document.getElementById("password").style.borderColor = "";
  document.getElementById("confirmPassword").style.borderColor = "";

  let isValid = true;

  if (password !== confirmPassword) {
    confirmPasswordFeedback.textContent = "Passwords do not match.";
    document.getElementById("confirmPassword").style.borderColor = "red";
    isValid = false;
  }

  const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
  if (!passwordRegex.test(password)) {
    passwordFeedback.textContent =
      "Please enter your password properly, it has to be at least 8 characters long and one digit.";
    document.getElementById("password").style.borderColor = "red";
    isValid = false;
  }

  const passwordConfirmRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
  if (!passwordConfirmRegex.test(confirmPassword)) {
    confirmPasswordFeedback.textContent =
      "Please enter your password properly, it has to be at least 8 characters long and one digit.";
    document.getElementById("confirmPassword").style.borderColor = "red";
    isValid = false;
  }

  return isValid;
}

document.getElementById("password").addEventListener("input", function (event) {
  event.target.style.borderColor = "";
  passwordFeedback.textContent = "";
});

document
  .getElementById("confirmPassword")
  .addEventListener("input", function (event) {
    event.target.style.borderColor = "";
    confirmPasswordFeedback.textContent = "";
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
    date.setTime(date.getTime() + hours * 60 * 10000);
    expires = "; expires=" + date.toUTCString();
  }
  document.cookie = name + "=" + (value || "") + expires + "; path=/";
}

function deleteAllCookies() {
  const cookies = document.cookie.split(";");
  cookies.forEach((cookie) => {
    const eqPos = cookie.indexOf("=");
    const name = eqPos > -1 ? cookie.substr(0, eqPos).trim() : cookie.trim();
    document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/";
  });
}
