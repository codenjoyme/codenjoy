const path = require('path');
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    devtool: 'source-map',

    mode: 'development',

    entry: {
        'index': './src/index.js'
    },

    output: {
        path: __dirname + '/dist/'
    },

    plugins: [
        new HtmlWebpackPlugin({
            template: __dirname + '/src/index.html'
        })
    ],

    module: {
          rules: [
        ]
    }
};
