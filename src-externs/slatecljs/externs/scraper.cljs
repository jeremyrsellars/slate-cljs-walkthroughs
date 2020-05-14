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
  [filename js-var-name lib]
  (spit filename
    (ext/externs-str js-var-name (ext/describe-externs lib))))

(defn print-externs
  [filename js-var-name lib]
  (println "---------------------------------")
  (println (.toString filename))
  (println "---------------------------------")
  (println (ext/externs-str js-var-name (ext/describe-externs lib)))
  (println))

(defn ^:export -main
  [& [out-dir]]
  (let [out-fn (if out-dir spit-externs print-externs)
        out-dir (or out-dir ".")]
    (out-fn (io/file out-dir "slate.ext.js") "Slate" slate)
    (out-fn (io/file out-dir "slate-react.ext.js") "SlateReact" slate-react)))

(set! *main-cli-fn* -main)
