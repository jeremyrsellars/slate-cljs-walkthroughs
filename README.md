# Slate text editor walkthroughs in ClojureScript

The walkthroughs of SlateJS translated to ClojureScript because there are lots of reasons to prefer ClojureScript.

* https://docs.slatejs.org/walkthroughs/01-installing-slate
    * [slate_01_installing_slate.cljs](src/slatecljs/slate_01_installing_slate.cljs)

### What?

The ClojureScript launcher and code reloading is roughly based on the [Figwheel.main NPM Modules tutorial](https://figwheel.org/docs/npm.html).

### Usage

clojure -m figwheel.main -b dev -r
