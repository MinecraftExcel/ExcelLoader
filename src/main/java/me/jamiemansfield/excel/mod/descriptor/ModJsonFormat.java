/*
 * This file is part of ExcelLoader, licensed under the MIT License (MIT).
 *
 * Copyright (c) Jamie Mansfield <https://www.jamierocks.uk/>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package me.jamiemansfield.excel.mod.descriptor;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collection;

public class ModJsonFormat implements DescriptorFormat {

    private static final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(ModDescriptor.class, new Serialiser())
            .create();

    @Override
    public String getFileName() {
        return "mod.json";
    }

    @Override
    public Collection<ModDescriptor> parse(final InputStream stream) {
        try (final InputStreamReader reader = new InputStreamReader(stream)) {
            return gson.fromJson(reader, new TypeToken<Collection<ModDescriptor>>() {}.getType());
        } catch (final JsonSyntaxException | IOException ex) {
            throw new RuntimeException("Failed to parse mod descriptor!", ex);
        }
    }

    /**
     * A serialiser and deserialier for the {@code mod.json} descriptor format.
     */
    public static final class Serialiser implements JsonSerializer<ModDescriptor>, JsonDeserializer<ModDescriptor> {

        @Override
        public ModDescriptor deserialize(final JsonElement json, final Type typeOfT,
                final JsonDeserializationContext context) throws JsonParseException {
            if (!json.isJsonObject()) {
                throw new JsonParseException("Failed to obtain a JsonObject from " + json);
            }
            final JsonObject obj = json.getAsJsonObject();

            if (!(obj.has("system") &&
                    obj.has("id"))) {
                throw new JsonParseException("Failed to obtain the required data from: " + json);
            }

            return new ModDescriptor(
                    obj.get("system").getAsString(),
                    obj.get("id").getAsString(),
                    obj.has("name") ? obj.get("name").getAsString() : null,
                    obj.has("version") ? obj.get("version").getAsString() : null
            );
        }

        @Override
        public JsonElement serialize(final ModDescriptor src, final Type typeOfSrc,
                final JsonSerializationContext context) {
            final JsonObject json = new JsonObject();

            json.add("system", new JsonPrimitive(src.getSystem()));
            json.add("id", new JsonPrimitive(src.getId()));
            json.add("name", new JsonPrimitive(src.getName()));
            json.add("version", new JsonPrimitive(src.getVersion()));

            return json;
        }

    }

}
