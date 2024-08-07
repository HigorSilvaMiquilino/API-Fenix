document
  .getElementById("formUpdatePassword")
  .addEventListener("submit", function (event) {
    event.preventDefault();

    const formData = new FormData(this);

    const client = {
      password: formData.get("password"),
      token: formData.get("token"),
    };

    if (validateForm()) {
      fetch("http://localhost:8080/reset/client/savePassword", {
        method: "POST",
        headers: new Headers({
          "Content-Type": "application/json; charset=utf8",
          Accept: "application/json",
        }),
        body: JSON.stringify(client),
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
const tokenFeedBack = document.getElementById("tokenFeedBack");

function validateForm() {
  const password = document.getElementById("password").value;
  const confirmPassword = document.getElementById("confirmPassword").value;
  const token = document.getElementById("token").value;

  passwordFeedback.textContent = "";
  confirmPasswordFeedback.textContent = "";
  tokenFeedBack.textContent = "";

  document.getElementById("password").style.borderColor = "";
  document.getElementById("confirmPassword").style.borderColor = "";
  document.getElementById("tokenFeedBack").style.borderColor = "";

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

  if (token === "") {
    tokenFeedBack.textContent = "Token field can't be empty";
    document.getElementById("token").style.borderColor = "red";
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

document.getElementById("token").addEventListener("input", function (event) {
  event.target.style.borderColor = "";
  tokenFeedBack.textContent = "";
});
