var goog = goog || {global: {}}
if(!goog.global) {
	goog.global = {}
}

import React from "react";
import ReactDOM from 'react-dom';
import ReactDOMServer from 'react-dom/server';
goog.global.React = React;
goog.global.ReactDOM = ReactDOM
goog.global['react-dom/server'] = ReactDOMServer;

// Import the Slate editor factory.
import { createEditor, Editor, Node, Transforms, Text } from 'slate'
goog.global.slate = {
	createEditor: createEditor,
	Editor: Editor,
	Node: Node,
	Transforms: Transforms,
	Text: Text,
}
goog.global.createEditor = createEditor
goog.global.Editor = Editor
goog.global.Transforms = Transforms
goog.global.Text = Text

// Import the Slate components and React plugin.
import { Slate, Editable, withReact } from 'slate-react'
goog.global['slate-react'] = {
	Slate: Slate,
	Editable: Editable,
	withReact: withReact,
}

import hljs from 'highlight.js/lib/core';
import javascript from 'highlight.js/lib/languages/javascript';
import clojure from 'highlight.js/lib/languages/clojure';
hljs.registerLanguage('javascript', javascript);
hljs.registerLanguage('clojure', clojure);
goog.global.hljs = hljs
