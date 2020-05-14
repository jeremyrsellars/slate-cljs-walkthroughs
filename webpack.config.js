const clientConfig = {
  target: 'web',
  entry: './src/js/index.js',
  output: {
    filename: 'index.bundle.js'
  }
}

const externsConfig = {
    target: 'node',
    entry: './src/js/index-externs.js',
    output: {
      filename: 'externs.bundle.js'
    }
  }

const staticConfig = {
  target: 'node',
  entry: './src/js/index-static.js',
  output: {
    filename: 'static.bundle.js'
  }
}

module.exports = [ clientConfig, externsConfig, staticConfig ];
