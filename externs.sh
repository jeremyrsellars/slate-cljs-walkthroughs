[ ! -f dist/externs.bundle.js ]                     && npx webpack --mode=development
[ src/index-externs.js -nt dist/externs.bundle.js ] && npx webpack --mode=development
[   -f target/node/externs/externs-main.js ] && rm target/node/externs/externs-main.js
clj -A:build-externs -m cljs.main -O simple -co externs.cljs.edn -t nodejs -v -c slatecljs.externs.scraper
[ ! -f target/node/externs/externs-main.js ] && read -p "Build failed.  Press enter to continue a pointless endeavor."
node target/node/externs/externs-main.js |less
node target/node/externs/externs-main.js .
