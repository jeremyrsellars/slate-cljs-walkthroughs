(ns slatecljs.externs.scraper
  (:require [clojure.string :as string]
            [clojure.pprint :as pprint]
            [goog.object :as gobj]
            slate
            slate-react
            [slatecljs.externs.core :as ext]
            cljs.nodejs
            [cljs-node-io.core :as io :refer [spit]]))

(cljs.nodejs/enable-util-print!)

(defn spit-externs
  [filename names lib]
  (spit filename
    (ext/externs-str names (ext/describe-externs lib))))

(defn print-externs
  [filename names lib]
  (println "---------------------------------")
  (println (.toString filename))
  (println "---------------------------------")
  (println (ext/externs-str names (ext/describe-externs lib)))
  (println))

(defn ^:export -main
  [& [out-dir]]
  (let [out-fn (if out-dir spit-externs print-externs)
        out-dir (or out-dir ".")]
    (out-fn (io/file out-dir "slate.ext.js")       {:js-var-name "Slate"       :ns-name "slate"}       slate)
    (out-fn (io/file out-dir "slate-react.ext.js") {:js-var-name "SlateReact"  :ns-name "slate-react"} slate-react)))

(set! *main-cli-fn* -main)
