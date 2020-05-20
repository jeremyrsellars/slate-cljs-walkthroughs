(ns slatecljs.slate-02-event-handlers
  (:require cljs.repl
            [clojure.string :as string]
            [react :refer [createElement useEffect useMemo useState]]
            [slate :refer [createEditor]]
            [slate-react :refer [Editable Slate withReact]]
            [slatecljs.common :as common])
  (:require-macros [slatecljs.github :refer [source-bookmark]]))

(def bookmark (source-bookmark "src"))

(defn App
  "const App = () => {
  const editor = useMemo(() => withReact(createEditor()), [])
  const [value, setValue] = useState([
    {
      type: 'paragraph',
      children: [{ text: 'A line of text in a paragraph.' }],
    },
  ])

  return (
    <Slate editor={editor} value={value} onChange={value => setValue(value)}>
      <Editable
        onKeyDown={event => {
          if (event.key === '&') {
            // Prevent the ampersand character from being inserted.
            event.preventDefault()
            // Execute the `insertText` method when the event occurs.
            editor.insertText('and')
          }
        }}
      />
    </Slate>
  )
}"
  []
  (let [editor (useMemo #(withReact (createEditor))
                       #js [])
        ; Add the initial value when setting up our state.
        [value setValue]
        (useState
         #js [#js {:type "paragraph"
                   :children
                   #js [#js {:text "A line of text in a paragraph."}]}])]
    (createElement Slate
      #js {:editor editor
           :value value
           :onChange #(setValue %)}
      (createElement Editable
        #js{:onKeyDown
            (fn onKeyDown [event]
              ; (js/console.log (.-key event)
              (when (= (.-key event) "&")
                ; Prevent the ampersand character from being inserted.
                (.preventDefault event) 
                ; Execute the `insertText` method when the event occurs.
                (.insertText editor "and")))}))))

(let [anchor "w02"
      title "02 Event handlers"]
  (defn ^:export -main
    []
    (slatecljs.common/render-demo
      App
      {:title title
       :anchor anchor
       :objective "Use an event handler to implement custom behavior."
       :description "Automatically replace '&' with 'and' when you type it."
       :cljs-source (with-out-str (cljs.repl/source App))
       :js-source (with-out-str (cljs.repl/doc App))
       :navigation [(let [anchor "w03"]
                      {:text (common/title anchor)
                       :rendered-link (common/rendered-link anchor)
                       :class "next"})
                    {:text title
                     :url "https://docs.slatejs.org/walkthroughs/02-adding-event-handlers"
                     :class "slate-tutorial"}
                    {:text "<App>"
                     :url bookmark
                     :class "source-link"}
                    (let [anchor "w01"]
                      {:text (common/title anchor)
                       :rendered-link (common/rendered-link anchor)
                       :class "previous"})]}))

  (defmethod common/app-component anchor [_] -main)
  (defmethod common/title anchor [_] title))
