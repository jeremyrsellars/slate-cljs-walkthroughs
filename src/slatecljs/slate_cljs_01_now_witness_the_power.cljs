(ns slatecljs.slate-cljs-01-now-witness-the-power
  (:require cljs.repl
            [clojure.string :as string]
            [goog.object :as gobj]
            [react :refer [createElement useCallback useEffect useMemo useState]]
            [slate :refer [createEditor Editor Transforms Text]]
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

(defn demo-destructuring-options
  "Which methods of comprehending a Slate node actually work?
   See some ideas that work or don't work below"
  []
  (let [leaf #js {:text "", :bold? false}

        ; Me: "ClojureScript has this cool destructuring syntax...."
        ; Nope! Sorry, we *cannot* destructure #js objects like this
        ; because {:strs [...]} destructuring is for Clojure maps.
        #_#_
        {:strs [text bold?], :or {bold? false}} leaf   ; <- won't work

        ; Me: "That's ok.  It also has the short and sweet .-property syntax"
        ; Probably not ok! Sorry, this will probably get you into trouble
        ; with advanced Closure optimizations (if you choose that path).
        ; Unfortunately, it would probably work ok in dev, 
        ; and worse, might even *partially* work in :advanced for properties
        ; defined in the slate interfaces like .-text, but will almost certainly fail
        ; for new properties like .-bold?
        #_#_
        bold? (boolean (.-bold? leaf))   ; <- won't work in :advanced

        ; Me: "Fine, I'll create my own type that implements the slate Text interface
        ;      and use that everywhere so I can say (.-bold? x) and use destructuring."
        ; Nope!  Slate doesn't let you use a type like this
        ; even though it implements the right interface (e.g. Text, Element, Node)
        ; because slate checks isPlainObject which makes sure the object
        ; was created with js/Object's constructor... 
        ; but ClojureScript records have their own constructor so that won't work.
        #_ (defrecord LeafText [^String text, ^boolean bold?])

        ; Do this instead:
        bold? (gobj/get leaf "bold?" false) ; supply the default value of not-bold
        text  (gobj/get leaf "text")
        
        ; Or, put the idiomatic ClojureScript attributes slightly deeper:
        emphasis-leaf  #js {:text "", :emphasis {:bold? true}}   ; <- emphasis is a Clojure map
        {:keys [bold?] :or {bold? false}} (gobj/get emphasis-leaf "emphasis")
        ; but, if you intend to use Transforms.setNodes, you won't want to mix
        ; attributes that you'll want to set individually because Transforms.setNodes
        ; is more like assoc than update.
        ; Also, remember to take extra care around serializing the extra attributes.
        
        text  (gobj/get emphasis-leaf "text")]
        
    {:text text, :bold? bold?}))

(defn Leaf
  [props]
  (let [{:keys [em-mode color]} (gobj/getValueByKeys props "leaf" "emphasis")
        bold? (= em-mode :bold)
        italic? (= em-mode :italic)
        color? (= em-mode :color)]
    (createElement "span"
      (doto (gobj/clone (.-attributes props))
        (gobj/set "style" #js {:fontWeight (if bold? "bold" "normal")
                               :fontStyle (if italic? "italic" "normal")
                               :color (if color? (or color "#33f") "unset")}))
      (.-children props))))

(def CustomEditor-bookmark (source-bookmark "src"))
; For ClojureScript, the CustomEditor object is a bit redundant since
; ClojureScript offers namespaces.  Instead, we'll use functions 
; at the namespace level.

(defn extract-emphasis-mode
  "Find the emphasis value for the first leaf."
  [editor]
  (let [[[text-node]]     ; <-- seq destructuring
        (es6-iterator-seq
          (.nodes Editor editor
                    #js {:match (fn [n] (and (.isText Text n)
                                             (gobj/get n "emphasis")
                                             true))
                         :universal true}))]
    (gobj/get text-node "emphasis")))

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

(def next-emphasis-mode ; rotate through the list
  (let [modes-in-order [{:em-mode :bold}
                        {:em-mode :italic}
                        {:em-mode :color, :color "red"}
                        {:em-mode :color, :color "blue"}
                        nil]]
    (zipmap modes-in-order
            (rest (cycle modes-in-order)))))

(defn rotate-bold-mark
  [editor]
  (let [em-mode (extract-emphasis-mode editor)]
    (.setNodes Transforms
      editor
      #js { :emphasis (next-emphasis-mode em-mode)}
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
              (createElement "div" #js {}
                (createElement "h2" #js {}
                  "How does CLJS Destructuring work with slate node objects?")
                "Slate nodes are #js {} objects. "
                "I was a bit surprised to find that there isn't really an efficient, succinct way to parse the Slate Node properties into clojure bindings. "
                "It can be succinct with "
                (createElement "code" #js {} "js->clj")
                " and then destructuring the resultant Clojure map, but it's not terribly fast. "
                "For my purposes, it's better to be clear than clever. "
                "I haven't found a compelling reason to trade idiomatic slate for idiomatic ClojureScript. "
                (createElement "p" #js {})
                "Another thing to remember is Clojure types don't serialize very well to the slate default of JSON.")
             :cljs-source-comment? true
             :cljs-source (with-out-str (cljs.repl/source demo-destructuring-options))})
          (common/demo
            nil
            {:source-comments
              (createElement "div" #js {}
                (createElement "h2" #js {}
                  "How does CLJS Destructuring work with slate .nodes search results (ES6 iterators)?")
                "Slate uses ES6 iterators, and ClojureScript can turn them into destructurable sequences with "
                (createElement "code" #js {} "es6-iterator-seq")
                ".")
             :cljs-source-comment? true
             :cljs-source (with-out-str (cljs.repl/source extract-emphasis-mode))})
          (common/demo
            nil
            {
             :cljs-source (with-out-str (cljs.repl/source rotate-bold-mark))})
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
                   #js [#js {:text "A line of text with some "}
                        #js {:text "emphasized text" :emphasis (next-emphasis-mode (next-emphasis-mode (next-emphasis-mode nil)))}
                        #js {:text ".  You can select the text and use Ctrl+b to cycle through the emphasis modes."}]}])
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
                  (rotate-bold-mark editor))}
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
                (rotate-bold-mark editor))
               
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
       :objective "Make Ctrl+b rotate through some different emphasis formats.  See what features of ClojureScript work well with and slate development."
       :description "Select some text and try the buttons, Ctrl+b (to cycle through the emphasis modes), ctrl-` as before."
       :source-comments (source-comments)
       :cljs-source (with-out-str (cljs.repl/source App))
       :navigation [#_
                    (let [anchor "c02"]
                      {:text (common/title anchor)
                       :rendered-link (common/rendered-link anchor)
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
                       :rendered-link (common/rendered-link anchor)
                       :class "previous"})]}))

  (defmethod common/app-component anchor [_] -main)
  (defmethod common/title anchor [_] title))
