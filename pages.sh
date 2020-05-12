clj -A:build-static -m figwheel.main -O simple --build-once static
[ ! -f target/node/static/static-main.js ] && read -p "Build failed.  Press enter to continue a pointless endeavor."
node -r "node-localstorage/register" target/node/static/static-main.js target/public/
powershell -f versionize-html.ps1 target/public/index.html
powershell -f versionize-html.ps1 target/public/w01.html
powershell -f versionize-html.ps1 target/public/w02.html
powershell -f versionize-html.ps1 target/public/w03.html
powershell -f versionize-html.ps1 target/public/w04.html
powershell -f versionize-html.ps1 target/public/w05.html
powershell -f versionize-html.ps1 target/public/w06.html
