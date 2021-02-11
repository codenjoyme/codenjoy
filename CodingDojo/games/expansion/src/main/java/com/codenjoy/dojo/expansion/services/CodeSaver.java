package com.codenjoy.dojo.expansion.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Oleksandr_Baglai on 2017-08-05.
 */
public class CodeSaver { // TODO test me

    public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss-SSS");

    private static File dir;

    static {
        dir = new File("code");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void save(String user, long date, int index, int count, String code) {
        user = clean(user);
        String time = formatter.format(new Date(date));

        try (FileWriter fw = new FileWriter(dir.getAbsolutePath() + "/" + time + "_" + user + ".js_(" + (index + 1) + "_of_" + count + ")");
             BufferedWriter bw = new BufferedWriter(fw))
        {
            bw.write(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String clean(String user) {
        return user.replaceAll("[^\\dA-Za-z0-9@\\.]", "_");
    }
}
