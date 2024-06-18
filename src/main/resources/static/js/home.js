document.addEventListener("DOMContentLoaded", () => {
  const email = localStorage.getItem("userEmail");

  if (email) {
    fetch(`http://localhost:8080/client/email/${email}`)
      .then((response) => response.json())
      .then((data) => {
        const container = document.getElementById("cardProfile");

        container.innerHTML = "";

        const cardProfile = document.createElement("div");
        cardProfile.className = "card";
        cardProfile.innerHTML = `
        <img src="${data.imageUrl}" alt="${data.firstName}"  style="width: 100%">
        <h1>${data.firstName} ${data.lastName}</h1>
        <p>Great to see you here</p>
        `;
        container.appendChild(cardProfile);

        const buttons = `
          <button id="promotions">Promotions</button>
          <button id="updateButtion">Update Profile</button>
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
      })
      .catch((error) => console.error("Error fetching user data", error));
  }
});

function fetchPromotion() {
  fetch("http://localhost:8080/promotion/all")
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

function updateProfile() {}

function deleteAccount() {}
