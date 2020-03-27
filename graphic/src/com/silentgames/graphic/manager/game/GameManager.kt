package com.silentgames.graphic.manager.game

import com.badlogic.gdx.Gdx
import com.google.gson.*
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Component
import com.silentgames.core.logic.ecs.entity.ComponentChangeHandler
import java.lang.reflect.Type


object GameManager {

    private val json = GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(List::class.java, FunctionListEmptySerializer())
            .registerTypeAdapter(ComponentChangeHandler::class.java, FunctionMapEmptySerializer())
            .registerTypeAdapter(Component::class.java, InterfaceAdapter())
            .create()
    private val fileHandle = Gdx.files.local("data/GameState.json")

    /**
     * Save data to file
     */
    fun saveData(gameState: GameState) {
        Gdx.app.log("[GameManager]", "saveData()")
        fileHandle.writeString(json.toJson(gameState), false)
    }

    /**
     * Load data from file
     */
    fun loadData(): GameState? {
        Gdx.app.log("[GameManager]", "loadData()")
        return if (fileHandle.exists()) {
            try {
                val data = json.fromJson<GameState>(fileHandle.readString(), GameState::class.java)
                if (data != null) {
                    Gdx.app.log("[GameManager]", "loadData success")
                    data
                } else {
                    Gdx.app.log("[GameManager]", "loadData failure")
                    null
                }
            } catch (ex: Exception) {
                fileHandle.delete()
                Gdx.app.log("[GameManager]", "loadData failure", ex)
                null
            }
        } else {
            Gdx.app.log("[GameManager]", "loadData failure")
            null
        }
    }

}

class InterfaceAdapter : JsonDeserializer<Any>, JsonSerializer<Any> {

    companion object {
        const val CLASSNAME = "CLASSNAME"
        const val DATA = "DATA"
    }

    @Throws(JsonParseException::class)
    override fun deserialize(
            jsonElement: JsonElement,
            type: Type,
            jsonDeserializationContext: JsonDeserializationContext
    ): Any {
        val jsonObject = jsonElement.asJsonObject
        val prim = jsonObject.get(CLASSNAME) as JsonPrimitive
        val className = prim.asString
        val objectClass = getObjectClass(className)
        return jsonDeserializationContext.deserialize(jsonObject.get(DATA), objectClass)
    }

    override fun serialize(jsonElement: Any, type: Type, jsonSerializationContext: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty(CLASSNAME, jsonElement.javaClass.name)
        jsonObject.add(DATA, jsonSerializationContext.serialize(jsonElement))
        return jsonObject
    }

    private fun getObjectClass(className: String): Class<*> {
        try {
            return Class.forName(className)
        } catch (e: ClassNotFoundException) {
            throw JsonParseException(e.message)
        }

    }
}

class FunctionListEmptySerializer : JsonSerializer<List<*>> {
    override fun serialize(
            src: List<*>,
            typeOfSrc: Type,
            context: JsonSerializationContext
    ): JsonElement = if (src.filterIsInstance<Function<*>>().isNotEmpty()) {
        JsonArray()
    } else {
        context.serialize(src)
    }

}

class FunctionMapEmptySerializer : JsonSerializer<ComponentChangeHandler> {
    override fun serialize(src: ComponentChangeHandler, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonObject().apply {
            add("onComponentChangedList", JsonArray())
        }
    }
}