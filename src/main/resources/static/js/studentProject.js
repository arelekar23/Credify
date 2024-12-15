$(document).ready(() => {
    const xhr = new XMLHttpRequest();
    const fetchUniList = () => {
        xhr.open("GET", "/api/university/list", true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    const data = JSON.parse(xhr.responseText);
                    const universitiesDropdown = $("#university");
                    universitiesDropdown.empty();
                    universitiesDropdown.append('<option disabled selected value=""> -- select an option -- </option>');
                    data.forEach(organization => {
                        universitiesDropdown.append(`<option value="${organization.id}">${organization.name}</option>`);
                    });
                } else {
                    alert("Failed to fetch universities: " + xhr.responseText);
                }
            }
        };
        xhr.send();
    };
    fetchUniList();

    $("#university").on("change", function () {
        const selectedUniversity = $(this).val();

        if (selectedUniversity) {
            const xhr = new XMLHttpRequest();
            xhr.open("GET", `/api/majors/university/${selectedUniversity}`, true);

            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        const majors = JSON.parse(xhr.responseText);
                        const majorDropdown = $("#major");
                        majorDropdown.empty();
                        majorDropdown.append('<option disabled selected value=""> -- select an option -- </option>');
                        majors.forEach(major => {
                            majorDropdown.append(`<option value="${major.id}">${major.name}</option>`);
                        });
                    } else {
                        alert("Failed to fetch majors: " + xhr.responseText);
                    }
                }
            };
            xhr.send();
        } else {
            const majorDropdown = $("#major");
            majorDropdown.empty();
            majorDropdown.append('<option disabled selected value=""> -- select a major -- </option>');
        }
    });
    $("#projectType").on("change", function () {
        const projectType = $(this).val();
        if (projectType == "Personal") {
            document.getElementById("uniDiv").style.display = "none";
            document.getElementById("profDiv").style.display = "none";
        } else {
            document.getElementById("uniDiv").style.display = "block";
            document.getElementById("profDiv").style.display = "block";

            xhr.open("GET", "/api/university/list", true);

            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        const data = JSON.parse(xhr.responseText);
                        const universitiesDropdown = $("#university");
                        universitiesDropdown.empty();
                        universitiesDropdown.append('<option disabled selected value=""> -- select an option -- </option>');

                        data.forEach(organization => {
                            universitiesDropdown.append(`<option value="${organization.id}">${organization.name}</option>`);
                        });
                    } else {
                        alert("Failed to fetch universities: " + xhr.responseText);
                    }
                }
            };
            xhr.send();
        }
    });

    $("#projectType").trigger("change");

    $("#university").on("change", function () {
        const selectedUniversity = $(this).val();
        $.get(`/api/university/users/by-role?role=Professor&orgId=${selectedUniversity}`, function (data) {
            const professorDropdown = $("#professor");
            professorDropdown.empty();
            professorDropdown.append('<option disabled selected value=""> -- select an option -- </option>');
            data.forEach(professor => {
                const user = professor.user;
                const person = user.person;
                professorDropdown.append(`<option value="${professor.id}">${person.firstName} ${person.lastName} (${user.username})</option>`);
            });
        }).fail((xhr) => {
            alert("Failed to fetch users: " + xhr.responseText);
        });
    });
});
