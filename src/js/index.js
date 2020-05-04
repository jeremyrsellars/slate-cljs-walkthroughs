import React from "react";
import ReactDOM from 'react-dom';
window.React = React;
window.ReactDOM = ReactDOM;

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

window.hljs = {
	highlightBlock: () => console.log("Uh oh.  hljs.highlightBlock isn't replaced."),
}
