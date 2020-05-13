(ns slatecljs.browser
  (:require [clojure.string :as string]
            goog.events
            goog.dom)
  (:import [goog.net cookies]
           goog.History
           goog.history.Html5History
           goog.history.Html5History.TokenTransformer))

(defn normalize-pathname
  [pathname]
  (string/replace (or (str pathname) "") #"^[^#]+/#?|^[#/]|\.html$" ""))

(defonce history
  (History. nil nil (goog.dom/getElement "goog_hist")))
(defonce history5
  (doto (goog.history.Html5History. nil
          (let [tt (TokenTransformer.)]
            (set! (.. tt -createUrl)
              (fn createUrl [token path-prefix location]
                (let [section (normalize-pathname token)]
                  (str (if-not (string/blank? section) section "index") ".html"))))
            (set! (.. tt -retrieveToken)
              (fn retrieveToken [path-prefix location]
                (normalize-pathname (.-pathname location))))
            tt))
    (.setUseFragment false)))

(defn add-navigation-handler!
  [f]
  (doto history
    (goog.events/listen goog.History.EventType.NAVIGATE f)
    (.setEnabled true)))

(defn get-scheme [default]
  (as-> (.get cookies "scheme" (when default (name default))) scheme
        (when scheme (keyword scheme))
        (or scheme default)))

(defn set-scheme! [scheme]
  (.set cookies "scheme" (when scheme (name scheme))))

#_
(defn set-timeout!
  [f ms]
  (.setTimeout js/window f ms))

(defn setToken
  [token opt-title]
  (when history5
    (try
      (.setToken history5 token opt-title)
      (catch js/Error err
        (.warn js/console "Cannot setToken " token  opt-title  err)
        (throw err)))))