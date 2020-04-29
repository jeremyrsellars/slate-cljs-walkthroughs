(ns slatecljs.slate-05-executing-commands
  (:require cljs.repl
            [clojure.string :as string]
            [goog.object :as gobj]
            [react :as React :refer [useEffect useMemo useState useCallback]]
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
  (React.createElement "pre" (.-attributes props)
    (React.createElement "code" #js{}
      (.-children props))))
  
(defn DefaultElement
  "const DefaultElement = props => {
  return <p {...props.attributes}>{props.children}</p>
}"
  [props]
  (React.createElement "p" (.-attributes props)
    (.-children props)))

(def leaf-bookmark (source-bookmark "src"))

(defn Leaf
  "const Leaf = props => {
  return (
    <span
      {...props.attributes}
      style={{ fontWeight: props.leaf.bold ? 'bold' : 'normal' }}
    >
      {props.children}
    </span>
  )
}"
  [props]
  (let [leaf (gobj/get props "leaf")
        bold (gobj/get leaf "bold")]
    (React.createElement "span"
      (doto (gobj/clone (.-attributes props))
        (gobj/set "style" #js {:font-weight (if bold "bold" "normal")}))
      (.-children props))))

(def CustomEditor-bookmark (source-bookmark "src"))
; For ClojureScript, the CustomEditor object is a bit redundant since
; ClojureScript offers namespaces.  Instead, we'll use functions 
; at the namespace level.

(defn is-bold-mark-active?
  "  isBoldMarkActive(editor) {
    const [match] = Editor.nodes(editor, {
      match: n => n.bold === true,
      universal: true,
    })

    return !!match
  },"
  [editor]
  (let [[match]
        (es6-iterator-seq
          (.nodes js/Editor editor
                    #js {:match
                         (fn [n]
                            (true? (.-bold n)))
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
          (.nodes js/Editor editor
                    #js {:match
                         (fn [n]
                            (= (.-type n) "code"))}))]
    (boolean match)))

(defn toggle-bold-mark
  "  toggleBoldMark(editor) {
    const isActive = CustomEditor.isBoldMarkActive(editor)
    Transforms.setNodes(
      editor,
      { bold: isActive ? null : true },
      { match: n => Text.isText(n), split: true }
    )
  },"
  [editor]
  (let [isActive (is-bold-mark-active? editor)]
    (.setNodes js/Transforms
      editor
      #js { :bold (if isActive nil true)}
      #js { :match (fn [n] (js/Text.isText n))
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
    (.setNodes js/Transforms
      editor
      #js { :type (if isActive nil "code")}
      #js { :match (fn [n] (js/Editor.isBlock editor n))})))

(def app-bookmark (source-bookmark "src"))

(defn App
  "const App = () => {
  const editor = useMemo(() => withReact(createEditor()), [])
  const [value, setValue] = useState([
    {
      type: 'paragraph',
      children: [{ text: 'A line of text in a paragraph.' }],
    },
  ])

  const renderElement = useCallback(props => {
    switch (props.element.type) {
      case 'code':
        return <CodeElement {...props} />
      default:
        return <DefaultElement {...props} />
    }
  }, [])

  const renderLeaf = useCallback(props => {
    return <Leaf {...props} />
  }, [])

  return (
    // Add a toolbar with buttons that call the same methods.
    <Slate editor={editor} value={value} onChange={value => setValue(value)}>
      <div>
        <button
          onMouseDown={event => {
            event.preventDefault()
            CustomEditor.toggleBoldMark(editor)
          }}
        >
          Bold
        </button>
        <button
          onMouseDown={event => {
            event.preventDefault()
            CustomEditor.toggleCodeBlock(editor)
          }}
        >
          Code Block
        </button>
      </div>
      <Editable
        editor={editor}
        renderElement={renderElement}
        renderLeaf={renderLeaf}
        onKeyDown={event => {
          if (!event.ctrlKey) {
            return
          }

          switch (event.key) {
            case '`': {
              event.preventDefault()
              CustomEditor.toggleCodeBlock(editor)
              break
            }

            case 'b': {
              event.preventDefault()
              CustomEditor.toggleBoldMark(editor)
              break
            }
          }
        }}
      />
    </Slate>
  )
}"
  []
  (let [editor (useMemo #(js/withReact (js/createEditor))
                        #js [])
        [value setValue]
        (useState
         #js [#js {:type "paragraph"
                   :children
                   #js [#js {:text "A line of text in a paragraph."}]}])
        renderElement
         (useCallback
           (fn renderElement [props]
             (case (.-type (.-element props))
               "code" (React.createElement CodeElement props)
                      (React.createElement DefaultElement props)))
           #js [])
        renderLeaf
         (useCallback
           (fn renderLeaf [props]
             (React.createElement Leaf props))
           #js [])]
    ; Add a toolbar with buttons that call the same methods.
    (React.createElement js/Slate
      #js {:editor editor
           :value value
           :onChange #(setValue %)}
      (React.createElement "div" #js {}
        (React.createElement "button"
          #js {:onMouseDown
                (fn [event]
                  (.preventDefault event)
                  (toggle-bold-mark editor))}
          "Bold")
        (React.createElement "button"
          #js {:onMouseDown
                (fn [event]
                  (.preventDefault event)
                  (toggle-code-block editor))}
          "Code Block"))
      (React.createElement js/Editable
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

(let [anchor "w05"
      title "05 Custom commands"]
  (defn ^:export -main
    []
    (slatecljs.common/render-demo
      App
      {:title title
       :objective "Make reusable formatting commands to help keep code clear and maintainable."
       :description "Select some text and try the buttons, Ctrl+b, ctrl-` as before."
       :cljs-source (with-out-str (cljs.repl/source App))
       :js-source (with-out-str (cljs.repl/doc App))
       :navigation [(let [anchor "w06"]
                      {:text (common/title anchor)
                       :url (str "#" anchor)
                       :class "next"})
                    {:text title
                     :url "https://docs.slatejs.org/walkthroughs/05-executing-commands"
                     :class "slate-tutorial"}
                    {:text (namespace ::x)
                     :url bookmark
                     :class "source-link"}
                    {:text "CustomEditor"
                     :url CustomEditor-bookmark
                     :class "source-link"}
                    {:text "<App>"
                     :url app-bookmark
                     :class "source-link"}
                    (let [anchor "w04"]
                      {:text (common/title anchor)
                       :url (str "#" anchor)
                       :class "previous"})]}))

  (defmethod common/app-component anchor [_] -main)
  (defmethod common/title anchor [_] title))
