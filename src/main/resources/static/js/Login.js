document.getElementById("login-form").addEventListener("submit", function (event) {
    event.preventDefault();
    submitForm(event.target);
});

function submitForm(form) {
    const formData = new FormData(form);
    const loginRequest = {
        email: formData.get("email"),
        password: formData.get("password")
    };

    postRequest(loginRequest);
}

async function postRequest(loginRequest) {
    const options = {
        method: 'POST',
        body: JSON.stringify(loginRequest),
        headers: new Headers({
            'Content-Type': 'application/json'
        })
    };

    try {
        const response = await fetch('/api/users/login', options);
        if (response.ok) {
            const okJson = await response.json();
            handleSuccessfulLogin(okJson)
            window.location.href = '/';
        } else {
            const errorJson = await response.json();
            displayErrorMessages(errorJson);
        }
    } catch (error) {
        console.error(`${error}`);
    }
}

function handleSuccessfulLogin(okJson) {
    localStorage.setItem('jwtToken', okJson.token);
}

function displayErrorMessages(errorJson) {
    const errorField = document.getElementById('messageError');
    errorField.textContent = errorJson?.error ?? "An unexpected error occurred.";
}