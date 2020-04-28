import React from "react";
// import React, { useEffect, useMemo, useState } from "react";
import ReactDOM from 'react-dom';
window.React = React;
window.ReactDOM = ReactDOM;

// Import the Slate editor factory.
import { createEditor, Editor, Transforms, Text } from 'slate'
window.slate = {
	createEditor: createEditor,
	Editor: Editor,
	Transforms: Transforms,
	Text: Text,
}
window.createEditor = createEditor
window.Editor = Editor
window.Transforms = Transforms
window.Text = Text

// Import the Slate components and React plugin.
import { Slate, Editable, withReact } from 'slate-react'
window.Slate = Slate
window.Editable = Editable
window.withReact = withReact
