let id;
let email;
let authorization;

document.addEventListener("DOMContentLoaded", function () {
  let allCookieNames = getAllCookieNames();
  let firstCokieName = allCookieNames[0];
  let userInfoCookie = getCookie(firstCokieName);
  if (userInfoCookie) {
    try {
      let decodedValue = decodeURIComponent(userInfoCookie);
      let userInfo = JSON.parse(decodedValue);

      console.log("User Email:", userInfo.email);
      email = userInfo.email;
      console.log("Authorization:", userInfo.Authorization);
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
          document.getElementById("currentImage").src = "/images/default.jpg ";
        }

        if (data.imageURL === null) {
          document.getElementById("currentImage").src = "/images/default.jpg ";
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
    headers: new Headers({
      Authorization: authorization,
    }),
    body: formData,
  })
    .then((response) => response.json())
    .then((data) => {
      console.log("Success:", data.message);
      const currentImage = document.getElementById("currentImage");
      if (currentImage) {
        currentImage.src = data.client.imageURL;
      } else {
        console.error("Current image element not found");
      }
    })
    .catch((error) =>
      console.error("Error updating profile picture: " + error)
    );
}

function home() {
  const userInfo = {
    email: email,
    Authorization: authorization,
  };

  const jsonValue = encodeURIComponent(JSON.stringify(userInfo));

  setCookie("userInfo", jsonValue, 7);
  window.location.href = "/home";
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

  console.log("User Email:", userInfo.userEmail);
  console.log("Authorization:", userInfo.Authorization);
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
