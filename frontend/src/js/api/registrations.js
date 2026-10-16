import { getJSON, sendJSON } from "./http.js";

export function getAvailability(conferenceId) {
    return getJSON(`/api/conferences/${conferenceId}/availability`);
}

export function register(conferenceId, { name, email }) {
    return sendJSON("POST", `/api/conferences/${conferenceId}/registrations`, { name, email });
}
