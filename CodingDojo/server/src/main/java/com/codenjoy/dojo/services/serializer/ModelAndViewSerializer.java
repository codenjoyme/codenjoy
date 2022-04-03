package com.codenjoy.dojo.services.serializer;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.json.JSONObject;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

public class ModelAndViewSerializer extends StdSerializer<ModelAndView> {

    public ModelAndViewSerializer() {
        this(null);
    }

    public ModelAndViewSerializer(Class<ModelAndView> clazz) {
        super(clazz);
    }

    @Override
    public void serialize(ModelAndView value,
                          JsonGenerator generator,
                          SerializerProvider provider) throws IOException
    {
        generator.writeStartObject();
        generator.writeObjectField("status", value.getStatus());
        ModelMap map = value.getModelMap();
        for (String key : map.keySet()) {
            generator.writeObjectField(key, map.get(key));
        }
        generator.writeEndObject();
    }
}