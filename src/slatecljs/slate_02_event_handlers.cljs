(ns slatecljs.slate-02-event-handlers
  (:require [clojure.string :as string]
            [react :as React :refer [useEffect useMemo useState]]
            [slate]))

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
 (let [editor (useMemo #(js/withReact (js/createEditor)))
       ; Add the initial value when setting up our state.
       [value setValue] (useState #js[#js {:type 'paragraph'
                                           :children #js [#js {:text "A line of text in a paragraph."}]}])]
    (React.createElement js/Slate
      #js {:editor editor
           :value value
           :onChange #(setValue %)}
      (React.createElement js/Editable
        #js{:onKeyDown
            (fn onKeyDown [event]
              ; (js/console.log (.-key event)
              (when (= (.-key event) "&")
                ; Prevent the ampersand character from being inserted.
                (.preventDefault event) 
                ; Execute the `insertText` method when the event occurs.
                (.insertText editor "and")))}))))

(defn -main 
  []
  (let [app-host-element (js/document.getElementById "app")]
    (js/ReactDOM.render
        (js/React.createElement App
          #js {})
        app-host-element)))
