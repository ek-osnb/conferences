import { router, page } from "./router.js";
import { renderConferencesPage } from "./pages/conferencesPage.js";
import { renderNotFoundPage } from "./pages/notFoundPage.js";

router
    .on("/", page(renderConferencesPage, "Conferences"))
    .notFound(page(renderNotFoundPage, "Not found"))
    .resolve();
