(ns slatecljs.slate-04-applying-custom-formatting
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
  [{:strs [bold] :as props}]
  (React.createElement "span"
    (doto (gobj/clone (.-attributes props))
      (gobj/set "style" #js {:font-weight (if bold "bold" "normal")}))
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

  // Define a leaf rendering function that is memoized with `useCallback`.
  const renderLeaf = useCallback(props => {
    return <Leaf {...props} />
  }, [])

  return (
    <Slate editor={editor} value={value} onChange={value => setValue(value)}>
      <Editable
        renderElement={renderElement}
        // Pass in the `renderLeaf` function.
        renderLeaf={renderLeaf}
        onKeyDown={event => {
          if (!event.ctrlKey) {
            return
          }

          switch (event.key) {
            case '`': {
              event.preventDefault()
              const [match] = Editor.nodes(editor, {
                match: n => n.type === 'code',
              })
              Transforms.setNodes(
                editor,
                { type: match ? null : 'code' },
                { match: n => Editor.isBlock(editor, n) }
              )
              break
            }

            case 'b': {
              event.preventDefault()
              Transforms.setNodes(
                editor,
                { bold: true },
                { match: n => Text.isText(n), split: true }
              )
              break
            }
          }
        }}
      />
    </Slate>
  )
}
"
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
           #js [])
        renderLeaf
         (useCallback
           (fn renderLeaf [props]
             (React.createElement Leaf props))
           #js [])]
    
    (React.createElement js/Slate
      #js {:editor editor
           :value value
           :onChange #(setValue %)}
      (React.createElement js/Editable
        #js{:renderElement renderElement
            ; Pass in the `renderLeaf` function.
            :renderLeaf renderLeaf
            :onKeyDown
            (fn onKeyDown [event & xs]
             (when (.-ctrlKey event)
              (case (.-key event)  
               "`"
               (do
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
                    #js { :match (fn [n] (js/Editor.isBlock editor n))})))

               "b"
               (do
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
                    #js { :bold true}
                    #js { :match (fn [n] (js/slate.Text.isText n))
                          :split true})))
               
               ;default
               nil)))}))))

(let [anchor "w04"
      title "04 Applying custom formatting"]
  (defn ^:export -main
    []
    (slatecljs.common/render-demo
      App
      {:title title
       :description "Press Ctrl+b to toggle bold."
       :cljs-source (with-out-str (cljs.repl/source App))
       :js-source (with-out-str (cljs.repl/doc App))
       :navigation [(let [anchor "w05"]
                      {:text (common/title anchor)
                       :url (str "#" anchor)
                       :class "next"})
                    {:text title
                     :url "https://docs.slatejs.org/walkthroughs/04-applying-custom-formatting"
                     :class "slate-tutorial"}
                    {:text (namespace ::x)
                     :url bookmark
                     :class "source-link"}
                    {:text "<App>"
                     :url app-bookmark
                     :class "source-link"}
                    {:text "<Leaf>"
                     :url leaf-bookmark
                     :class "source-link"}
                    (let [anchor "w03"]
                      {:text (common/title anchor)
                       :url (str "#" anchor)
                       :class "previous"})]}))

  (defmethod common/app-component anchor [_] -main)
  (defmethod common/title anchor [_] title))
