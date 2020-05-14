(ns slatecljs.externs.core
  (:require [clojure.string :as string]
            [clojure.pprint :as pprint]
            [goog.object :as gobj]
            slate))

(def fn-extern (fn [] "function() {}"))

(defn describe-externs
  [x]
  (cond (fn?     x)    fn-extern
        (string? x)    ""
        (array?  x)    #js []
        (number? x)    #js []

        (object? x)
        (into (sorted-map)
          (zipmap
            (gobj/getKeys x)
            (map describe-externs (gobj/getValues x))))

        :default       x))

(def ^:dynamic *indent* 0)
(def indent-str "   ") ; Use a string of 3 spaces for an effective indent of 4 spaces

(defn println-indent
  [& xs]
  (apply println (concat (repeat *indent* indent-str) xs)))

(defn println-extern-kv
  [entries]
  (cond
    (map? entries)
    (doseq [[k v] entries]
      (cond (map? v)
            (do
              (println-indent (str (pr-str (str k)) \:) \{)
              (binding [*indent* (inc *indent*)]
                (println-extern-kv v))
              (println-indent "},"))

            (fn? v)
            (println-indent (str (pr-str (str k)) \:) (str (v) \,))

            :default
            (println-indent (str (pr-str (str k)) \:) (str (js/JSON.stringify v) \,))))
    
    :default
    (js/console.warn entries)))

(defn externs-str
  [js-var-name externs]
  (with-out-str
    (println-indent "var" js-var-name \= \{)
    (binding [*indent* (inc *indent*)]
      (println-extern-kv externs))
    #_
    (doseq [[k v] externs]
      (println-indent (str (pr-str (str k)) \:) \{)
      (binding [*indent* (inc *indent*)])
        ;(doseq))
      (println-indent "},"))
    (println-indent "};")))
