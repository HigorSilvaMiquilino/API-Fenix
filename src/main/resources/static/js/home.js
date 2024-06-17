document.getElementById("promotions").addEventListener("click", () => {
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
});
