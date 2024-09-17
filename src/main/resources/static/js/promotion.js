let email;
let idClient;
let id;
let authorization;
document.addEventListener("DOMContentLoaded", () => {
  let promotionInformationCokie = getCookie("userPromotionInfo");
  if (promotionInformationCokie) {
    try {
      let decodedValue = decodeURIComponent(promotionInformationCokie);

      let promotionInfo = JSON.parse(decodedValue);

      email = promotionInfo.email;

      id = promotionInfo.promotionId;
    } catch (e) {
      console.error("Error parsing JSON from cookie:", e);
    }
  } else {
    console.log("User info cookie not found!");
  }

  let userInformationCokie = getCookie("userInfo");

  if (userInformationCokie) {
    try {
      let decodedValue = decodeURIComponent(userInformationCokie);

      let userInfo = JSON.parse(decodedValue);

      email = userInfo.email;

      authorization = userInfo.Authorization;
    } catch (e) {
      console.error("Error parsing JSON from cookie:", e);
    }
  } else {
    console.log("User info cookie not found!");
  }

  fetch(`http://localhost:8080/client/email/${email}`, {
    method: "GET",
    headers: new Headers({
      Authorization: authorization,
      "Content-Type": "application/json",
    }),
  })
    .then((response) => response.json())
    .then((data) => {
      idClient = data.id;
    })
    .catch((error) => console.error("Error fetching user data", error));

  fetch(`http://localhost:8080/promotion/${id}`, {
    method: "GET",
    headers: new Headers({
      Authorization: authorization,
      "Content-Type": "application/json",
    }),
  })
    .then((response) => response.json())
    .then((data) => {
      document.getElementById("h1").textContent = data.promotionName;
      document.getElementById("currentImage").src = data.imageUrl;
    })
    .catch((error) => console.error("Error fetching promotions:", error));
});

document
  .getElementById("apiCouponForm")
  .addEventListener("submit", function (event) {
    event.preventDefault();

    const formData = new FormData(this);
    const couponData = {
      cnpj: formData.get("CNPJ"),
      couponNumber: formData.get("number"),
      localDate: formData.get("dateOfPurchase"),
      clientId: idClient,
      promotionId: id,
    };

    if (validateForm()) {
      fetch(`http://localhost:8080/coupon`, {
        method: "POST",
        headers: new Headers({
          Authorization: authorization,
          "Content-Type": "application/json",
        }),
        body: JSON.stringify(couponData),
      })
        .then((response) => {
          return response.json();
        })
        .then((data) => {
          alert(data.message);
        })
        .catch((error) => console.error("Error saving coupon: " + error));
    }
  });

const CNPJFeedback = document.getElementById("CNPJFeedback");
const couponNumberFeedback = document.getElementById("couponNumberFeedback");
const dateOfPurchaseFeedback = document.getElementById(
  "dateOfPurchaseFeedback"
);

function validateForm() {
  const CNPJ = document.getElementById("CNPJ").value;
  const number = document.getElementById("number").value;
  const dateOfPurchase = document.getElementById("dateOfPurchase").value;

  CNPJFeedback.textContent = "";
  couponNumberFeedback.textContent = "";
  dateOfPurchaseFeedback.textContent = "";

  document.getElementById("CNPJ").style.borderColor = "";
  document.getElementById("number").style.borderColor = "";
  document.getElementById("dateOfPurchase").style.borderColor = "";

  let isValid = true;

  const CNPJdRegex = /^\d{2}\.\d{3}\.\d{3}\/\d{4}\-\d{2}$/;
  if (!CNPJdRegex.test(CNPJ)) {
    CNPJFeedback.textContent = "Please enter your CNPJ properly";
    document.getElementById("CNPJ").style.borderColor = "red";
    isValid = false;
  }

  const numberCouponmRegex = /^\d{6}$/;
  if (!numberCouponmRegex.test(number)) {
    couponNumberFeedback.textContent =
      "Please enter your coupon number properly, it has to be 6 digits log.";
    document.getElementById("number").style.borderColor = "red";
    isValid = false;
  }

  if (dateOfPurchase === "") {
    dateOfPurchaseFeedback.textContent = "Datte field can't be empty";
    document.getElementById("dateOfPurchase").style.borderColor = "red";
    isValid = false;
  }

  return isValid;
}

document.getElementById("CNPJ").addEventListener("input", function (event) {
  event.target.style.borderColor = "";
  CNPJFeedback.textContent = "";
});

document.getElementById("number").addEventListener("input", function (event) {
  event.target.style.borderColor = "";
  couponNumberFeedback.textContent = "";
});

document
  .getElementById("dateOfPurchase")
  .addEventListener("input", function (event) {
    event.target.style.borderColor = "";
    dateOfPurchaseFeedback.textContent = "";
  });

function getCookie(name) {
  let matches = document.cookie.match(
    new RegExp(
      "(?:^|; )" + name.replace(/([.$?*|{}()[\]\\/+^])/g, "\\$1") + "=([^;]*)"
    )
  );
  return matches ? decodeURIComponent(matches[1]) : undefined;
}

let userPromotionInfoCookie = getCookie("userPromotionInfo");

if (userPromotionInfoCookie) {
  let decodedValue = decodeURIComponent(userPromotionInfoCookie);

  let promotionInfo = JSON.parse(decodedValue);
} else {
  console.log("promotion info cookie not found!");
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

  cookies.forEach((eachCookies) => {
    const cookie = eachCookies.indexOf("=");
    const name = cookie > -1 ? cookie.substr(0, eqPos) : cookie;
    document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/";
  });
}
