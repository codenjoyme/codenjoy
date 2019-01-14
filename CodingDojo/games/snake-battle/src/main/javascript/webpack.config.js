const path = require('path');
const dotenv = require('dotenv');
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');

dotenv.config();

const GAME_URL = process.env.GAME_URL;
if (
    !GAME_URL ||
    GAME_URL === 'http://codenjoy.com/codenjoy-contest/board/player/code@gmail.com?code=00000000000000000000'
) {
    console.error('Please, modify .env file and replace with your game url');
    process.exit(1);
}

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
        new webpack.DefinePlugin({
            'process.env': {
              GAME_URL: JSON.stringify(GAME_URL),
            }
        }),
        new HtmlWebpackPlugin({
            template: __dirname + '/src/index.html'
        })
    ],

    module: {
          rules: [
        ]
    }
};
