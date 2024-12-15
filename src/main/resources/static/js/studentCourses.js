$(document).ready(() => {
    const xhr = new XMLHttpRequest();

    // Fetch and populate the university dropdown
    const fetchUniList = () => {
        xhr.open("GET", "/api/university/list", true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    const data = JSON.parse(xhr.responseText);
                    const universitiesDropdown = $("#university");
                    universitiesDropdown.empty();
                    universitiesDropdown.append('<option disabled selected value=""> -- select an option -- </option>');
                    data.forEach((organization) => {
                        universitiesDropdown.append(
                            `<option value="${organization.id}">${organization.name}</option>`
                        );
                    });

                    // Check if the URL already has a "university" parameter and set it as selected
                    const urlParams = new URLSearchParams(window.location.search);
                    const selectedUniversity = urlParams.get("university");
                    if (selectedUniversity) {
                        universitiesDropdown.val(selectedUniversity);
                        fetchMajorsForUniversity(selectedUniversity); // Automatically fetch majors if filter exists
                    }
                } else {
                    alert("Failed to fetch universities: " + xhr.responseText);
                }
            }
        };
        xhr.send();
    };

    // Handle university selection change
    $("#university").on("change", () => {
        const selectedUniversity = $("#university").val();

        if (selectedUniversity) {
            // Update the URL to include the selected university as a query parameter
            const url = new URL(window.location.href);
            url.searchParams.set("university", selectedUniversity);
            window.history.pushState({}, "", url);

            // Fetch majors for the selected university
            fetchMajorsForUniversity(selectedUniversity);
        }
    });

    // Fetch majors for a specific university
    const fetchMajorsForUniversity = (universityId) => {
        const xhr = new XMLHttpRequest();
        xhr.open("GET", `/api/majors/university/${universityId}`, true);
        xhr.onreadystatechange = () => {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    const majors = JSON.parse(xhr.responseText);
                    const tableBody = $("#majorsTableBody");
                    tableBody.empty();

                    if (majors.length > 0) {
                        majors.forEach((major) => {
                            const row = `
                                <tr>
                                    <td>${major.id}</td>
                                    <td>${major.name}</td>
                                    <td>${major.domain}</td>
                                    <td>${major.numberOfCredits}</td>
                                    <td>${major.fee}</td>
                                    <td class="status-column" data-major-id="${major.id}">Loading...</td>
                                    <td>
                                        <form action="/student/courses/apply" method="post" class="d-inline">
                                            <input type="hidden" name="majorId" value="${major.id}" />
                                            <button type="submit" class="btn btn-primary btn-sm apply-button">Apply</button>
                                        </form>
                                    </td>
                                </tr>
                            `;
                            tableBody.append(row);
                        });

                        fetchApplicationStatuses();
                    } else {
                        const noResultsRow = `
                            <tr>
                                <td colspan="7" class="text-center">No majors found for the selected university.</td>
                            </tr>
                        `;
                        tableBody.append(noResultsRow);
                    }
                } else {
                    alert("Failed to fetch majors: " + xhr.responseText);
                }
            }
        };
        xhr.send();
    };

    const fetchApplicationStatuses = () => {
        $("#majorsTableBody .status-column").each((_, element) => {
            const statusCell = $(element);
            const majorId = statusCell.data("major-id");
            const row = statusCell.closest("tr");

            $.ajax({
                url: `/api/university-applications/status/${majorId}`,
                type: "GET",
                success: (status) => {
                    statusCell.text(status);

                    const applyButton = row.find(".apply-button");

                    if (status === "SENT" || status === "UNDER_REVIEW") {
                        applyButton.prop("disabled", true);
                        applyButton.text("Applied");
                    } else if (status === "ACCEPTED") {
                        applyButton.prop("enabled", true);
                        applyButton.text("Finalize University");

                        applyButton.off("click").on("click", function (event) {
                            event.preventDefault();

                            finalizeUniversity(majorId);
                        });
                    } else if (status === "OFFER_ACCEPTED" || status === "COMPLETED" || status === "REJECTED") {
                        applyButton.remove();
                    }
                },
                error: () => {
                    statusCell.text("Error fetching status");
                },
            });
        });
    };
    const finalizeUniversity = (majorId) => {
        // Make an API call to finalize the university
        $.ajax({
            url: `/student/courses/final`,
            type: "POST",
            data: { majorId: majorId },
            success: (response) => {
                fetchApplicationStatuses();
            },
            error: (xhr, status, error) => {
                alert("Failed to finalize the university: " + error);
            }
        });
    };

    fetchUniList();
});