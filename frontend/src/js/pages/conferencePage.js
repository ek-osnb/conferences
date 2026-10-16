import { getConference } from "../api/conferences.js";
import { getAvailability, register } from "../api/registrations.js";
import { getConferenceTalks } from "../api/talks.js";
import { createAlert } from "../components/Alert.js";
import { createRegistrationForm } from "../components/RegistrationForm.js";
import { createTalkList } from "../components/TalkList.js";
import { formatDateRange } from "../format.js";
import { setTitle } from "../router.js";

export async function renderConferencePage(container, { id }) {
    container.textContent = "Loading...";

    let conference;
    let talks;
    let availability;

    try {
        [conference, talks, availability] = await Promise.all([
            getConference(id),
            getConferenceTalks(id),
            getAvailability(id)
        ]);
    } catch (error) {
        const message = error.status === 404
            ? "This conference does not exist."
            : `Could not load the conference. ${error.message}`;
        container.replaceChildren(createAlert({ message }));
        return;
    }

    setTitle(conference.name);

    container.innerHTML = `
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/" data-navigo>Conferences</a></li>
                <li class="breadcrumb-item active" aria-current="page"></li>
            </ol>
        </nav>

        <div class="row g-4">
            <div class="col-lg-8">
                <h1 class="conference-name"></h1>
                <p class="text-body-secondary conference-when"></p>
                <p class="lead text-measure conference-description"></p>

                <h2 class="h4 mt-4 mb-1">Programme</h2>
                <p class="small text-body-secondary mb-3">Example talks for this demo, not the real programme.</p>
                <div class="talks"></div>
            </div>

            <div class="col-lg-4">
                <div class="card">
                    <div class="card-body">
                        <h2 class="h5 card-title">Register</h2>
                        <p class="seats"></p>
                        <div class="registration"></div>
                    </div>
                </div>
            </div>
        </div>
    `;

    container.querySelector(".breadcrumb-item.active").textContent = conference.name;
    container.querySelector(".conference-name").textContent = conference.name;
    container.querySelector(".conference-when").textContent =
        `${formatDateRange(conference.startDate, conference.endDate)} · ${[conference.venue, conference.city].filter(Boolean).join(", ")}`;
    container.querySelector(".conference-description").textContent = conference.description;
    container.querySelector(".talks").append(createTalkList({ talks }));

    const seats = container.querySelector(".seats");
    showSeats(seats, availability);

    const form = createRegistrationForm({
        async onSubmit(data) {
            const registration = await register(id, data);
            showSeats(seats, await getAvailability(id));
            return `You are registered for ${registration.conferenceName}.`;
        }
    });

    container.querySelector(".registration").append(form);
}

function showSeats(element, { seatsLeft, capacity }) {
    const badge = document.createElement("span");

    if (seatsLeft === 0) {
        badge.className = "badge text-bg-danger";
        badge.textContent = "Fully booked";
    } else if (seatsLeft <= 5) {
        badge.className = "badge text-bg-warning";
        badge.textContent = `Only ${seatsLeft} ${seatsLeft === 1 ? "seat" : "seats"} left`;
    } else {
        badge.className = "badge text-bg-success";
        badge.textContent = `${seatsLeft} of ${capacity} seats left`;
    }

    element.replaceChildren(badge);
}
