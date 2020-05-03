(ns slatecljs.common
  (:require [clojure.string :as string]
            [react :refer [createElement]]
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
  (if-let [hljs nil];(comment (gobj/get js/window "hljs"))]
    (let [selector (if force
                        "#source pre code"
                        "#source pre code:not(.hljs)")]
      (doseq [block (array-seq (.querySelectorAll js/document selector))]
        (.highlightBlock hljs block)))
    (js/setTimeout highlight-source 1000)))

(defn demo
  [App {:keys [title about objective description source-comments cljs-source js-source navigation]}]
  (do
      (createElement "div" #js {}
        (createElement "h1" #js {} title)
        (when about
          (createElement "p" #js {:class "about"} about))
        (when objective
         (createElement "p" #js {:class "objective"}
          objective
          (into-array
            (for [{:keys [text url class]} navigation
                  :when (= class "slate-tutorial")]
              (createElement "span" #js {}
               #js [(createElement "br" #js {})
                    "  Follow along with "
                    (createElement "a"
                      #js {:href url, :class class
                           :target "tutorial"}
                      (str "Slate tutorial " text))
                    " (JavaScript)."])))))

        (when App
          (createElement "h3" #js {} "Interactive Demo"))
        (when description
          (createElement "p" #js {:class "description"}
            description))
        (when App
          (createElement "div" #js {:id "editor-parent"}
            (createElement App
              #js {})))

        (when (or cljs-source js-source source-comments)
         (createElement "div" #js {:id "source"}
          source-comments
          (when cljs-source
            (createElement "h3" #js {} "ClojureScript"))
          (when cljs-source
            (createElement "pre" #js {}
              (createElement "code" #js {:class "language-clojure"
                                                  :key (str "cljs:" title)}
                (string/replace-first cljs-source
                  #"\"(?:\\\"|[^\"])*\"\s*" ""))))
          (when js-source
            (createElement "h3" #js {} "JavaScript (from Slate Tutorial)"))
          (when js-source
            (createElement "pre" #js {}
              (createElement "code" #js {:class "language-javascript"
                                                  :key (str "js:" title)}
                (string/replace js-source
                  #"^-----*\r?\n(.*)\r?\n(.*)" "  //From $1 $2"))))))

        (when navigation
         (createElement "h3" #js {} "Where to next?"))
        (when navigation
         (createElement "ul" #js {:id "nav"}
          (into-array
            (for [{:keys [text url class]} navigation]
              (createElement "li" #js {:class class}
                (createElement "a"
                  #js {:href url, :class class
                       :target (when (= class "slate-tutorial") "tutorial")}
                  text)))))))))

(def ^:dynamic render-demo
 (fn render-demo
  [App {:keys [title] :as data}]
  (let [app-host-element (js/document.getElementById "app")]
    (gobj/set js/document "title" (str title " - Slate with ClojureScript"))
    (js/ReactDOM.render
      (demo App data)
      app-host-element)
    (highlight-source :force))))
