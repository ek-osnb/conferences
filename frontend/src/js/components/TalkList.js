import { formatDateTime, formatLevel } from "../format.js";

const LEVEL_STYLES = {
    BEGINNER: "text-bg-success",
    INTERMEDIATE: "text-bg-warning",
    ADVANCED: "text-bg-danger"
};

export function createTalkList({ talks }) {
    if (talks.length === 0) {
        const empty = document.createElement("p");
        empty.className = "text-body-secondary";
        empty.textContent = "No talks yet.";
        return empty;
    }

    const element = document.createElement("ul");

    element.className = "list-group";

    for (const talk of talks) {
        element.append(createTalkItem(talk));
    }

    return element;
}

function createTalkItem(talk) {
    const item = document.createElement("li");

    item.className = "list-group-item py-3";

    item.innerHTML = `
        <div class="d-flex justify-content-between align-items-start gap-2">
            <small class="text-body-secondary talk-when"></small>
            <span class="badge talk-level"></span>
        </div>
        <h3 class="h6 mt-1 mb-1 talk-title"></h3>
        <p class="mb-1 small talk-description"></p>
        <p class="mb-0 small fw-semibold talk-speaker"></p>
    `;

    item.querySelector(".talk-when").textContent = `${formatDateTime(talk.startsAt)} · ${talk.room}`;
    item.querySelector(".talk-title").textContent = talk.title;
    item.querySelector(".talk-description").textContent = talk.description;
    item.querySelector(".talk-speaker").textContent = talk.speaker;

    const level = item.querySelector(".talk-level");
    level.classList.add(LEVEL_STYLES[talk.level] ?? "text-bg-secondary");
    level.textContent = formatLevel(talk.level);

    return item;
}
