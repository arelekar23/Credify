
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('loginForm');
    const username = document.getElementById('username');
    const password = document.getElementById('password');
    const usernameRegex = /^[A-Za-z0-9]{5,25}$/;
    const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])(?=.*[A-Z])[A-Za-z\d!@#$%^&*]{8,}$/;

    function showError(element, message) {
        let errorDiv = document.createElement('div');
        errorDiv.className = 'alert alert-danger';
        errorDiv.innerText = message;
        element.parentElement.appendChild(errorDiv);
    }

    function clearErrors() {
        const errors = document.querySelectorAll('.alert-danger');
        errors.forEach(error => error.remove());
    }


    function validateUsername() {
        clearErrors();
        if (!usernameRegex.test(username.value)) {
            showError(username, 'Username must be alphanumeric and between 5 to 25 characters.');
            return false;
        }
        return true;
    }

    function validatePassword() {
        clearErrors();
        if (!passwordRegex.test(password.value)) {
            showError(password, 'Password must be at least 8 characters long, contain at least one uppercase letter, one number, and one special character.');
            return false;
        }
        return true;
    }

    form.addEventListener('submit', function(event) {
        event.preventDefault();

        if (
            validateUsername() &&
            validatePassword()
        ) {
            form.submit();
        }
    });

    username.addEventListener('input', validateUsername);
    password.addEventListener('input', validatePassword);
});