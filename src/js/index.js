import hljs from 'highlight.js/lib/core';
import javascript from 'highlight.js/lib/languages/javascript';
import clojure from 'highlight.js/lib/languages/clojure';
hljs.registerLanguage('javascript', javascript);
hljs.registerLanguage('clojure', clojure);
window.hljs = hljs
