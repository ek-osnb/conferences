import { router, page } from "./router.js";
import { renderConferencesPage } from "./pages/conferencesPage.js";
import { renderConferencePage } from "./pages/conferencePage.js";
import { renderNotFoundPage } from "./pages/notFoundPage.js";

router
    .on("/", page(renderConferencesPage, "Conferences"))
    .on("/conferences/:id", page(renderConferencePage, "Conference"))
    .notFound(page(renderNotFoundPage, "Not found"))
    .resolve();
