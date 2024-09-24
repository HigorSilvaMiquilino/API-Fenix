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
            document.getElementById("password").style.borderColor = "red";
            document.getElementById("email").style.borderColor = "red";
            responseFromBack.style.color = "red";
            responseFromBack.textContent = err.message;

            return Promise.reject();
          });
        }
        let token = response.headers.get("Authorization");
        return token;
      })
      .then((token) => {
        const userInfo = {
          email: email,
          Authorization: token,
        };

        const jsonValue = encodeURIComponent(JSON.stringify(userInfo));
        setCookie("userInfo", jsonValue, 7);

        window.location.href = "/home";
      })
      .catch((error) => {
        console.error("Error:", error);
        alert(error.message || "Invalid credentials. Please try again.");
      });
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

const responseFromBack = document.getElementById("responseFromBack");

document
  .getElementById("responseFromBack")
  .addEventListener("input", function (event) {
    event.target.style.borderColor = "";
  });

document.getElementById("password").addEventListener("input", function (event) {
  event.target.style.borderColor = "";
  responseFromBack.textContent = "";
});

document.getElementById("email").addEventListener("input", function (event) {
  event.target.style.borderColor = "";
  responseFromBack.textContent = "";
});
