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

    postRequest(registrationRequest);
}

async function postRequest(registrationRequest) {
    const options = {
        method: 'POST',
        body: JSON.stringify(registrationRequest),
        headers: new Headers({
            'Content-Type': 'application/json'
        })
    };

    try {
        const response = await fetch(`/api/users`, options);
        if (response.ok) {
            window.location.href = '/login';
        } else {
            const errorJson = await response.json();
            displayErrorMessages(errorJson);
        }
    } catch (error) {
        console.error(`${error}`);
    }
}

function displayErrorMessages(errorJson) {
    const errorField = document.getElementById('messageError');
    if (errorJson && errorJson.error) {
        errorField.textContent = errorJson.error;
    } else {
        errorField.textContent = "An unexpected error occurred.";
    }
}