# Slate text editor walkthroughs in ClojureScript

The walkthroughs of SlateJS translated to ClojureScript because there are lots of reasons to prefer ClojureScript.

* [Introduction](https://jeremyrsellars.github.io/slate-cljs-walkthroughs/#)
* [01 Installing Slate](https://jeremyrsellars.github.io/slate-cljs-walkthroughs/#w01)
* [02 Event handlers](https://jeremyrsellars.github.io/slate-cljs-walkthroughs/#w02)
* [03 Defining custom elements](https://jeremyrsellars.github.io/slate-cljs-walkthroughs/#w03)
* [04 Applying custom formatting](https://jeremyrsellars.github.io/slate-cljs-walkthroughs/#w04)
* [05 Custom commands](https://jeremyrsellars.github.io/slate-cljs-walkthroughs/#w05)
* [06 Saving to a database](https://jeremyrsellars.github.io/slate-cljs-walkthroughs/#w06)

Also, you may want to take a look at these files for more on getting Slate, React, NPM, WebPack, and ClojureScript working together.

* NPM and WebPack
    * [package.json](package.json)
    * [webpack.config.js](webpack.config.js)
    * [src/js/index.js](src/js/index.js)
* Figwheel and ClojureScript
	* [deps.edn](deps.edn)
	* [dev.cljs.edn](dev.cljs.edn)
	* Along with the [Figwheel.main NPM Modules tutorial](https://figwheel.org/docs/npm.html).

### Usage

    clojure -m figwheel.main -b dev -r


### Webpack?

After editing [src/js/index.js](src/js/index.js), you'll need to run webpack again. See [here](https://figwheel.org/docs/npm.html#create-the-indexjs-file).

    npx webpack  --mode=development

or

    npx webpack


### Static pages

    clj -A:build-static -m cljs.main -O simple -t nodejs -v -c slatecljs.static-site

    node out/main.js
