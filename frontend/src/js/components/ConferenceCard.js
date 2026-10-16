import { formatDateRange } from "../format.js";

export function createConferenceCard({ conference }) {
    const element = document.createElement("article");

    element.className = "card h-100 card-link-hover";

    element.innerHTML = `
        <div class="card-body">
            <h2 class="h5 card-title"><a class="stretched-link text-decoration-none" data-navigo></a></h2>
            <p class="card-subtitle mb-2 text-body-secondary"></p>
            <p class="card-text"></p>
        </div>
    `;

    const link = element.querySelector("a");
    link.href = `/conferences/${conference.id}`;
    link.textContent = conference.name;

    element.querySelector(".card-subtitle").textContent =
        `${formatDateRange(conference.startDate, conference.endDate)} · ${conference.city}`;
    element.querySelector(".card-text").textContent = conference.description;

    return element;
}
