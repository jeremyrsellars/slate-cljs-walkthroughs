(ns slatecljs.common
  (:require [clojure.string :as string]
            [goog.object :as gobj]))

(defmulti app-component
  (fn app-component-dispatch [hash]
    (or hash ""))
  :default "")

(defmulti title
  (fn title-dispatch [hash]
    (or hash "")))

(defmethod title :default
  [hash]
  (str "Oops... What is the title for " hash "?"))

(defn ^:export highlight-source
  [force?]
  (if-let [hljs (gobj/get js/window "hljs")]
    (let [selector (if force
                        "#source pre code"
                        "#source pre code:not(.hljs)")]
      (doseq [block (array-seq (.querySelectorAll js/document selector))]
        (.highlightBlock hljs block)))
    (js/setTimeout highlight-source 1000)))

(defn render-demo
  [App {:keys [title description cljs-source js-source navigation]}]
  (let [app-host-element (js/document.getElementById "app")]
    (gobj/set js/document "title" (str title " - Slate with ClojureScript"))
    (js/ReactDOM.render
      (js/React.createElement "div" #js {}
        (js/React.createElement "h1" #js {} title)

        (js/React.createElement "h3" #js {} "Slate editor")
        (js/React.createElement "p" #js {:class "description"}
          description)
        (js/React.createElement "div" #js {:id "editor-parent"}
          (js/React.createElement App
            #js {}))

        (js/React.createElement "div" #js {:id "source"}
          (js/React.createElement "h3" #js {} "ClojureScript")
          (js/React.createElement "pre" #js {}
            (js/React.createElement "code" #js {:class "language-clojure"
                                                :key (str "cljs:" title)}
              (string/replace-first cljs-source
                #"\"(?:\\\"|[^\"])*\"\s*" "")))
          (js/React.createElement "h3" #js {} "JavaScript (from Slate Tutorial)")
          (js/React.createElement "pre" #js {}
            (js/React.createElement "code" #js {:class "language-javascript"
                                                :key (str "js:" title)}
              (string/replace js-source
                #"^-----*\r?\n(.*)\r?\n(.*)" "  //From $1 $2"))))

        (js/React.createElement "h3" #js {} "Where to next?")
        (js/React.createElement "ul" #js {:id "nav"}
          (into-array
            (for [{:keys [text url class]} navigation]
              (js/React.createElement "li" #js {:class class}
                (js/React.createElement "a"
                  #js {:href url, :class class
                       :target (when (= class "slate-tutorial") "tutorial")}
                  text))))))
      app-host-element)
    (highlight-source :force)))
