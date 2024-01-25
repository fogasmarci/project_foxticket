document.getElementById("registrationForm").addEventListener("submit", function (event) {
    event.preventDefault();
    submitForm(event.target);
});

function submitForm(form) {
    const formData = new FormData(form);
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
        const response = await fetch('/api/users', options);
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
    errorField.textContent = errorJson?.error ?? "An unexpected error occurred.";
}