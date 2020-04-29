(ns slatecljs.slate-06-saving
  (:require cljs.repl
            [clojure.string :as string]
            [goog.object :as gobj]
            [react :as React :refer [useEffect useMemo useState useCallback]]
            [slatecljs.common :as common]
            [slate :refer [Node]])
  (:require-macros [slatecljs.github :refer [source-bookmark]]))

(def bookmark (source-bookmark "src"))

(defn serialize
  "// Define a serializing function that takes a value and returns a string.
const serialize = value => {
  return (
    value
      // Return the string content of each paragraph in the value's children.
      .map(n => Node.string(n))
      // Join them all with line breaks denoting paragraphs.
      .join('\n')
  )
}"
  [value]
  (string/join "\n"
    (map
      (fn [n] (.string js/slate.Node n))
      value)))
  
(defn deserialize
  "// Define a deserializing function that takes a string and returns a value.
const deserialize = string => {
  // Return a value array of children derived by splitting the string.
  return string.split('\n').map(line => {
    return {
      children: [{ text: line }],
    }
  })
}
"
  [s]
  (into-array
    (map
      (fn [line]
        #js {:children #js [#js { :text line}]})
      (string/split s "\n"))))

(def app-bookmark (source-bookmark "src"))

(defn App
  "const App = () => {
  const editor = useMemo(() => withReact(createEditor()), [])
  // Use our deserializing function to read the data from Local Storage.
  const [value, setValue] = useState(
    deserialize(localStorage.getItem('content')) || ''
  )

  return (
    <Slate
      editor={editor}
      value={value}
      onChange={value => {
        setValue(value)
        // Serialize the value and save the string value to Local Storage.
        localStorage.setItem('content', serialize(value))
      }}
    >
      <Editable />
    </Slate>
  )
}"
  []
  (let [editor (useMemo #(js/withReact (js/createEditor))
                        #js [])
        [value setValue]
        (useState
         ; (js/JSON.parse (.getItem js/localStorage "content"))
         ; Use our deserializing function to read the data from Local Storage.
         (deserialize
          (or (.getItem js/localStorage "content")
              "")))]
    ; Add a toolbar with buttons that call the same methods.
    (React.createElement js/Slate
      #js {:editor editor
           :value value
           :onChange
            (fn [value]
              (setValue value)
              (.setItem js/localStorage "content"
                ;(.stringify js/JSON value)
                (serialize value)))}
      (React.createElement js/Editable #js{}))))

(let [anchor "w06"
      title "06 Saving to a database"]
  (defn ^:export -main
    []
    (slatecljs.common/render-demo
      App
      {:title title
       :objective "Save and load the document with a custom serialization format."
       :description "Type some text and then reload the page.  The text should be restored."
       :cljs-source (with-out-str (cljs.repl/source App))
       :js-source (with-out-str (cljs.repl/doc App))
       :navigation [#_
                    (let [anchor "w07"]
                      {:text (common/title anchor)
                       :url (str "#" anchor)
                       :class "next"})
                    {:text title
                     :url "https://docs.slatejs.org/walkthroughs/06-saving-to-a-database"
                     :class "slate-tutorial"}
                    {:text (namespace ::x)
                     :url bookmark
                     :class "source-link"}
                    {:text "<App>"
                     :url app-bookmark
                     :class "source-link"}
                    (let [anchor "w05"]
                      {:text (common/title anchor)
                       :url (str "#" anchor)
                       :class "previous"})]}))

  (defmethod common/app-component anchor [_] -main)
  (defmethod common/title anchor [_] title))
