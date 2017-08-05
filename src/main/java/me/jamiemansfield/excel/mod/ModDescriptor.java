/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
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

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * An immutable representation of a mod's descriptor (<code>mod.json</code>)
 * in code.
 */
public class ModDescriptor {

    private final String mainClass;
    private final String id;
    private final String name;
    private final String version;

    public ModDescriptor(final String mainClass, final String id, final String name,
            final String version) {
        this.mainClass = mainClass;
        this.id = id;
        this.name = Strings.isNullOrEmpty(name) ? id : name;
        this.version = Strings.isNullOrEmpty(version) ? "0.0.1" : version;
    }

    /**
     * Gets the main class of the mod being described.
     *
     * @return The mod's main class
     */
    public final String getMainClass() {
        return this.mainClass;
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
        return Objects.hash(this.mainClass, this.id, this.name, this.version);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("mainClass", this.mainClass)
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
        return Objects.equals(this.mainClass, that.mainClass) &&
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
            } catch (JsonSyntaxException ex) {
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

            if (!(obj.has("mainClass") && obj.has("id"))) {
                throw new JsonParseException("Failed to obtain both a mainClass and an id from " + json);
            }

            return new ModDescriptor(
                    obj.get("mainClass").getAsString(),
                    obj.get("id").getAsString(),
                    obj.has("name") ? obj.get("name").getAsString() : null,
                    obj.has("version") ? obj.get("version").getAsString() : null
            );
        }

        @Override
        public JsonElement serialize(final ModDescriptor src, final Type typeOfSrc,
                final JsonSerializationContext context) {
            final JsonObject json = new JsonObject();

            json.add("mainClass", new JsonPrimitive(src.getMainClass()));
            json.add("id", new JsonPrimitive(src.getId()));
            json.add("name", new JsonPrimitive(src.getName()));
            json.add("version", new JsonPrimitive(src.getVersion()));

            return json;
        }

    }

}
