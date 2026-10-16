#!/usr/bin/env bash
set -euo pipefail

VERSION="${1:?usage: $0 <version>   (latest: npm view bootstrap version)}"
DIR="$(cd "$(dirname "$0")/.." && pwd)/src/vendor"
BASE="https://cdn.jsdelivr.net/npm/bootstrap@$VERSION/dist"
mkdir -p "$DIR"

TMP="$(mktemp)"
trap 'rm -f "$TMP"' EXIT

curl -fsSL "$BASE/css/bootstrap.min.css" | grep -v 'sourceMappingURL=' > "$TMP"

chmod 644 "$TMP"
mv "$TMP" "$DIR/bootstrap.min.css"
echo "Saved Bootstrap $VERSION to $DIR"
