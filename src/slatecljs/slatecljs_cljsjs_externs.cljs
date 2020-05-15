(ns slatecljs.slatecljs-cljsjs-externs
  (:require [clojure.string :as string]
            [react :refer [createElement]]
            slate
            slate-react
            [slatecljs.common :as common]
            [slatecljs.externs.core :as ext]))

(defn- print-externs
  [filename {:keys [js-var-name ns-name]} lib]
  (println "/*---------------------------------")
  (println (.toString filename))
  (println "---------------------------------*/")
  (println (ext/externs-str {:js-var-name js-var-name, :ns-name ns-name} (ext/describe-externs lib)))
  (println))

(defn- externs-string
  []
  (with-out-str
    (print-externs "slate.ext.js"       {:js-var-name "Slate"      :ns-name "slate"}       slate)
    (print-externs "slate-react.ext.js" {:js-var-name "SlateReact" :ns-name "slate-react"} slate-react)))

(defn App
  []
  (createElement "div" #js {}
    (createElement "p" #js {}
      "To generate extern files, see the project "
      (createElement "a" #js {:href "https://github.com/jeremyrsellars/slate-cljs-walkthroughs#closure-externs"}
        "README.md"))))

(let [anchor "closure_externs"
      title "Closure Advanced Compliation Externs"]

  (defn ^:export -main
    []
    (slatecljs.common/render-demo
      nil
      {:title title
       :anchor anchor
       :about (App)
       :objective "Use slatecljs.externs.core/externs-str to generate externs."
       :cljs-source (with-out-str (cljs.repl/source externs-string))
       :js-source-title "Externs (Generated from the javascript)"
       :js-source (externs-string)
       :navigation
        (reduce into []
         [(for [anchor [""]]
            {:text (common/title anchor)
             :rendered-link (common/rendered-link anchor)})
          [{:text "CLJSJS slate package"
            :url "https://github.com/cljsjs/packages/tree/master/slate"}
           {:text "CLJSJS slate-react package"
            :url "https://github.com/cljsjs/packages/tree/master/slate-react"}
           {:text "slatecljs.externs.core"
            :url slatecljs.externs.core/bookmark}]])}))
            

  (defmethod common/app-component anchor [_] -main)
  (defmethod common/title anchor [_] title))
