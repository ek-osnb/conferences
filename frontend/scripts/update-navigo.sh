#!/usr/bin/env bash
set -euo pipefail

VERSION="${1:?usage: $0 <version>   (latest: npm view navigo version)}"
OUT="$(cd "$(dirname "$0")/.." && pwd)/src/vendor/navigo.js"
TMP="$(mktemp)"
trap 'rm -f "$TMP"' EXIT

{
  echo "// Navigo $VERSION - downloaded by scripts/update-navigo.sh, do not edit"
  curl -fsSL "https://cdn.jsdelivr.net/npm/navigo@$VERSION/+esm" | grep -v '^//# sourceMappingURL='
} > "$TMP"

chmod 644 "$TMP"
mkdir -p "$(dirname "$OUT")"
mv "$TMP" "$OUT"
echo "Saved Navigo $VERSION to $OUT"
