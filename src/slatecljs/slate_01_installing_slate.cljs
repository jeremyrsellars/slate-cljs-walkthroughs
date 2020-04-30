(ns slatecljs.slate-01-installing-slate
  (:require cljs.repl
            [clojure.string :as string]
            [react :as React :refer [useEffect useMemo useState]]
            [slatecljs.common :as common])
  (:require-macros [slatecljs.github :refer [source-bookmark]]))

(def bookmark (source-bookmark "src"))

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
        [value setValue]
        (useState
         #js [#js {:type "paragraph"
                   :children
                   #js [#js {:text "A line of text in a paragraph."}]}])]
    (React.createElement js/Slate
      #js {:editor editor
           :value value
           :onChange #(setValue %)}
      (React.createElement js/Editable #js{}))))

(defn About
  []
  (React.createElement "div" #js {}
    (React.createElement "p" #js {}
      "You may want to take a look at these files for more on getting Slate, React, NPM, WebPack, and ClojureScript working together."
      (React.createElement "ul" #js {} 
        (React.createElement "li" #js {}
          "NPM"
          (React.createElement "ul" #js {}
            (React.createElement "li" #js {} (React.createElement "a" #js {:href "https://github.com/jeremyrsellars/slate-cljs-walkthroughs/jeremyrsellars/slate-cljs-walkthroughs/blob/master/package.json"} "package.json"))))

        (React.createElement "li" #js {}
          "WebPack"
          (React.createElement "ul" #js {}
            (React.createElement "li" #js {} (React.createElement "a" #js {:href "https://github.com/jeremyrsellars/slate-cljs-walkthroughs/jeremyrsellars/slate-cljs-walkthroughs/blob/master/webpack.config.js"} "webpack.config.js"))
            (React.createElement "li" #js {} (React.createElement "a" #js {:href "https://github.com/jeremyrsellars/slate-cljs-walkthroughs/jeremyrsellars/slate-cljs-walkthroughs/blob/master/src/js/index.js"} "src/js/index.js"))
            (React.createElement "li" #js {}
              "Seeding the webpack "
              (React.createElement "code" #js {}
                "npx webpack")
              (React.createElement "br" #js {})
              "This command refers to " 
              (React.createElement "code" #js {} "webpack.config.js")
              " and "
              (React.createElement "code" #js {} "src/js/index.js")
              " to generate "
              (React.createElement "code" #js {} "dist/index.bundle.js"))))

        (React.createElement "li" #js {} 
          "Figwheel and ClojureScript"
          (React.createElement "ul" #js {}
            (React.createElement "li" #js {} (React.createElement "a" #js {:href "https://github.com/jeremyrsellars/slate-cljs-walkthroughs/jeremyrsellars/slate-cljs-walkthroughs/blob/master/deps.edn"} "deps.edn"))
            (React.createElement "li" #js {} (React.createElement "a" #js {:href "https://github.com/jeremyrsellars/slate-cljs-walkthroughs/jeremyrsellars/slate-cljs-walkthroughs/blob/master/dev.cljs.edn"} "dev.cljs.edn"))
            (React.createElement "li" #js {} "Along with the " (React.createElement "a" #js {:href "https://figwheel.org/docs/npm.html" :rel "nofollow"} "Figwheel.main NPM Modules tutorial"))
            (React.createElement "li" #js {}
              "Running using the clojure CLI: "
              (React.createElement "code" #js {}
                "clojure -m figwheel.main -b dev -r"))))

        (React.createElement "li" #js {} 
          "Building"
          (React.createElement "ul" #js {}
            (React.createElement "li" #js {} (React.createElement "a" #js {:href "https://github.com/jeremyrsellars/slate-cljs-walkthroughs/jeremyrsellars/slate-cljs-walkthroughs/blob/master/deps.edn"} "deps.edn"))
            (React.createElement "li" #js {} (React.createElement "a" #js {:href "https://github.com/jeremyrsellars/slate-cljs-walkthroughs/jeremyrsellars/slate-cljs-walkthroughs/blob/master/dev.cljs.edn"} "dev.cljs.edn"))
            (React.createElement "li" #js {}
              "Building using the clojure CLI: "
              (React.createElement "code" #js {}
                "clojure -m figwheel.main -O advanced --build-once dev")
              (React.createElement "br" #js {})
              "This combines the artifacts from Webpack (React and Slate) "
              "with the ClojureScript and Google Closure libraries "
              "and the runs Closure's advanced compilation to optimize and discard the parts of Clojure and Closure that aren't used."
              (React.createElement "br" #js {})
              "For the walkthroughs, moving from optimizations whitespace to advanced cut out about 1.5 MB taking the single built JS file down to 445kb.")))))))

(let [anchor "w01"
      title "01 Installing slate"]
  (defn ^:export -main
    []
    (slatecljs.common/render-demo
      App
      {:title title
       :about (About)
       :objective "Get started by showing a editable Slate."
       :description "Type some text"
       :cljs-source (with-out-str (cljs.repl/source App))
       :js-source (with-out-str (cljs.repl/doc App))
       :navigation [(let [anchor "w02"]
                      {:text (common/title anchor)
                       :url (str "#" anchor)
                       :class "next"})
                    {:text "<App>"
                     :url bookmark
                     :class "source-link"}
                    {:text title
                     :url "https://docs.slatejs.org/walkthroughs/01-installing-slate"
                     :class "slate-tutorial"}]}))
                    
  (defmethod common/app-component anchor [_] -main)
  (defmethod common/title anchor [_] title))
