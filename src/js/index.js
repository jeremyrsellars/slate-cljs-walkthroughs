import React from "react";
// import React, { useEffect, useMemo, useState } from "react";
import ReactDOM from 'react-dom';
window.React = React;
window.ReactDOM = ReactDOM;

// Import the Slate editor factory.
import { createEditor } from 'slate'
window.createEditor = createEditor

// Import the Slate components and React plugin.
import { Slate, Editable, withReact } from 'slate-react'
window.Slate = Slate
window.Editable = Editable
window.withReact = withReact
