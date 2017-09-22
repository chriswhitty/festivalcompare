const path = require('path');

module.exports = {
    context: path.join(__dirname, '.'),
    entry: [
        './src/main/js/main.js',
    ],
    output: {
        path: path.join(__dirname, 'build/webpack'),
        filename: 'bundle.js',
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['env', 'react']
                    }
                }
            },
        ],
    },
    resolve: {
        modules: [
            path.join(__dirname, 'node_modules'),
        ],
    },
};
