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

package me.jamiemansfield.excel.mod;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
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
import java.util.Objects;

/**
 * An immutable representation of a mod's descriptor (<code>mod.json</code>)
 * in code.
 */
public class ModDescriptor {

    private final String system;
    private final String id;
    private final String name;
    private final String version;

    public ModDescriptor(final String system, final String id, final String name,
            final String version) {
        this.system = system;
        this.id = id;
        this.name = Strings.isNullOrEmpty(name) ? id : name;
        this.version = Strings.isNullOrEmpty(version) ? "0.0.1" : version;
    }

    /**
     * Gets the modification system that will load the mod.
     *
     * @return The mod system
     */
    public final String getSystem() {
        return this.system;
    }

    /**
     * Gets the identifier of the mod being described.
     *
     * @return The mod's identifier
     */
    public final String getId() {
        return this.id;
    }

    /**
     * Gets the name of the mod being described.
     *
     * @return The mod's name
     */
    public final String getName() {
        return this.name;
    }

    /**
     * Gets the version of the mod being described.
     *
     * @return The mod's version
     */
    public final String getVersion() {
        return this.version;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.system, this.id, this.name, this.version);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("system", this.system)
                .add("id", this.id)
                .add("name", this.name)
                .add("version", this.version)
                .toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ModDescriptor)) return false;
        final ModDescriptor that = (ModDescriptor) obj;
        return Objects.equals(this.system, that.system) &&
                Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.version, that.version);
    }

    /**
     * A serialiser and deserialier for {@link ModDescriptor}.
     */
    public static final class Serialiser implements JsonSerializer<ModDescriptor>, JsonDeserializer<ModDescriptor> {

        private static final Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(ModDescriptor.class, new Serialiser())
                .create();

        /**
         * Serialises the {@link ModDescriptor} to json.
         *
         * @param obj The mod descriptor
         * @return The json form
         */
        public static String serialise(final ModDescriptor obj) {
            return gson.toJson(obj);
        }

        /**
         * Deserialises the json form to a {@link ModDescriptor}.
         *
         * @param obj The mod descriptor
         * @return The mod descriptor
         */
        public static ModDescriptor deserialise(final String obj) {
            try {
                return gson.fromJson(obj, ModDescriptor.class);
            } catch (final JsonSyntaxException ex) {
                throw new RuntimeException("Failed to parse mod descriptor!", ex);
            }
        }

        /**
         * Deserialises the given json source, to a {@link ModDescriptor}.
         *
         * @param is The input stream
         * @return The mod descriptor
         */
        public static ModDescriptor deserialise(final InputStream is) {
            try (final InputStreamReader reader = new InputStreamReader(is)) {
                return gson.fromJson(reader, ModDescriptor.class);
            } catch (final JsonSyntaxException | IOException ex) {
                throw new RuntimeException("Failed to parse mod descriptor!", ex);
            }
        }

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
