document.addEventListener("DOMContentLoaded", function () {
    const editButton = document.getElementById("editButton");
    const saveButton = document.getElementById("saveButton");
    const cancelButton = document.getElementById("cancelButton");
    const inputs = document.querySelectorAll("input");
    const emailInput = document.getElementById("email");
    const genderInput = document.getElementById("gender");
    editButton.addEventListener("click", () => {
        inputs.forEach(input => {
            input.removeAttribute("readonly");
        });
        genderInput.removeAttribute("disabled")
        editButton.classList.add("d-none");
        saveButton.classList.remove("d-none");
        cancelButton.classList.remove("d-none");
    });

    cancelButton.addEventListener("click", () => {
        inputs.forEach(input => input.setAttribute("readonly", "readonly"));
        editButton.classList.remove("d-none");
        saveButton.classList.add("d-none");
        cancelButton.classList.add("d-none");
    });
});