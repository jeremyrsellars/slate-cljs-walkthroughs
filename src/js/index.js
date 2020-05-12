import React from "react";
import ReactDOM from 'react-dom';
import ReactDOMServer from 'react-dom/server';
window.React = React;
window.ReactDOM = ReactDOM
window['react-dom/server'] = ReactDOMServer;

// Import the Slate editor factory.
import { createEditor, Editor, Node, Transforms, Text } from 'slate'
window.slate = {
	createEditor: createEditor,
	Editor: Editor,
	Node: Node,
	Transforms: Transforms,
	Text: Text,
}
window.createEditor = createEditor
window.Editor = Editor
window.Transforms = Transforms
window.Text = Text

// Import the Slate components and React plugin.
import { Slate, Editable, withReact } from 'slate-react'
window['slate-react'] = {
	Slate: Slate,
	Editable: Editable,
	withReact: withReact,
}

import hljs from 'highlight.js/lib/core';
import javascript from 'highlight.js/lib/languages/javascript';
import clojure from 'highlight.js/lib/languages/clojure';
hljs.registerLanguage('javascript', javascript);
hljs.registerLanguage('clojure', clojure);
window.hljs = hljs
