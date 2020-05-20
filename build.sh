[ ! -f dist/index.bundle.js ]             && npx webpack --mode=production
[ src/index.js -nt dist/index.bundle.js ] && npx webpack --mode=production
[ src/index-static.js -nt dist/static.bundle.js ] && npx webpack --mode=production
rm -rf target/public/
mkdir target/public
# Add  -s to start server, then ctrl+c to break.
clojure -A:cljsjs -m figwheel.main -O advanced --build-once dev
cp --recursive resources/public/* target/public/
rm -rf target/public/cljs-out/dev/
powershell -f versionize-html.ps1 target/public/index.html
./pages.sh
cp --recursive target/public/* ../slate-cljs.gh-pages/
#tree -- ../slate-cljs.gh-pages/
