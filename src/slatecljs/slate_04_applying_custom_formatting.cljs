(ns slatecljs.slate-04-applying-custom-formatting
  (:require cljs.repl
            [clojure.string :as string]
            [goog.object :as gobj]
            [react :as React :refer [createElement useCallback useEffect useMemo useState]]
            [slate :refer [createEditor Editor Text Transforms]]
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
    (createElement "span"
      (doto (gobj/clone (.-attributes props))
        (gobj/set "style" #js {:fontWeight (if bold "bold" "normal")}))
      (.-children props))))

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
                  "Leaf")
                "Leaves are usually inline-style, in that they represent something like text that flows.  Let's define a leaf node that can be bold or not.")
             :cljs-source (with-out-str (cljs.repl/source Leaf))
             :js-source (with-out-str (cljs.repl/doc Leaf))})
          (createElement "h2" #js {}
            "App")
          "Now pull it all together.")})))

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
  (let [editor (useMemo #(withReact (createEditor))
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
               "code" (createElement CodeElement props)
                      (createElement DefaultElement props)))
           #js [])
        ; Define a leaf rendering function that is memoized with `useCallback`.
        renderLeaf
         (useCallback
           (fn renderLeaf [props]
             (createElement Leaf props))
           #js [])]
    
    (createElement Slate
      #js {:editor editor
           :value value
           :onChange #(setValue %)}
      (createElement Editable
        #js{:renderElement renderElement
            ; Pass in the `renderLeaf` function.
            :renderLeaf renderLeaf
            :onKeyDown
            (fn onKeyDown [event]
             (when (.-ctrlKey event)
              (case (.-key event)  
               "`"
               (do
                (.preventDefault event)
                (let [[match] 
                      (es6-iterator-seq
                        (.nodes Editor editor
                                  #js {:match
                                       (fn [n]
                                          (= (.-type n) "code"))}))]

                  ; Toggle the block type depending on whether there's already a match.
                  (.setNodes Transforms
                    editor
                    #js { :type (if match "paragraph" "code")}
                    #js { :match (fn [n] (Editor.isBlock editor n))})))

               "b"
               (do
                (.preventDefault event)
                (.setNodes Transforms
                  editor
                  #js { :bold true}
                  #js { :match (fn [n] (Text.isText n))
                        :split true}))
               
               ;default
               nil)))}))))

(let [anchor "w04"
      title "04 Applying custom formatting"]
  (defn ^:export -main
    []
    (slatecljs.common/render-demo
      App
      {:title title
       :anchor anchor
       :objective "Use an event handler to format a span of text span using a custom leaf node.  Toggling back again is an excercise for the reader, but you can check out Walkthrough #3 for ideas, or just move on to the next step."
       :description "Press Ctrl+b to set selected text as bold."
       :source-comments (source-comments)
       :cljs-source (with-out-str (cljs.repl/source App))
       :js-source (with-out-str (cljs.repl/doc App))
       :navigation [(let [anchor "w05"]
                      {:text (common/title anchor)
                       :url (common/rendered-link anchor)
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
                       :url (common/rendered-link anchor)
                       :class "previous"})]}))

  (defmethod common/app-component anchor [_] -main)
  (defmethod common/title anchor [_] title))
