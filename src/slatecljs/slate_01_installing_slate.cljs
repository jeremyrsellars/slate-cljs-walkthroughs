(ns slatecljs.slate-01-installing-slate
  (:require [clojure.string :as string]
            [react :as React :refer [useEffect useMemo useState]]
            slatecljs.common))

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
 (let [editor (useMemo #(js/withReact (js/createEditor))
                       #js [])
       ; Add the initial value when setting up our state.
       [value setValue] (useState #js[#js {:type "paragraph"
                                           :children #js [#js {:text "A line of text in a paragraph."}]}])]
    (React.createElement js/Slate
      #js {:editor editor
           :value value
           :onChange #(setValue %)}
      (React.createElement js/Editable #js{}))))

(defn -main 
  []
  (slatecljs.common/render-demo
    App
    (with-out-str (cljs.repl/source App))
    (with-out-str (cljs.repl/doc App))))
