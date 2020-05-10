(ns slatecljs.slate-walkthroughs 
  (:require [clojure.string :as string]
            [react :refer [createElement]]
            [slatecljs.browser :as browser]
            [slatecljs.common :as common]
            slatecljs.slatecljs-introduction
            slatecljs.slate-01-installing-slate
            slatecljs.slate-02-event-handlers
            slatecljs.slate-03-defining-custom-elements
            slatecljs.slate-04-applying-custom-formatting
            slatecljs.slate-05-executing-commands
            slatecljs.slate-06-saving))

(defn ^:export load-section [hash]
  (let [k (string/replace hash #"^#" "")
        component-fn (common/app-component k)]
    (component-fn))
  (window.scrollTo 0 0) ; scroll to top
  (js/setTimeout ; focus the first editor
    #(when-let [editor (js/document.querySelector "#editor-parent *")]
      (.focus editor))
    1))

(defn- on-navigate [event]
  (load-section (str "#" (.-token event))))

(defonce _init_navigation-handler
  (browser/add-navigation-handler! on-navigate))

(load-section (.-hash (.-location js/window)))

