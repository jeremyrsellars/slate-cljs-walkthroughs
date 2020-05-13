(ns slatecljs.common
  (:require [clojure.string :as string]
            [react :refer [createElement]]
            [react-dom :refer [render]]
            [goog.object :as gobj]
            [hljs :refer [highlightBlock]]))

(def ^:dynamic rendered-link
  (fn rendered-link
    [hash]
    {:hash hash
     :href
      (str
        (if (string/blank? hash)
          "index"
          hash)
        ".html")}))

(def ^:dynamic load-section #(println "not overriden"))

(defmulti app-component
  (fn app-component-dispatch [hash]
    (string/replace (or hash "") #"^/" ""))
  :default "")

(defmulti title
  (fn title-dispatch [hash]
    (string/replace (or hash "") #"^/" "")))

(defmethod title :default
  [hash]
  (str "Oops... What is the title for " hash "?"))

(defn ^:export highlight-source
  [force?]
  (let [selector (if force
                      "#source pre code"
                      "#source pre code:not(.hljs)")]
    (doseq [block (array-seq (.querySelectorAll js/document selector))]
      (highlightBlock block))))

(defn demo
  [App {:keys [title about objective description source-comments cljs-source js-source navigation]}]
  (let [load-section load-section]
      (createElement "div" #js {}
        (createElement "h1" #js {} title)
        (when about
          (createElement "div" #js {:className "about"} about))
        (when objective
         (createElement "div" #js {:className "objective"}
          objective
          (into-array
            (for [[idx {:keys [text url class rendered-link]}] (map-indexed vector navigation)
                  :when (= class "slate-tutorial")]
              (createElement "span" #js {:key (str idx)}
               #js [(createElement "br" #js {:key "br"})
                    "  Follow along with "
                    (createElement "a"
                      #js {:href url, :className class, :key "a"
                           :target "tutorial"}
                      (str "Slate tutorial " text))
                    " (JavaScript)."])))))

        (when App
          (createElement "h3" #js {} "Interactive Demo"))
        (when description
          (createElement "p" #js {:className "description"}
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
              (createElement "code" #js {:className "language-clojure"
                                         :key (str "cljs:" title)}
                (string/replace-first cljs-source
                  #"\"(?:\\\"|[^\"])*\"\s*" ""))))
          (when js-source
            (createElement "h3" #js {} "JavaScript (from Slate Tutorial)"))
          (when js-source
            (createElement "pre" #js {}
              (createElement "code" #js {:className "language-javascript"
                                         :key (str "js:" title)}
                (string/replace js-source
                  #"^-----*\r?\n(.*)\r?\n(.*)" "  //From $1 $2"))))))

        (when navigation
         (createElement "h3" #js {} "Where to next?"))
        (when navigation
         (createElement "ul" #js {:id "nav", :key "nav"}
          (into-array
            (for [[idx {:keys [text url class rendered-link]}] (map-indexed vector navigation)
                  :let [{:keys [hash href]} rendered-link]]
              (createElement "li" #js {:className class, :key (str idx)}
                (createElement "a"
                  #js {:href (or url href)
                       :onClick (when rendered-link
                                 (fn rendered-link-on-click [e]
                                    (if load-section
                                      (try
                                        (load-section hash)
                                        (.preventDefault e)
                                        false
                                        (catch js/Error error
                                          (.warn js/console (str "Trouble rendering " hash "\r\n" (.toString error) "\r\n" error))))
                                     (.warn js/console (str ::not-defined! "load-section")))))
                       :className class
                       :target (when (= class "slate-tutorial") "tutorial")}
                  text)))))))))

(def ^:dynamic render-demo
 (fn render-demo
  [App {:keys [title] :as data}]
  (let [app-host-element (js/document.getElementById "app")]
    (gobj/set js/document "title" (str title " - Slate with ClojureScript"))
    (render
      (demo App data)
      app-host-element)
    (js/setTimeout #(do(println "highlighting")(highlight-source :force)) 10))))
