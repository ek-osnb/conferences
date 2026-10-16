import { getConferences } from "../api/conferences.js";
import { createAlert } from "../components/Alert.js";
import { createConferenceCard } from "../components/ConferenceCard.js";

export async function renderConferencesPage(container) {
    container.innerHTML = `
        <h1>Conferences</h1>
        <p class="lead text-measure">Find a conference, see the programme and register.</p>

        <form class="row g-2 mb-4" role="search">
            <div class="col-sm-8 col-md-6">
                <input class="form-control" type="search" name="q"
                       placeholder="Search by name or city" aria-label="Search by name or city">
            </div>
            <div class="col-auto">
                <button class="btn btn-primary" type="submit">Search</button>
            </div>
        </form>

        <div class="results">Loading...</div>
    `;

    const form = container.querySelector("form");
    const results = container.querySelector(".results");

    form.addEventListener("submit", (event) => {
        event.preventDefault();
        showConferences(form.elements.q.value.trim());
    });

    async function showConferences(search) {
        try {
            const conferences = await getConferences(search);
            results.replaceChildren(createGrid(conferences, search));
        } catch (error) {
            results.replaceChildren(createAlert({ message: `Could not load the conferences. ${error.message}` }));
        }
    }

    await showConferences("");
}

function createGrid(conferences, search) {
    if (conferences.length === 0) {
        const empty = document.createElement("p");
        empty.className = "text-body-secondary";
        empty.textContent = `No conferences match "${search}".`;
        return empty;
    }

    const grid = document.createElement("div");
    grid.className = "row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4";

    for (const conference of conferences) {
        const column = document.createElement("div");
        column.className = "col";
        column.append(createConferenceCard({ conference }));
        grid.append(column);
    }

    return grid;
}
