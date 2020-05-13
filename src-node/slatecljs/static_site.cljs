(ns slatecljs.static-site
  (:require [clojure.string :as string]
            cljs.nodejs
            [react :refer [createElement]]
            [ReactDOMServer :refer [renderToStaticMarkup]]
            [slatecljs.common :as common]
            slatecljs.slatecljs-introduction
            slatecljs.slate-01-installing-slate
            slatecljs.slate-02-event-handlers
            slatecljs.slate-03-defining-custom-elements
            slatecljs.slate-04-applying-custom-formatting
            slatecljs.slate-05-executing-commands
            slatecljs.slate-06-saving
            [cljs-node-io.core :as io :refer [spit]]))

(cljs.nodejs/enable-util-print!)

(defn shell
  [{:keys [title anchor]} app-content]
  (createElement "html" #js {}

    (createElement "head" #js {}
      (createElement "meta" #js {:charSet "utf-8"})
      (createElement "title" #js {}
        (str title " - Slate with ClojureScript"))
      (createElement "link" #js {:rel "stylesheet" :href "css/style.css"}))

    (createElement "body" #js {}
      (createElement "h1" #js {:className "title"}
        "Slate with ClojureScript")
      (createElement "div" #js {:id "app"}
        app-content)

      (createElement "script"
        #js {:dangerouslySetInnerHTML #js {:__html (str "window.location.hash = " (pr-str anchor) ";")}})
      (createElement "script" #js {:src "cljs-out/dev-main.js"})

      (createElement "link" #js {:rel "stylesheet"
                                 :href "highlightjs/styles/obsidian.css"})

      (createElement "script"
        #js {:dangerouslySetInnerHTML
              #js {:__html (str "slatecljs.common.highlight_source(\"force\");")}})

      (createElement "div" #js {}
        "You might also like this "
        (createElement "a" #js {:href "https://jeremyrsellars.github.io/no-new-legacy/posts/2018-09-07-generative-testing-introduction/"}
          "blog about generative testing in Clojure")
        " also by "
        (createElement "a" #js {:href "https://jeremyrsellars.github.io/"}
          "Jeremy Sellars")
        "."))))

(defn- static-render-demo
  [App {:keys [anchor title] :as data}]
  (renderToStaticMarkup
    (shell
      {:title title
       :anchor anchor}
      (common/demo App data))))

(defn- static-rendered-link
  [hash]
  (str
    (if (string/blank? hash)
      "index"
      hash)
    ".html"
    "#" hash)) ; So when the app starts it re-renders the right content.

(defn- save-section [out hash]
  (let [f (io/file out (str (if (string/blank? hash) "index" hash) ".html"))]
    (println (.toString f)
             ":\t"
             (str (common/title hash) " - Slate with ClojureScript"))
    (spit
      f
      ((common/app-component hash)))))

(defn ^:export -main
  [& [out args]]
  (doall (map println args))
  (binding [common/render-demo static-render-demo
            common/rendered-link static-rendered-link]
    (doseq [hash (sort (keys (methods common/app-component)))]
      (save-section out hash))))

(set! *main-cli-fn* -main)
