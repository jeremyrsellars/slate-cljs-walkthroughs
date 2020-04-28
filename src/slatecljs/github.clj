(ns slatecljs.github
  (:require [clojure.string :as string])) 

(defmacro source-bookmark
  [root]
  (let [namespace {:namespace (name cljs.analyzer/*cljs-ns*)}
        {:keys [file end-line]} (meta &form)
        root-relative
          (string/replace-first file
            (re-pattern (str ".*?/(?=" root "/)"))
            "https://github.com/jeremyrsellars/slate-cljs-walkthroughs/blob/master/")]
    (str root-relative
         "#L" (inc end-line))))
       
;https://github.com/jeremyrsellars/slate-cljs-walkthroughs/blob/master/src/slatecljs/slate_01_installing_slate.cljs