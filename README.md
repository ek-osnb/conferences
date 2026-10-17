# Conferences

A small app for tech conferences. You can see the conferences, look at the talks and register with your name and email.

- `conferences-api` is the Spring Boot backend (MySQL)
- `frontend` is plain JavaScript, served by Nginx
- Caddy sits in front of both, so everything runs on http://localhost:8088

## Getting started

Copy the env file and change the passwords in it:

```bash
cp .env.example .env
```

### Spring Boot in IntelliJ

```bash
docker compose -f compose.dev.yaml up -d
```

This starts MySQL, Nginx and Caddy. Run `ConferencesApplication` from IntelliJ and open http://localhost:8088. Spring Boot reads the passwords from `.env` as well, so there's nothing to set up in IntelliJ.

Nginx serves `frontend/src` directly, so you just save and reload the page.

### Everything in Docker

```bash
docker compose up -d --build
```

Remember `--build` when you have changed code.

### Stopping

```bash
docker compose down
docker compose -f compose.dev.yaml down
```

Add `-v` if you want to delete the database too.

## Tests

```bash
cd conferences-api
./mvnw test
```

The tests use H2, so you don't need Docker running.

## Endpoints

```
GET  /api/conferences?q=java
GET  /api/conferences/{id}
GET  /api/conferences/{id}/talks
GET  /api/conferences/{id}/availability
POST /api/conferences/{id}/registrations    {"name": "...", "email": "..."}
```

## How the code is organised

The backend has one package per feature: `conference`, `talk` and `registration`. Classes are package-private. When one feature needs another, it uses its public `XxxApi` interface (like `ConferenceApi`) and only stores the id.

The sample data is created on startup in `ConferenceInitData` and `TalkInitData`.

In the frontend, the routes are in `js/app.js`, each page is a file in `js/pages`, and all fetch calls go through `js/api`.

## Releases

When you push a tag like `v0.1.0`, GitHub Actions builds both images and pushes them to GitHub Container Registry. The compose files in `deploy/` run those images on a server.

To create a new release, run:

```bash
git tag v0.1.0
git push origin v0.1.0
```
This will trigger the GitHub Actions workflow.
