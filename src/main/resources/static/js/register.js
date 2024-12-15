const updateFields = () => {
    const role = document.getElementById("role").value;
    const universityFields = document.getElementById("universityFields");
    const employerFields = document.getElementById("employerFields");

    universityFields.style.display = "none";
    employerFields.style.display = "none";

    if (role === "University-Admin") {
        universityFields.style.display = "block";
    } else if (role === "Employer-Admin") {
        employerFields.style.display = "block";
    }
};
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('registrationForm');
    const form1 = document.getElementById('registrationForm1');

    const firstName = document.getElementById('firstName');
    const lastName = document.getElementById('lastName');
    const email = document.getElementById('email');
    const universityName = document.getElementById('universityName');
    const employerName = document.getElementById('employerName');
    const username = document.getElementById('username');
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirmPassword');
    const confirmPasswordError = document.getElementById('confirmPasswordError');

    const nameRegex = /^[A-Z a-z]{2,}$/;
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
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

    function validateRole() {
        const role = document.getElementById('role');

        // Check if a role is selected
        if (role.value.trim() === '') {
            showError(role, 'Role is required.');
            return false;
        }
        clearErrors(role);
        return true;
    }
    function validateFirstName() {
        clearErrors();
        if (!nameRegex.test(firstName.value)) {
            showError(firstName, 'First name should contain only letters and be at least 2 characters long.');
            return false;
        }
        return true;
    }

    function validateLastName() {
        clearErrors();
        if (!nameRegex.test(lastName.value)) {
            showError(lastName, 'Last name should contain only letters and be at least 2 characters long.');
            return false;
        }
        return true;
    }

    // Validate Email Address
    function validateEmail() {
        clearErrors();
        if (!emailRegex.test(email.value)) {
            showError(email, 'Please enter a valid email address.');
            return false;
        }
        return true;
    }

    // Validate University Name (only if University Role is selected)
    function validateUniversityName() {
        if (document.getElementById('role').value === 'University-Admin') {
            if (universityName.value.trim() === '') {
                showError(universityName, 'University name is required for University role.');
                return false;
            } else {
                clearErrors(universityName);
                return true;
            }
        }
        return true;
    }

    // Validate Employer Name (only if Employer Role is selected)
    function validateEmployerName() {
        if (document.getElementById('role').value === 'Employer-Admin') {
            if (employerName.value.trim() === '') {
                showError(employerName, 'Employer name is required for Employer role.');
                return false;
            } else {
                clearErrors(employerName);
                return true;
            }
        }
        return true;
    }

    // Validate Username
    function validateUsername() {
        clearErrors();
        if (!usernameRegex.test(username.value)) {
            showError(username, 'Username must be alphanumeric and between 5 to 25 characters.');
            return false;
        }
        return true;
    }

    // Validate Password
    function validatePassword() {
        clearErrors();
        if (!passwordRegex.test(password.value)) {
            showError(password, 'Password must be at least 8 characters long, contain at least one uppercase letter, one number, and one special character.');
            return false;
        }
        return true;
    }

    function validateConfirmPassword() {
        clearErrors();
        if (confirmPassword.value !== password.value) {
            showError(confirmPassword, 'Passwords do not match');
            return false;
        }
        return true;
    }

    // Form Submission
    form.addEventListener('submit', function(event) {
        clearErrors(role);
        event.preventDefault();

        if (
            validateRole() &&
            validateFirstName() &&
            validateLastName() &&
            validateEmail() &&
            validateUsername() &&
            validatePassword() &&
            validateConfirmPassword() &&
            validateUniversityName() &&
            validateEmployerName()
        ) {
            form.submit();  // Submit the form if all validations pass
        }
    });

    // Live Validation Event Listeners
    firstName.addEventListener('input', validateFirstName);
    lastName.addEventListener('input', validateLastName);
    email.addEventListener('input', validateEmail);
    username.addEventListener('input', validateUsername);
    password.addEventListener('input', validatePassword);
    confirmPassword.addEventListener('input', validateConfirmPassword);
    employerName.addEventListener('input', validateEmployerName);
    universityName.addEventListener('input', validateUniversityName);

    // function updateFields() {
    //     const role = document.getElementById('role').value;
    //     const universityFields = document.getElementById('universityFields');
    //     const employerFields = document.getElementById('employerFields');
    //
    //     universityFields.style.display = role === 'University-Admin' ? 'block' : 'none';
    //     employerFields.style.display = role === 'Employer-Admin' ? 'block' : 'none';
    //
    //
    // }

    document.getElementById('role').addEventListener('change', validateRole, updateFields, validateEmployerName(), validateUniversityName());
    updateFields();


});