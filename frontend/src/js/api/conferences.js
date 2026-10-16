import { getJSON } from "./http.js";

export function getConferences(search) {
    return getJSON(search ? `/api/conferences?q=${encodeURIComponent(search)}` : "/api/conferences");
}

export function getConference(id) {
    return getJSON(`/api/conferences/${id}`);
}
