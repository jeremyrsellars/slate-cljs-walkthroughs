(ns slatecljs.slate-01-installing-slate
  (:require [clojure.string :as string]
            [react :as React :refer [useEffect useMemo useState]]
            [slate]))
            ; :refer [createEditor]]
  ;(:import [Slate]))

(defn App
  "const App = () => {
  const editor = useMemo(() => withReact(createEditor()), [])
  // Add the initial value when setting up our state.
  const [value, setValue] = useState([
    {
      type: 'paragraph',
      children: [{ text: 'A line of text in a paragraph.' }],
    },
  ])

  return (
    <Slate editor={editor} value={value} onChange={value => setValue(value)}>
      <Editable />
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
      (React.createElement js/Editable #js{}))))

(defn -main 
  []
  (let [app-host-element (js/document.getElementById "app")]
    (js/ReactDOM.render
        (js/React.createElement App
          #js {})
        app-host-element)))
