[ ! -f dist/index.bundle.js ]             && npx webpack
[ src/index.js -nt dist/index.bundle.js ] && npx webpack
rm -rf target/public/
clojure -A:cljsjs -m figwheel.main -b dev -r
