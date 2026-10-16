import { createAlert } from "./Alert.js";

export function createRegistrationForm({ onSubmit }) {
    const form = document.createElement("form");

    form.noValidate = true;

    form.innerHTML = `
        <div class="form-message"></div>
        <div class="mb-3">
            <label class="form-label" for="registration-name">Name</label>
            <input class="form-control" id="registration-name" name="name" autocomplete="name">
            <div class="invalid-feedback"></div>
        </div>
        <div class="mb-3">
            <label class="form-label" for="registration-email">Email</label>
            <input class="form-control" id="registration-email" name="email" type="email" autocomplete="email">
            <div class="invalid-feedback"></div>
        </div>
        <button class="btn btn-primary w-100" type="submit">Register</button>
    `;

    const message = form.querySelector(".form-message");
    const button = form.querySelector("button");

    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        clearErrors();
        button.disabled = true;

        try {
            const text = await onSubmit({
                name: form.elements.name.value.trim(),
                email: form.elements.email.value.trim()
            });

            form.reset();
            message.replaceChildren(createAlert({ type: "success", message: text }));
        } catch (error) {
            showErrors(error);
        } finally {
            button.disabled = false;
        }
    });

    function showErrors(error) {
        const fieldErrors = error.fieldErrors ?? {};

        for (const [field, text] of Object.entries(fieldErrors)) {
            const input = form.elements[field];

            if (input) {
                input.classList.add("is-invalid");
                input.nextElementSibling.textContent = text;
            }
        }

        message.replaceChildren(createAlert({ message: error.message }));
    }

    function clearErrors() {
        message.replaceChildren();

        for (const input of form.querySelectorAll(".is-invalid")) {
            input.classList.remove("is-invalid");
        }
    }

    return form;
}
