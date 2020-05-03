(ns slatecljs.slate-01-installing-slate
  (:require cljs.repl
            [clojure.string :as string]
            [react :as React :refer [createElement useEffect useMemo useState]]
            [slate :refer [createEditor]]
            [slate-react :refer [Editable Slate withReact]]
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
      (createElement Editable #js{}))))

(defn About
  []
  (createElement "div" #js {}
    (createElement "p" #js {}
      "You may want to take a look at these files for more on getting Slate, React, NPM, WebPack, and ClojureScript working together."
      (createElement "ul" #js {} 
        (createElement "li" #js {}
          "NPM"
          (createElement "ul" #js {}
            (createElement "li" #js {} (createElement "a" #js {:href "https://github.com/jeremyrsellars/slate-cljs-walkthroughs/jeremyrsellars/slate-cljs-walkthroughs/blob/master/package.json"} "package.json"))))

        (createElement "li" #js {}
          "WebPack"
          (createElement "ul" #js {}
            (createElement "li" #js {} (createElement "a" #js {:href "https://github.com/jeremyrsellars/slate-cljs-walkthroughs/jeremyrsellars/slate-cljs-walkthroughs/blob/master/webpack.config.js"} "webpack.config.js"))
            (createElement "li" #js {} (createElement "a" #js {:href "https://github.com/jeremyrsellars/slate-cljs-walkthroughs/jeremyrsellars/slate-cljs-walkthroughs/blob/master/src/js/index.js"} "src/js/index.js"))
            (createElement "li" #js {}
              "Seeding the webpack "
              (createElement "code" #js {}
                "npx webpack")
              (createElement "br" #js {})
              "This command refers to " 
              (createElement "code" #js {} "webpack.config.js")
              " and "
              (createElement "code" #js {} "src/js/index.js")
              " to generate "
              (createElement "code" #js {} "dist/index.bundle.js"))))

        (createElement "li" #js {} 
          "Figwheel and ClojureScript"
          (createElement "ul" #js {}
            (createElement "li" #js {} (createElement "a" #js {:href "https://github.com/jeremyrsellars/slate-cljs-walkthroughs/jeremyrsellars/slate-cljs-walkthroughs/blob/master/deps.edn"} "deps.edn"))
            (createElement "li" #js {} (createElement "a" #js {:href "https://github.com/jeremyrsellars/slate-cljs-walkthroughs/jeremyrsellars/slate-cljs-walkthroughs/blob/master/dev.cljs.edn"} "dev.cljs.edn"))
            (createElement "li" #js {} "Along with the " (createElement "a" #js {:href "https://figwheel.org/docs/npm.html" :rel "nofollow"} "Figwheel.main NPM Modules tutorial"))
            (createElement "li" #js {}
              "Running using the clojure CLI: "
              (createElement "code" #js {}
                "clojure -m figwheel.main -b dev -r"))))

        (createElement "li" #js {} 
          "Building"
          (createElement "ul" #js {}
            (createElement "li" #js {} (createElement "a" #js {:href "https://github.com/jeremyrsellars/slate-cljs-walkthroughs/jeremyrsellars/slate-cljs-walkthroughs/blob/master/deps.edn"} "deps.edn"))
            (createElement "li" #js {} (createElement "a" #js {:href "https://github.com/jeremyrsellars/slate-cljs-walkthroughs/jeremyrsellars/slate-cljs-walkthroughs/blob/master/dev.cljs.edn"} "dev.cljs.edn"))
            (createElement "li" #js {}
              "Building using the clojure CLI: "
              (createElement "code" #js {}
                "clojure -m figwheel.main -O advanced --build-once dev")
              (createElement "br" #js {})
              "This combines the artifacts from Webpack (React and Slate) "
              "with the ClojureScript and Google Closure libraries "
              "and the runs Closure's advanced compilation to optimize and discard the parts of Clojure and Closure that aren't used."
              (createElement "br" #js {})
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
