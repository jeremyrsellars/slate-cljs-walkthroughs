(ns slatecljs.nodes)

(comment
  (defrecord Text [^String text]) ; Can't really do this because of Text.isText using isPlainObject

  (defrecord Editor [children])

  (defrecord Element [children])

  (defn path
    [& path-indexes]
    (into-array path-indexes))

  (defrecord Point [path ^int offset])

  (defrecord Range [^Point anchor ^Point focus])

  (defn selection
    [editor]
    (->Range (.-selection editor))))
