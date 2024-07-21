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
            src=${data.imageURL}
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
          <button id="deleteButtion">Delete Acount</button>
        `;
        container.insertAdjacentHTML("beforeend", buttons);

        if (data.imageURL) {
          document.getElementById("currentImage").src = data.imageURL;
        } else {
          document.getElementById("currentImage").src =
            "http://localhost:5500/src/main/resources/static/images/default.jpg ";
        }

        document
          .getElementById("promotions")
          .addEventListener("click", fetchPromotion);
        document
          .getElementById("updateButtion")
          .addEventListener("click", updateProfile);

        document
          .getElementById("deleteButtion")
          .addEventListener("click", deleteAccount);
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
  window.location.href =
    "http://127.0.0.1:5500/src/main/resources/static/html/updateProfile.html";
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
          "http://127.0.0.1:5500/src/main/resources/static/html/login.html";
      })
      .catch((error) => console.error("Error deleting account: " + error));
  }
}
