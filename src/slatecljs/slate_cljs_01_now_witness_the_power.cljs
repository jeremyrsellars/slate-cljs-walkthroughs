(ns slatecljs.slate-cljs-01-now-witness-the-power
  (:require cljs.repl
            [clojure.string :as string]
            [goog.object :as gobj]
            [react :as React :refer [createElement useCallback useEffect useMemo useState]]
            [slate :refer [createEditor Editor Transforms]]
            [slate-react :refer [Editable Slate withReact]]
            [slatecljs.common :as common])
  (:require-macros [slatecljs.github :refer [source-bookmark]]))

(def bookmark (source-bookmark "src"))


(defn CodeElement
  "// Define a React component renderer for our code blocks.
const CodeElement = props => {
  return (
    <pre {...props.attributes}>
      <code>{props.children}</code>
    </pre>
  )
}"
  [props]
  (createElement "pre" (.-attributes props)
    (createElement "code" #js{}
      (.-children props))))
  
(defn DefaultElement
  "const DefaultElement = props => {
  return <p {...props.attributes}>{props.children}</p>
}"
  [props]
  (createElement "p" (.-attributes props)
    (.-children props)))

(def leaf-bookmark (source-bookmark "src"))

(defrecord LeafText
  [^String text
   ^boolean bold?])

(defn Leaf
  [props]
  ;(println :Leaf :props (.-attributes props))
  ;(println :Leaf :leaf (.-leaf props))
  (let [;{:keys [bold?]} (gobj/get props "leaf")
        bold? false]
    (createElement "span"
      (doto (gobj/clone (.-attributes props))
        (gobj/set "style" #js {:fontWeight (if bold? "bold" "normal")}))
      (.-children props))))

(def CustomEditor-bookmark (source-bookmark "src"))
; For ClojureScript, the CustomEditor object is a bit redundant since
; ClojureScript offers namespaces.  Instead, we'll use functions 
; at the namespace level.

(defn is-bold-mark-active?
  [editor]
  (let [[match]
        (es6-iterator-seq
          (.nodes Editor editor
                    #js {:match
                         (fn [n]
                            (boolean (:bold? n)))
                         :universal true}))]
    (boolean match)))

(defn is-code-block-active?
  "  isCodeBlockActive(editor) {
    const [match] = Editor.nodes(editor, {
      match: n => n.type === 'code',
    })

    return !!match
  },"
  [editor]
  (let [[match]
        (es6-iterator-seq
          (.nodes Editor editor
                    #js {:match
                         (fn [n]
                            (= (.-type n) "code"))}))]
    (boolean match)))

(defn toggle-bold-mark
  [editor]
  (let [isActive (is-bold-mark-active? editor)]
    (.setNodes Transforms
      editor
      #js { :bold? (if isActive nil true)}
      #js { :match #(Text.isText %)
            :split true})))

(defn toggle-code-block
  "  toggleCodeBlock(editor) {
    const isActive = CustomEditor.isCodeBlockActive(editor)
    Transforms.setNodes(
      editor,
      { type: isActive ? null : 'code' },
      { match: n => Editor.isBlock(editor, n) }
    )
  },"
  [editor]
  (let [isActive (is-code-block-active? editor)]
    (.setNodes Transforms
      editor
      #js { :type (if isActive nil "code")}
      #js { :match (fn [n] (Editor.isBlock editor n))})))

(defn source-comments
  []
  (createElement "div" #js {}
    (common/demo
      nil
      {:source-comments
        (createElement "div" #js {}
          (common/demo
            nil
            {:source-comments
              (createElement "h2" #js {}
                "CustomEditor.isBoldMarkActive")
             :cljs-source (with-out-str (cljs.repl/source is-bold-mark-active?))
             :js-source (with-out-str (cljs.repl/doc is-bold-mark-active?))})
          (common/demo
            nil
            {:source-comments
              (createElement "h2" #js {}
                "CustomEditor.isCodeBlockActive")
             :cljs-source (with-out-str (cljs.repl/source is-code-block-active?))
             :js-source (with-out-str (cljs.repl/doc is-code-block-active?))})
          (common/demo
            nil
            {:source-comments
              (createElement "h2" #js {}
                "CustomEditor.toggleBoldMark")
             :cljs-source (with-out-str (cljs.repl/source toggle-bold-mark))
             :js-source (with-out-str (cljs.repl/doc toggle-bold-mark))})
          (common/demo
            nil
            {:source-comments
              (createElement "h2" #js {}
                "CustomEditor.toggleCodeBlock")
             :cljs-source (with-out-str (cljs.repl/source toggle-code-block))
             :js-source (with-out-str (cljs.repl/doc toggle-code-block))})
          (createElement "h2" #js {}
            "App"))})))

(def app-bookmark (source-bookmark "src"))


(defn App
  []
  (let [editor (useMemo #(withReact (createEditor))
                        #js [])
        [value setValue]
        (useState
         #js [#js {:type "paragraph"
                   :children
                   #js [#js {:text "A line of text in a paragraph."}
                        (->LeafText "A line of text in a cljs paragraph." nil)]}])
                        
;                        (map->LeafText {:text "A line of text in a cljs paragraph."})]}])
;                                        :bold? false})]}])
        renderElement
         (useCallback
           (fn renderElement [props]
             (case (.-type (.-element props))
               "code" (createElement CodeElement props)
                      (createElement DefaultElement props)))
           #js [])
        renderLeaf
         (useCallback
           (fn renderLeaf [props]
             (createElement Leaf props))
           #js [])]
    ; Add a toolbar with buttons that call the same methods.
    (createElement Slate
      #js {:editor editor
           :value value
           :onChange #(setValue %)}
      (createElement "div" #js {}
        (createElement "button"
          #js {:onMouseDown
                (fn [event]
                  (.preventDefault event)
                  (toggle-bold-mark editor))}
          "Bold")
        (createElement "button"
          #js {:onMouseDown
                (fn [event]
                  (.preventDefault event)
                  (toggle-code-block editor))}
          "Code Block"))
      (createElement Editable
        #js{:renderElement renderElement
            :renderLeaf renderLeaf
            :onKeyDown
            (fn onKeyDown [event]
             (when (.-ctrlKey event)
              (case (.-key event)  
               "`"
               (do
                (.preventDefault event)
                (toggle-code-block editor))

               "b"
               (do
                (.preventDefault event)
                (toggle-bold-mark editor))
               
               ;default
               nil)))}))))

(let [anchor "c01"
      title "CLJS 01 Now witness the expressive power of Slate with ClojureScript"]
  (defn ^:export -main
    []
    (slatecljs.common/render-demo
      App
      {:title title
       :anchor anchor
       :objective "Make reusable formatting commands to help keep code clear and maintainable."
       :description "Select some text and try the buttons, Ctrl+b, ctrl-` as before."
       :source-comments (source-comments)
       :cljs-source (with-out-str (cljs.repl/source App))
       :navigation [#_
                    (let [anchor "c02"]
                      {:text (common/title anchor)
                       :url (common/rendered-link anchor)
                       :class "next"})
                    {:text (namespace ::x)
                     :url bookmark
                     :class "source-link"}
                    {:text "CustomEditor"
                     :url CustomEditor-bookmark
                     :class "source-link"}
                    {:text "<App>"
                     :url app-bookmark
                     :class "source-link"}
                    (let [anchor "w06"]
                      {:text (common/title anchor)
                       :url (common/rendered-link anchor)
                       :class "previous"})]}))

  (defmethod common/app-component anchor [_] -main)
  (defmethod common/title anchor [_] title))
