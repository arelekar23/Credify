$(document).ready(() => {
    const xhr = new XMLHttpRequest();
    let currentUsername="";
    const fetchUserList = () => {

        xhr.open("GET", "/api/users/list", true);

        xhr.onreadystatechange = () => {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    const data = JSON.parse(xhr.responseText);
                    const userListBody = document.getElementById("userListBody");
                    userListBody.innerHTML = "";

                    data.forEach(user => {
                        const userRow = `
                            <tr>
                                <td>${user.user.role.name}</td>
                                <td>${user.user.username}</td>
                                <td>${user.user.person.firstName}</td>
                                <td>${user.user.person.lastName}</td>
                                <td>${user.user.person.mobileNumber}</td>
                            </tr>`;
                        userListBody.innerHTML += userRow;
                    });
                } else {
                    alert("Failed to fetch users: " + xhr.responseText);
                }
            }
        };

        xhr.send();
    };

    fetchUserList();

    $("#role1").on("change", function () {
        const selectedRole = $(this).val();
        $.get(`/api/users/by-role?role=${selectedRole}`, function (data) {
            const usersDropdown = $("#users");
            usersDropdown.empty();
            usersDropdown.append('<option disabled selected value=""> -- select an option -- </option>');
            data.forEach(user => {
                usersDropdown.append(`<option value="${user.user.id}">${user.user.username}</option>`);
            });
        }).fail((xhr) => {
            alert("Failed to fetch users: " + xhr.responseText);
        });
    });

    $("#users").on("change", function () {
        const selectedUserId = $(this).val();
        $.get(`/api/users/details?id=${selectedUserId}`, function (data) {
            const person = data.person;
            currentUsername = data.username;
            $("#firstNameEdit").val(person.firstName || "");
            $("#lastNameEdit").val(person.lastName || "");
            $("#emailEdit").val(person.emailAddress || "");
            $("#mobileEdit").val(person.mobileNumber || "");
            $("#genderEdit").val(person.gender || "");
            $("#ageEdit").val(person.age || "");
            $("#countryEdit").val(person.country || "");
            $("#usernameEdit").val(data.username || "");
        }).fail((xhr) => {
            alert("Failed to fetch user details: " + xhr.responseText);
        });
    });

    $("#submitForm").click(()=> {
        const formData = {
            role: $("#role").val(),
            person: {
                firstName: $("#firstName").val(),
                lastName: $("#lastName").val(),
                emailAddress: $("#email").val(),
                mobileNumber: $("#mobile").val(),
                gender: $("#gender").val(),
                age: $("#age").val(),
                country: $("#country").val(),
            },
            user: {
                username: $("#username").val(),
                password: $("#password").val(),
            }
        };

        // Confirm password validation
        if ($("#password").val() !== $("#confirmPassword").val()) {
            $("#confirmPasswordError").show();
            return;
        } else {
            $("#confirmPasswordError").hide();
        }

        $.ajax({
            url: "/api/users/create",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(formData),
            success: function (response) {
                alert(response);
                $('#registrationForm')[0].reset();
                $('#exampleModal').modal('hide');
                fetchUserList();
            },
            error: function (xhr, status, error) {
                alert("Error: " + xhr.responseText);
            }
        });
    });

    $("#submitEditForm").click(()=> {
        const updatedUser = {
            // user: {
            //     username: document.getElementById("usernameEdit").value,
            //     currentUsername: currentUsername
            // },
            person: {
                firstName: document.getElementById("firstNameEdit").value,
                lastName: document.getElementById("lastNameEdit").value,
                emailAddress: document.getElementById("emailEdit").value,
                mobileNumber: document.getElementById("mobileEdit").value,
                gender: document.getElementById("genderEdit").value,
                age: document.getElementById("ageEdit").value,
                country: document.getElementById("countryEdit").value
            },
            role: document.getElementById("role1").value
        };

        fetch("/api/users/edit", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(updatedUser)
        })
            .then(response => response.text())
            .then(result => {
                alert(result);
                $('#exampleModal2').modal('hide');
                fetchUserList();
            })
            .catch(err => alert("Failed to update user: " + err));
    });
});