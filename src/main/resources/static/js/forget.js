document
  .getElementById("passwordResetForm")
  .addEventListener("submit", function (event) {
    event.preventDefault();

    const email = document.getElementById("email").value;

    fetch("http://localhost:8080/reset/client/resetPassword", {
      method: "POST",
      headers: new Headers({
        "Content-Type": "application/json; charset=utf8",
        Accept: "application/json",
      }),
      body: JSON.stringify({ content: email }),
    })
      .then((response) => {
        if (!response.ok) {
          return response.json().then((err) => {
            throw new Error(err.message);
          });
        }
        return response.json();
      })
      .then((data) => {
        console.log(data);
        document.getElementById("responseFromBack").textContent = data.message;
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  });
