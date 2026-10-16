export function renderNotFoundPage(container) {
    container.innerHTML = `
        <h1>Page not found</h1>
        <p>There is no page at this address.</p>
        <a class="btn btn-primary" href="/" data-navigo>See all conferences</a>
    `;
}
