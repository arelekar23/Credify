document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('updatePasswordForm');
    const currentPassword = document.getElementById('currentPassword');
    const newPassword = document.getElementById('newPassword');
    const confirmPassword = document.getElementById('confirmPassword');
    const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])(?=.*[A-Z])[A-Za-z\d!@#$%^&*]{8,}$/;

    function clearErrors(element) {
        const parent = element.parentElement;
        const errorDiv = parent.querySelector('.alert-danger');
        if (errorDiv) {
            errorDiv.remove();
        }
    }

    function showError(element, message) {
        clearErrors(element);
        let errorDiv = document.createElement('div');
        errorDiv.className = 'alert alert-danger';
        errorDiv.innerText = message;
        element.parentElement.appendChild(errorDiv);
    }

    // function clearErrors() {
    //     const errors = document.querySelectorAll('.alert-danger');
    //     errors.forEach(error => error.remove());
    // }

    function validateCurrentPassword() {
        clearErrors(currentPassword);
        if (!passwordRegex.test(currentPassword.value)) {
            showError(currentPassword, "Current password is required.");
            return false;
        }
        return true;
    }

    function validateNewPassword() {
        clearErrors(newPassword);
        if (!passwordRegex.test(newPassword.value)) {
            showError(
                newPassword,
                "New password must be at least 8 characters long, contain at least one uppercase letter, one number, and one special character."
            );
            return false;
        }
        return true;
    }

    function validateConfirmPassword() {
        clearErrors(confirmPassword);
        if (confirmPassword.value !== newPassword.value) {
            showError(confirmPassword, "Passwords do not match.");
            return false;
        }
        return true;
    }

    function validateForm() {
        const isCurrentPasswordValid = validateCurrentPassword();
        const isNewPasswordValid = validateNewPassword();
        const isConfirmPasswordValid = validateConfirmPassword();

        return isCurrentPasswordValid && isNewPasswordValid && isConfirmPasswordValid;
    }

    form.addEventListener('submit', function (event) {
        event.preventDefault();


        if (validateForm()) {
            form.submit();
        }
    });

    currentPassword.addEventListener('input', () => {
        validateCurrentPassword();
    });
    newPassword.addEventListener('input', () => {
        validateCurrentPassword();
    });
    confirmPassword.addEventListener('input', () => {
        validateCurrentPassword();
    });

});