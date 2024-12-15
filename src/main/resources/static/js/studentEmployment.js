$(document).ready(() => {
    const xhr = new XMLHttpRequest();

    const fetchUniList = () => {
        xhr.open("GET", "/api/employment/list", true);
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
});