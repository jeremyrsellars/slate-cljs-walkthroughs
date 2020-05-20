import React from 'react'
import ReactDOM from 'react-dom'
import ReactDOMServer from 'react-dom/server'
globalThis.React = React
globalThis.ReactDOM = ReactDOM
globalThis.ReactDOMServer = ReactDOMServer

// Import the Slate editor factory.
import { createEditor, Editor, Node, Transforms, Text } from 'slate'
globalThis.slate = {
	createEditor: createEditor,
	Editor: Editor,
	Node: Node,
	Transforms: Transforms,
	Text: Text,
}
globalThis.createEditor = createEditor
globalThis.Editor = Editor
globalThis.Transforms = Transforms
globalThis.Text = Text

// Import the Slate components and React plugin.
import { Slate, Editable, withReact } from 'slate-react'
globalThis['slate-react'] = {
	Slate: Slate,
	Editable: Editable,
	withReact: withReact,
}

import hljs from 'highlight.js/lib/core'
import javascript from 'highlight.js/lib/languages/javascript'
import clojure from 'highlight.js/lib/languages/clojure'
hljs.registerLanguage('javascript', javascript)
hljs.registerLanguage('clojure', clojure)
globalThis.hljs = hljs
