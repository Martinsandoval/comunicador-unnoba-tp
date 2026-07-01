#!/usr/bin/env bash
set -e

PROJECT="/Users/martinsandoval/workspace/comunicadorMartin"
BUILD="/tmp/comunicador-build"
WAR="/tmp/comunicador2.war"
JAVA8="/Library/Java/JavaVirtualMachines/temurin-8.jdk/Contents/Home/bin"
GF="$HOME/glassfish/glassfish4/glassfish"
ASADMIN="$GF/bin/asadmin"
APP="comunicador2"
CP="$GF/lib/javaee.jar:$GF/modules/javax.faces.jar:$BUILD/WEB-INF/lib/primefaces-5.3.jar:$BUILD/WEB-INF/classes"

# ── 1. Sync web resources (never touch WEB-INF/classes or WEB-INF/lib) ────────
echo "==> Syncing web resources..."
rsync -a \
  --exclude "WEB-INF/classes/" \
  --exclude "WEB-INF/lib/" \
  "$PROJECT/web/" "$BUILD/"

# ── 2. Sync non-Java source files ─────────────────────────────────────────────
cp "$PROJECT/src/java/bundle/bundle.properties" "$BUILD/WEB-INF/classes/bundle/bundle.properties"
cp "$PROJECT/src/conf/persistence.xml"          "$BUILD/WEB-INF/classes/META-INF/persistence.xml"

# ── 3. Recompile Java if any source is newer than its class file ───────────────
NEEDS_COMPILE=false
while IFS= read -r -d '' src; do
  rel="${src#$PROJECT/src/java/}"
  cls="$BUILD/WEB-INF/classes/${rel%.java}.class"
  if [ ! -f "$cls" ] || [ "$src" -nt "$cls" ]; then
    NEEDS_COMPILE=true
    break
  fi
done < <(find "$PROJECT/src/java" -name "*.java" -print0)

if $NEEDS_COMPILE; then
  echo "==> Compiling Java sources..."
  JAVA_SOURCES=$(find "$PROJECT/src/java" -name "*.java" | tr '\n' ' ')
  $JAVA8/javac -encoding UTF-8 -cp "$CP" -d "$BUILD/WEB-INF/classes" $JAVA_SOURCES
  echo "    Compiled."
else
  echo "==> No Java changes, skipping compile."
fi

# ── 4. Package WAR ─────────────────────────────────────────────────────────────
echo "==> Building WAR..."
cd "$BUILD"
$JAVA8/jar -cf "$WAR" .

# ── 5. Redeploy ────────────────────────────────────────────────────────────────
echo "==> Deploying..."
$ASADMIN undeploy $APP 2>&1 | grep -v "^$" || true
$ASADMIN deploy "$WAR" 2>&1 | grep -v "PER01\|WELD\|JSF1074"

echo ""
echo "✓ Live at http://localhost:9090/$APP/"
