document.getElementById("registrationForm").addEventListener("submit", function (event) {
    event.preventDefault();
    submitForm();
});

function submitForm() {
    const request = () => {
        let name = document.getElementById("name").value;
        let email = document.getElementById("email").value;
        let password = document.getElementById("password").value;

        return {
            name: name,
            email: email,
            password: password
        }
    };

    const postRequest = post => {
        const options = {
            method: 'POST',
            body: JSON.stringify(request()),
            headers: new Headers({
                'Content-Type': 'application/json'
            })
        }

        fetch(`http://localhost:8080/api/users`, options)
            .then(res => res.json())
            .then(response => {
                if (response.id) {
                    window.location.href = 'http://localhost:8080/login';
                } else {
                    console.log(response)
                    displayErrorMessages(response)
                }
            })
            .catch(error => console.error(`${error}`));
    }

    postRequest(request());
}

function displayErrorMessages(error) {
    const errorField = document.getElementById('messageError');
    return errorField.textContent = error.error;
}
