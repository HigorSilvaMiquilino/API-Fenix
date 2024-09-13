let idClient;
let email;
let authorization;
let promotionIdByCoupon;
let promotionTakenId = [];

document.addEventListener("DOMContentLoaded", () => {
  let allCookieNames = getAllCookieNames();
  let firstCokieName = allCookieNames[0];
  let userInfoCookie = getCookie(firstCokieName);
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
        const container = document.getElementById("cardProfile");
        idClient = data.id;

        container.innerHTML = "";

        const cardProfile = document.createElement("div");
        cardProfile.className = "card";
        cardProfile.innerHTML = `
            <img
            id="currentImage"
            src="${data.imageURL || "/default.jpg"}"
            alt="Current Profile Image"
            style="width: 250px; height: 250px"
          />
          <h1>${data.firstName || firstName} ${data.lastName || lastName}</h1>
        <p>Great to see you here</p>
        `;
        container.appendChild(cardProfile);

        const buttons = `
          <button id="promotions">Promotions</button>
          <button id="myPromotions">My Promotions</button>
          <button id="updateButtion">Update Profile</button>
          <button id="LogoutButton">Logout</button>
          <button id="deleteButtion">Delete Acount</button>
        `;
        container.insertAdjacentHTML("beforeend", buttons);

        document
          .getElementById("promotions")
          .addEventListener("click", fetchPromotion);
        document
          .getElementById("updateButtion")
          .addEventListener("click", updateProfile);

        document
          .getElementById("deleteButtion")
          .addEventListener("click", deleteAccount);

        document
          .getElementById("LogoutButton")
          .addEventListener("click", logoutAccount);

        document
          .getElementById("myPromotions")
          .addEventListener("click", fetchMyPromotions);
      })
      .catch((error) => console.error("Error fetching user data", error));
  }
});

async function fetchPromotion() {
  try {
    const response = await fetch("http://localhost:8080/promotion/all", {
      method: "GET",
      headers: new Headers({
        Authorization: authorization,
        "Content-Type": "application/json",
      }),
    });
    const data = await response.json();

    const container = document.getElementById("cardsContainer");
    container.innerHTML = "";

    const takenPromotions = await fetchPromotionAlreadyTaken();

    data.forEach((promotion, index) => {
      if (takenPromotions.some((takenId) => takenId === promotion.id)) {
        console.log(
          `Skipping promotion with ID: ${promotion.id}, witch is ${promotion.promotionName}`
        );
        return;
      }

      const card = document.createElement("div");
      card.className = "cardArry";
      card.innerHTML = `
        <img src="${promotion.imageUrl}" alt="${promotion.promotionName}">
        <h2>${promotion.promotionName}</h2>
        <p>${promotion.description}</p>
        <p>Prize $R: ${promotion.prize}</p>
        <button class="participateButton" data-id="${promotion.id}" data-index="${index}">Participate</button>
      `;
      container.appendChild(card);
    });

    const buttons = document.querySelectorAll(".participateButton");
    buttons.forEach((button) => {
      button.addEventListener("click", function () {
        const userPromotionInfo = {
          email: email,
          promotionId: this.getAttribute("data-id"),
        };
        const jsonValue = encodeURIComponent(JSON.stringify(userPromotionInfo));
        setCookie("userPromotionInfo", jsonValue, 7);
        window.location.href = "/promotioncupom";
      });
    });
  } catch (error) {
    console.error("Error fetching promotions:", error);
  }
}

function fetchMyPromotions() {
  fetch(`http://localhost:8080/coupon/CouponClient/${idClient}`, {
    method: "GET",
    headers: new Headers({
      Authorization: authorization,
      "Content-Type": "application/json",
    }),
  })
    .then((response) => response.json())
    .then((data) => {
      const container = document.getElementById("cardsContainer");
      container.innerHTML = "";

      data.forEach((promotionIdByCoupon) => {
        fetch(
          `http://localhost:8080/promotion/${promotionIdByCoupon.promotionId}`,
          {
            method: "GET",
            headers: new Headers({
              Authorization: authorization,
              "Content-Type": "application/json",
            }),
          }
        )
          .then((response) => response.json())
          .then((data) => {
            const card = document.createElement("div");
            card.className = "cardArry";
            card.innerHTML = `
              <img src="${data.imageUrl}" alt="${data.promotionName}">
              <h2>${data.promotionName}</h2>
              <p>${data.description}</p>
              <p>Prize $R: ${data.prize}</p>
            `;
            container.appendChild(card);
          })
          .catch((error) => console.error("Error fetching promotions:", error));
      });
    })
    .catch((error) => console.error("Error fetching promotions:", error));
}

async function fetchPromotionAlreadyTaken() {
  try {
    const response = await fetch(
      `http://localhost:8080/coupon/CouponClient/${idClient}`,
      {
        method: "GET",
        headers: new Headers({
          Authorization: authorization,
          "Content-Type": "application/json",
        }),
      }
    );
    const data = await response.json();
    promotionTakenId = data.map((element) => element.promotionId);
    return promotionTakenId;
  } catch (error) {
    console.error("Error fetching promotions:", error);
    return [];
  }
}

function updateProfile() {
  window.location.href = "/updateProfile";
}

function deleteAccount() {
  if (confirm("Are you sure you want to delete this account?")) {
    fetch(`http://localhost:8080/client/${idClient}`, {
      method: "DELETE",
      headers: new Headers({
        Authorization: authorization,
        "Content-Type": "application/json",
      }),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        alert("Account deleted succesfully");
        window.location.href =
          "http://127.0.0.1:5500/src/main/resources/templates/login.html";
      })
      .catch((error) => console.error("Error deleting account: " + error));
  }
}

function logoutAccount() {
  const logoutPopup = document.createElement("div");
  logoutPopup.id = "logoutPopup";
  logoutPopup.className = "popup";

  const popupContent = document.createElement("div");
  popupContent.className = "popup-content";

  const header = document.createElement("h2");
  header.textContent = "Are you sure you want to logout?";

  const popupButtons = document.createElement("div");
  popupButtons.className = "popup-buttons";

  const confirmLogoutBtn = document.createElement("button");
  confirmLogoutBtn.id = "confirmLogoutBtn";
  confirmLogoutBtn.className = "btn confirm";
  confirmLogoutBtn.textContent = "Yes";

  const cancelLogoutBtn = document.createElement("button");
  cancelLogoutBtn.id = "cancelLogoutBtn";
  cancelLogoutBtn.className = "btn cancel";
  cancelLogoutBtn.textContent = "No";

  popupButtons.appendChild(confirmLogoutBtn);
  popupButtons.appendChild(cancelLogoutBtn);

  popupContent.appendChild(header);
  popupContent.appendChild(popupButtons);

  logoutPopup.appendChild(popupContent);

  document.body.appendChild(logoutPopup);

  confirmLogoutBtn.addEventListener("click", function () {
    fetch(`http://localhost:8080/logout`, {
      method: "POST",
      headers: new Headers({
        Authorization: authorization,
        "Content-Type": "application/json",
      }),
    })
      .then((response) => {
        logoutPopup.style.display = "none";
        deleteAllCookies();
        window.location.href = "/";
      })
      .catch((error) => {
        console.error("Error during logout:", error);
      });
  });

  cancelLogoutBtn.addEventListener("click", function () {
    logoutPopup.style.display = "none";
  });

  logoutPopup.style.display = "block";
}

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
