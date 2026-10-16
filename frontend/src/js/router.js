import Navigo from "navigo";

export const router = new Navigo("/");

const APP_NAME = "Conferences";
const app = document.querySelector("#app");

document.addEventListener("click", (event) => {
    const link = event.target.closest('a[data-navigo]:not([data-navigo="false"]):not([target="_blank"])');
    const isPlainClick = event.button === 0 && !event.metaKey && !event.ctrlKey && !event.shiftKey && !event.altKey;

    if (!link || !isPlainClick || event.defaultPrevented) {
        return;
    }

    event.preventDefault();
    router.navigate(link.getAttribute("href"));
});

export function page(render, title) {
    return async (match) => {
        setTitle(title);
        window.scrollTo(0, 0);

        await render(app, match.data ?? {});
    };
}

export function setTitle(title) {
    document.title = title ? `${title} · ${APP_NAME}` : APP_NAME;
}
