document.getElementById("registrationForm").addEventListener("submit", function (event) {
    event.preventDefault();
    submitForm();
});

function submitForm() {
    const formData = new FormData(document.getElementById("registrationForm"));

    const registrationRequest = {
        name: formData.get("name"),
        email: formData.get("email"),
        password: formData.get("password")
    };
    const postRequest = req => {
        const options = {
            method: 'POST',
            body: JSON.stringify(registrationRequest),
            headers: new Headers({
                'Content-Type': 'application/json'
            })
        }

        fetch(`/api/users`, options)
            .then(res =>  res.json())
            .then(jsonData => {
                if (jsonData.id) {
                    window.location.href = '/login';
                } else {
                    displayErrorMessages(jsonData)
                }
            })
            .catch(error => console.error(`${error}`));
    }

    postRequest(registrationRequest);
}

function displayErrorMessages(error) {
    const errorField = document.getElementById('messageError');
    return errorField.textContent = error.error;
}
