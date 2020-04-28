(ns slatecljs.slate-03-defining-custom-elements
  (:require cljs.repl
            [clojure.string :as string]
            [react :as React :refer [useEffect useMemo useState useCallback]]
            [slatecljs.common :as common])
  (:require-macros [slatecljs.github :refer [source-bookmark]]))

(def bookmark (source-bookmark "src"))

(def code-bookmark (source-bookmark "src"))

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
  
(def default-bookmark (source-bookmark "src"))

(defn DefaultElement
  "const DefaultElement = props => {
  return <p {...props.attributes}>{props.children}</p>
}"
  [props]
  (React.createElement "p" (.-attributes props)
    (.-children props)))

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

  return (
    <Slate editor={editor} value={value} onChange={value => setValue(value)}>
      <Editable
        renderElement={renderElement}
        onKeyDown={event => {
          if (event.key === '`' && event.ctrlKey) {
            event.preventDefault()
            // Determine whether any of the currently selected blocks are code blocks.
            const [match] = Editor.nodes(editor, {
              match: n => n.type === 'code',
            })
            // Toggle the block type depending on whether there's already a match.
            Transforms.setNodes(
              editor,
              { type: match ? 'paragraph' : 'code' },
              { match: n => Editor.isBlock(editor, n) }
            )
          }
        }}
      />
    </Slate>
  )
}"
  []
  (let [editor (useMemo #(js/withReact (js/createEditor))
                        #js [])
        ; Add the initial value when setting up our state.
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
           #js [])]
    
    (React.createElement js/Slate
      #js {:editor editor
           :value value
           :onChange #(setValue %)}
      (React.createElement js/Editable
        #js{:renderElement renderElement
            :onKeyDown
            (fn onKeyDown [event]
              (when (and (= (.-key event) "`") (.-ctrlKey event))
                (.preventDefault event)
                ; Determine whether any of the currently selected blocks are code blocks.
                (let [[match] 
                      (es6-iterator-seq
                        (.nodes js/Editor editor
                                  #js {:match
                                       (fn [n]
                                          (= (.-type n) "code"))}))]

                  ; Toggle the block type depending on whether there's already a match.
                  (.setNodes js/Transforms
                    editor
                    #js { :type (if match "paragraph" "code")}
                    #js { :match (fn [n] (js/Editor.isBlock editor n))}))))}))))

(let [anchor "w03"
      title "03 Defining custom elements"]
  (defn ^:export -main
    []
    (slatecljs.common/render-demo
      App
      {:title title
       :description "Press Ctrl+` to toggle code section for the line you're editing."
       :cljs-source (with-out-str (cljs.repl/source App))
       :js-source (with-out-str (cljs.repl/doc App))
       :navigation [(let [anchor "w04"]
                      {:text (common/title anchor)
                       :url (str "#" anchor)
                       :class "next"})
                    {:text title
                     :url "https://docs.slatejs.org/walkthroughs/03-defining-custom-elements"
                     :class "slate-tutorial"}
                    {:text (namespace ::x)
                     :url bookmark
                     :class "source-link"}
                    {:text "<App>"
                     :url app-bookmark
                     :class "source-link"}
                    (let [anchor "w02"]
                      {:text (common/title anchor)
                       :url (str "#" anchor)
                       :class "previous"})]}))

  (defmethod common/app-component anchor [_] -main)
  (defmethod common/title anchor [_] title))
