(ns ^:figwheel-hooks slatecljs.slate-walkthroughs
  (:require [clojure.string :as string]
            [react :refer [createElement]]
            [slatecljs.browser :as browser]
            [slatecljs.common :as common]
            slatecljs.slatecljs-cljsjs-externs
            slatecljs.slatecljs-introduction
            slatecljs.slate-01-installing-slate
            slatecljs.slate-02-event-handlers
            slatecljs.slate-03-defining-custom-elements
            slatecljs.slate-04-applying-custom-formatting
            slatecljs.slate-05-executing-commands
            slatecljs.slate-06-saving
            slatecljs.slate-cljs-01-now-witness-the-power))

(defn ^:export load-section
 ([pathname] (load-section pathname false))
 ([pathname reload?]
  (let [hash (browser/normalize-pathname pathname)
        pathname (when-not (string/blank? hash)
                  (str "/" hash ".html"))
        component-fn (common/app-component hash)]
    (when-not (= pathname (.-pathname js/location))
      (browser/setToken hash (common/title hash))) ; like history.pushState
    (binding [common/load-section load-section]
      (component-fn)))
  (when-not reload?
   (js/setTimeout ; focus the first editor
    #(when-let [editor (js/document.querySelector "#editor-parent *")]
      (.focus editor)
      (window.scrollTo 0 0)); scroll to top
    1))))


(defn- on-navigate [event]
  (load-section (.-pathname (.-location js/window))))

(defn ^:after-load after-figwheel-reload
  []
  (load-section (.-pathname (.-location js/window)) true))

(defonce _init_navigation-handler
  (browser/add-navigation-handler! on-navigate))
