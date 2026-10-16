const dateFormat = new Intl.DateTimeFormat("en-GB", { day: "numeric", month: "long", year: "numeric" });
const dateTimeFormat = new Intl.DateTimeFormat("en-GB", {
    weekday: "short", day: "numeric", month: "short", hour: "2-digit", minute: "2-digit"
});

function toDate(isoDate) {
    return new Date(`${isoDate}T00:00`);
}

export function formatDateRange(startDate, endDate) {
    return dateFormat.formatRange(toDate(startDate), toDate(endDate));
}

export function formatDateTime(isoDateTime) {
    return dateTimeFormat.format(new Date(isoDateTime));
}

export function formatLevel(level) {
    return level.charAt(0) + level.slice(1).toLowerCase();
}
