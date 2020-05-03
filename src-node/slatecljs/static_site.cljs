(ns slatecljs.static-site
  (:require [clojure.string :as string]
            [react :refer [createElement]]
            ["react-dom/server" :as ReactDOMServer :refer [renderToString]]
            [slatecljs.common :as common]
            slatecljs.slate-01-installing-slate
            slatecljs.slate-02-event-handlers
            slatecljs.slate-03-defining-custom-elements
            slatecljs.slate-04-applying-custom-formatting
            slatecljs.slate-05-executing-commands
            ;slatecljs.slate-06-saving ; deferring while localStorage is not defined in nodejs
            cljs.nodejs))

(cljs.nodejs/enable-util-print!)

(defn App
  []
  (createElement "div" #js {}
    (createElement "a" #js {:href "https://docs.slatejs.org/"}
      "Slate")
    " is a DOM editor, but you get to define the Document Object Model " 
    (createElement "em" #js {} "AND")
    " the way that renders to HTML."
    (createElement "p" #js {}
      "These walkthroughs show how ClojureScript can be used to implement the JavaScript code from the official walkthroughs in the Slate documentation."
      "ClojureScript offers a bunch of ways to make this easier, but I usually chose a very direct translation so they would be easier to follow along with the JS source."
      "Let's save the power of ClojureScript for another time.")))

(let [anchor ""
      title "Introduction"]
  (defn ^:export introduction
    []
    (slatecljs.common/render-demo
      nil
      {:title title
       :about (App)
       :objective "Feel free to read through the code or clone this repo and edit it to improve your understanding."
       :navigation
        (reduce into []
         [(for [anchor ["w01" "w02" "w03" "w04" "w05" "w06"]]
            {:text (common/title anchor)
             :url (str "#" anchor)})
          [{:text "01 Installing Slate"
            :url "https://docs.slatejs.org/walkthroughs/01-installing-slate"
            :class "slate-tutorial"}]])}))

  (defmethod common/app-component anchor [_] introduction)
  (defmethod common/title anchor [_] title))

(defn render-demo
  [App {:keys [title] :as data}]
  (println (str title " - Slate with ClojureScript"))
  (renderToString
    (common/demo App data)))

(defn save-section [hash]
  (println ((common/app-component hash))))

(defn ^:export -main
  [& args]
  (doall (map println args))
  (binding [common/render-demo render-demo]
    (doseq [hash (sort (keys (methods common/app-component)))]
      (save-section hash))))

(-main)