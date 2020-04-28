rm -rf target/public/
mkdir target/public
clojure -m figwheel.main -O whitespace --build-once dev
cp --recursive resources/public/* target/public/
rm -rf target/public/cljs-out/dev/
powershell -f versionize-html.ps1 target/public/index.html
tree
cp --recursive target/public/* ../slate-cljs.gh-pages/
tree -- ../slate-cljs.gh-pages/
