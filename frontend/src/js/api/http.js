export class ApiError extends Error {
    constructor(status, message, fieldErrors = {}) {
        super(message);
        this.status = status;
        this.fieldErrors = fieldErrors;
    }
}

export function getJSON(url) {
    return request("GET", url);
}

export function sendJSON(method, url, body) {
    return request(method, url, body);
}

async function request(method, url, body) {
    const options = {
        method,
        headers: { Accept: "application/json" }
    };

    if (body !== undefined) {
        options.headers["Content-Type"] = "application/json";
        options.body = JSON.stringify(body);
    }

    let response;

    try {
        response = await fetch(url, options);
    } catch (error) {
        throw new ApiError(0, "Cannot reach the server. Check your connection and try again.");
    }

    if (response.status === 204) {
        return null;
    }

    const data = await readJson(response);

    if (!response.ok) {
        throw new ApiError(response.status, errorMessage(response.status, data), data?.errors ?? {});
    }

    return data;
}

async function readJson(response) {
    try {
        return await response.json();
    } catch (error) {
        return null;
    }
}

function errorMessage(status, data) {
    if (data?.detail) {
        return data.detail;
    }

    if (status === 502 || status === 503 || status === 504) {
        return `Cannot reach the API (HTTP ${status}). Is Spring Boot running?`;
    }

    if (status >= 500) {
        return `The server had a problem (HTTP ${status}). Try again in a moment.`;
    }

    return `The request failed (HTTP ${status}).`;
}
