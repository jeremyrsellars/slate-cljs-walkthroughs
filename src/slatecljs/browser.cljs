(ns slatecljs.browser
  (:require goog.events
            goog.dom)
  (:import [goog.net cookies]
           goog.History))

(defonce history
  (History. nil nil (goog.dom/getElement "goog_hist")))

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
