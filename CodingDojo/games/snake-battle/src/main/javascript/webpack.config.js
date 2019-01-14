/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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
