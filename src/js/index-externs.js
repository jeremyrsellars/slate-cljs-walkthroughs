import React from 'react'
import ReactDOM from 'react-dom'
import ReactDOMServer from 'react-dom/server'
globalThis.React = React
globalThis.ReactDOM = ReactDOM
globalThis.ReactDOMServer = ReactDOMServer

import * as Slate from 'slate'
globalThis.slate = Slate

import * as SlateReact from 'slate-react'
globalThis['slate-react'] = SlateReact

import hljs from 'highlight.js/lib/core'
import javascript from 'highlight.js/lib/languages/javascript'
import clojure from 'highlight.js/lib/languages/clojure'
hljs.registerLanguage('javascript', javascript)
hljs.registerLanguage('clojure', clojure)
globalThis.hljs = hljs
