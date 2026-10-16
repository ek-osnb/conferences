import { getJSON } from "./http.js";

export function getConferenceTalks(conferenceId) {
    return getJSON(`/api/conferences/${conferenceId}/talks`);
}
