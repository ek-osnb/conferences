export function createAlert({ type = "danger", message }) {
    const element = document.createElement("div");

    element.className = `alert alert-${type}`;
    element.setAttribute("role", type === "danger" ? "alert" : "status");
    element.textContent = message;

    return element;
}
