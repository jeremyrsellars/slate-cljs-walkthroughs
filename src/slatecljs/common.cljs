(ns slatecljs.common
  (:require [clojure.string :as string]))

(defn render-demo
  [App cljs-source js-source]
  (let [app-host-element (js/document.getElementById "app")]
    (js/ReactDOM.render
      (js/React.createElement "div" #js {:componentDidUpdate #(js/highlightNow :force)}
        (js/React.createElement "h3" #js {} "Let's edit some text")
        (js/React.createElement "div" #js {:id "editor-parent"}
          (js/React.createElement App
            #js {}))

        (js/React.createElement "div" #js {:id "source"}
          (js/React.createElement "h3" #js {} "ClojureScript")
          (js/React.createElement "pre" #js {}
            (js/React.createElement "code" #js {:class "language-clojure"}
              cljs-source))

          (js/React.createElement "h3" #js {} "JavaScript (from Slate Tutorial)")
          (js/React.createElement "pre" #js {}
            (js/React.createElement "code" #js {:class "language-javascript"}
              (string/replace js-source #"^-----*\r?\n(.*)\r?\n(.*)" "  //From $1 $2")))))
      app-host-element)))
