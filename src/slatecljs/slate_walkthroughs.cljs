(ns slatecljs.slate-walkthroughs 
  (:require [clojure.string :as string]
            [slatecljs.browser :as browser]
            [slatecljs.common :as common]
            [slatecljs.slate-01-installing-slate                      :as w01]
            [slatecljs.slate-02-event-handlers                        :as w02]
            [slatecljs.slate-03-defining-custom-elements              :as w03]))

(def -main w03/-main)
(defmethod common/app-component "" [_] -main)


(defn load-section [hash]
  (let [k (string/replace hash #"^#" "")
        component-fn (common/app-component k)]
    (println k hash :component-fn component-fn)
    (component-fn)))

(defn- on-navigate [event]
  (load-section (str "#" (.-token event))))

(defonce _init_navigation-handler
  (browser/add-navigation-handler! on-navigate))

(load-section (.-hash (.-location js/window)))

