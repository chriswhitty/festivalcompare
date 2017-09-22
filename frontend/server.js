const express = require('express');
const webpackDevMiddleware = require('webpack-dev-middleware');
const webpack = require('webpack');
const webpackConfig = require('./webpack.config.js');
const proxy = require('express-http-proxy');
const app = express();
const url = require('url');

const compiler = webpack(webpackConfig);

app.use('/api/*', proxy('localhost:8080/api', {
    proxyReqPathResolver: req => {
        return url.parse(req.baseUrl).path
    }
}));

app.use(express.static(__dirname + '/src/main/resources/'));

app.use(webpackDevMiddleware(compiler, {
  hot: true,
  filename: 'bundle.js',
  publicPath: '/',
  stats: {
    colors: true,
  },
  historyApiFallback: true,
}));

const server = app.listen(3000, function() {
  const host = server.address().address;
  const port = server.address().port;
  console.log('Listening at http://%s:%s', host, port);
});
