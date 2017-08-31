const path = require('path');

module.exports = {
  context: path.join(__dirname, 'src/main/js'),
  entry: [
    './main.js',
  ],
  output: {
    path: path.join(__dirname, 'build/resources/main/static/'),
    filename: 'bundle.js',
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: [
          'babel-loader',
        ],
      },
    ],
  },
  resolve: {
    modules: [
      path.join(__dirname, 'node_modules'),
    ],
  },
};
