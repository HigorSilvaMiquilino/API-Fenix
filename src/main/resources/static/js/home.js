let idClient;

document.addEventListener("DOMContentLoaded", () => {
  const email = localStorage.getItem("userEmail");
  const firstName = localStorage.getItem("userFirstName");
  const lastName = localStorage.getItem("userLastName");

  if (email) {
    fetch(`http://localhost:8080/client/email/${email}`, {
      method: "GET",
      headers: new Headers({
        Authorization: localStorage.getItem("Authorization"),
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
      })
      .catch((error) => console.error("Error fetching user data", error));
  }
});

function fetchPromotion() {
  fetch("http://localhost:8080/promotion/all", {
    method: "GET",
    headers: new Headers({
      Authorization: localStorage.getItem("Authorization"),
      "Content-Type": "application/json",
    }),
  })
    .then((response) => response.json())
    .then((data) => {
      const container = document.getElementById("cardsContainer");

      data.forEach((promotion) => {
        const card = document.createElement("div");
        card.className = "cardArry";
        card.innerHTML = `
          <img src="${promotion.imageUrl}" alt="${promotion.promotionName}">
          <h2>${promotion.promotionName}</h2>
          <p>${promotion.description}</p>
          <p>Prize $R: ${promotion.prize}</p>
          <button>Participate</button>
        `;
        container.appendChild(card);
      });
    })
    .catch((error) => console.error("Error fetching promotions:", error));
}

function updateProfile() {
  window.location.href = "/updateProfile";
}

function deleteAccount() {
  if (confirm("Are you sure you want to delete this account?")) {
    fetch(`http://localhost:8080/client/${idClient}`, {
      method: "DELETE",
      headers: new Headers({
        Authorization: localStorage.getItem("Authorization"),
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
  // Create the popup container
  const logoutPopup = document.createElement("div");
  logoutPopup.id = "logoutPopup";
  logoutPopup.className = "popup";

  // Create the popup content
  const popupContent = document.createElement("div");
  popupContent.className = "popup-content";

  // Create the header
  const header = document.createElement("h2");
  header.textContent = "Are you sure you want to logout?";

  // Create the buttons container
  const popupButtons = document.createElement("div");
  popupButtons.className = "popup-buttons";

  // Create the confirm button
  const confirmLogoutBtn = document.createElement("button");
  confirmLogoutBtn.id = "confirmLogoutBtn";
  confirmLogoutBtn.className = "btn confirm";
  confirmLogoutBtn.textContent = "Yes";

  // Create the cancel button
  const cancelLogoutBtn = document.createElement("button");
  cancelLogoutBtn.id = "cancelLogoutBtn";
  cancelLogoutBtn.className = "btn cancel";
  cancelLogoutBtn.textContent = "No";

  // Append the buttons to the buttons container
  popupButtons.appendChild(confirmLogoutBtn);
  popupButtons.appendChild(cancelLogoutBtn);

  // Append the header and buttons container to the popup content
  popupContent.appendChild(header);
  popupContent.appendChild(popupButtons);

  // Append the popup content to the popup container
  logoutPopup.appendChild(popupContent);

  // Append the popup container to the body (or any other container)
  document.body.appendChild(logoutPopup);

  // Add event listeners for the buttons
  confirmLogoutBtn.addEventListener("click", function () {
    fetch(`http://localhost:8080/logout`, {
      method: "POST",
      headers: new Headers({
        Authorization: localStorage.getItem("Authorization"),
        "Content-Type": "application/json",
      }),
    })
      .then((response) => {
        console.log(response);
        logoutPopup.style.display = "none";
        // Clear the Authorization token from localStorage and redirect to login page
        localStorage.clear();
        window.location.href = "/";
      })
      .catch((error) => {
        console.error("Error during logout:", error);
      });
  });

  cancelLogoutBtn.addEventListener("click", function () {
    logoutPopup.style.display = "none";
  });

  // Show the popup
  logoutPopup.style.display = "block";
}
