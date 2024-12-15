$(document).ready(() => {
    const xhr = new XMLHttpRequest();

    const fetchEmployerList = () => {
        xhr.open("GET", "/api/employer/list", true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    const data = JSON.parse(xhr.responseText);
                    const employerDropdown = $("#employer");
                    employerDropdown.empty();
                    employerDropdown.append('<option disabled selected value=""> -- select an option -- </option>');

                    data.forEach((organization) => {
                        employerDropdown.append(
                            `<option value="${organization.id}">${organization.name}</option>`
                        );
                    });

                    const urlParams = new URLSearchParams(window.location.search);
                    const selectedEmployer = urlParams.get("employer");
                    if (selectedEmployer) {
                        employerDropdown.val(selectedEmployer);
                        fetchJobsForEmployer(selectedEmployer); // Automatically fetch jobs if filter exists
                    }
                } else {
                    alert("Failed to fetch employers: " + xhr.responseText);
                }
            }
        };
        xhr.send();
    };

    $("#employer").on("change", () => {
        const selectedEmployer = $("#employer").val();
        if (selectedEmployer) {
            const url = new URL(window.location.href);
            url.searchParams.set("employer", selectedEmployer);
            window.history.pushState({}, "", url);

            fetchJobsForEmployer(selectedEmployer);
        }
    });

    // Fetch jobs for a specific employer
    const fetchJobsForEmployer = (employerId) => {
        xhr.open("GET", `/api/jobs/employer/${employerId}`, true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    const jobs = JSON.parse(xhr.responseText);
                    const tableBody = $("#jobsTableBody");
                    tableBody.empty(); // Clear existing rows

                    if (jobs.length > 0) {
                        jobs.forEach((job) => {
                            const row = `
                            <tr data-job-id="${job.id}">
                                <td>${job.id}</td>
                                <td>${job.name}</td>
                                <td>${job.domain}</td>
                                <td>${job.description}</td>
                                <td>${job.location || "N/A"}</td>
                                <td>${job.pay ? `$${job.pay}` : "N/A"}</td>
                                <td class="status-column">Loading...</td>
                                <td>
                                    <form action="/student/jobs/apply" method="post" class="d-inline">
                                        <input type="hidden" name="jobId" value="${job.id}" />
                                        <button type="submit" class="btn btn-primary btn-sm apply-button">Apply</button>
                                    </form>
                                </td>
                            </tr>
                        `;
                            tableBody.append(row);
                        });

                        // Fetch job statuses after loading jobs
                        fetchJobStatuses();
                    } else {
                        tableBody.append('<tr><td colspan="8" class="text-center">No jobs found for the selected employer.</td></tr>');
                    }
                } else {
                    alert(`Failed to fetch jobs: ${xhr.statusText}`);
                }
            }
        };
        xhr.send();
    };

    // Fetch job statuses and update apply buttons
    const fetchJobStatuses = () => {
        $("#jobsTableBody .status-column").each((_, element) => {
            const statusCell = $(element);
            const jobId = statusCell.closest("tr").data("job-id");

            $.ajax({
                url: `/api/employer-applications/status/${jobId}`,
                type: "GET",
                success: (status) => {
                    statusCell.text(status); // Update the status in the cell

                    const row = statusCell.closest("tr");
                    const applyButton = row.find(".apply-button");

                    if (status === "SENT" || status === "UNDER_REVIEW") {
                        applyButton.prop("disabled", true);
                        applyButton.text("Applied");
                    } else if (status === "ACCEPTED") {
                        // Enable the button for finalizing without changing its color
                        applyButton.prop("disabled", false);
                        applyButton.text("Accept Offer");

                        applyButton.off("click").on("click", function (event) {
                            event.preventDefault();

                            finalizeJob(jobId);
                        });
                    } else if (status === "OFFER_ACCEPTED" || status === "COMPLETED" || status === "REJECTED") {
                        applyButton.remove();
                    }
                },
                error: () => {
                    statusCell.text("Error fetching status"); // Show error in status cell
                },
            });
        });
    };

    const finalizeJob = (jobId) => {
        // Make an API call to finalize the university
        $.ajax({
            url: `/student/jobs/final`,
            type: "POST",
            data: { jobId: jobId },
            success: (response) => {
                fetchJobStatuses();
            },
            error: (xhr, status, error) => {
                alert("Failed to finalize the university: " + error);
            }
        });
    };
    // Initial call to populate the employer dropdown
    fetchEmployerList();
});