let id;
let email;

document.addEventListener("DOMContentLoaded", function () {
  email = localStorage.getItem("userEmail");

  if (email) {
    fetch(`http://localhost:8080/client/email/${email}`)
      .then((response) => response.json())
      .then((data) => {
        const container = document.getElementById("cardProfile");
        id = data.id;

        container.innerHTML = "";

        const cardProfile = document.createElement("div");
        cardProfile.className = "card";
        cardProfile.innerHTML = `
          <form enctype="multipart/form-data" id="formProfile">
            <div>
              <img
              id="currentImage" 
              src=${data.imageURL}
              alt="Current Profile Image"
              style="width: 250px; height: 250px"
              />
            </div>
            <div>
                <input type="file" id="file" name="file" accept="image/*"/>
                <button  type="submit" id="updateProfileButtion">Update Profile Picture</button>
                <button id="homeBtn"><i class="fa fa-home">Home</i></button>
            </div>
          </form>
        `;
        container.appendChild(cardProfile);

        if (data.imageURL) {
          document.getElementById("currentImage").src = data.imageURL;
        } else {
          document.getElementById("currentImage").src =
            "http://localhost:5500/src/main/resources/static/images/default.jpg ";
        }

        if (data.imageURL === null) {
          document.getElementById("currentImage").src =
            "http://localhost:5500/src/main/resources/static/images/default.jpg ";
        }

        document
          .getElementById("formProfile")
          .addEventListener("submit", updateProfilePicture);

        document.getElementById("homeBtn").addEventListener("click", home);
      })
      .catch((error) => console.error("Error fetching client data: " + error));
  }
});

function updateProfilePicture(event) {
  event.preventDefault();

  const fileInput = document.getElementById("file");
  const formData = new FormData();
  formData.append("imageURL", fileInput.files[0]);

  fetch(`http://localhost:8080/client/${id}/profile`, {
    method: "PUT",
    body: formData,
  })
    .then((response) => response.json())
    .then((data) => {
      console.log("Success:", data.message);
      const currentImage = document.getElementById("currentImage");
      if (currentImage) {
        currentImage.src = data.client.imageURL;
        window.location.href =
          "http://127.0.0.1:5500/src/main/resources/static/html/home.html";
      } else {
        console.error("Current image element not found");
      }
    })
    .catch((error) =>
      console.error("Error updating profile picture: " + error)
    );
}

function home() {
  localStorage.setItem("userEmail", email);
  window.location.href =
    "http://127.0.0.1:5500/src/main/resources/static/html/home.html";
}
