package com.silentgames.graphic.manager.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.SerializationException
import com.google.gson.*
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.Turn
import com.silentgames.core.logic.ecs.component.Component
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.Transport
import com.silentgames.core.logic.ecs.entity.ComponentChangeHandler
import com.silentgames.core.logic.ecs.entity.cell.CellEcs
import com.silentgames.core.logic.ecs.entity.event.EventEcs
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import java.lang.reflect.Type


object GameManager {

    private val json = GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Transport::class.java, TransportDeserializer())
            .registerTypeAdapter(CellEcs::class.java, AnyClassTypeAdapter())
            .registerTypeAdapter(UnitEcs::class.java, AnyClassTypeAdapter())
            .registerTypeAdapter(EventEcs::class.java, AnyClassTypeAdapter())
            .registerTypeAdapter(GameState::class.java, GameStateDeserializer())
            .registerTypeAdapter(ComponentChangeHandler::class.java, ChangeHandlerDeserializer())
            .registerTypeAdapter(Component::class.java, AnyClassTypeAdapter())
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

class AnyClassTypeAdapter : JsonDeserializer<Any>, JsonSerializer<Any> {

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

class ChangeHandlerDeserializer : JsonDeserializer<ComponentChangeHandler> {
    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): ComponentChangeHandler = ComponentChangeHandler()
}

class TransportDeserializer : JsonDeserializer<Transport> {
    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): Transport = Transport()
}

class GameStateDeserializer : JsonDeserializer<GameState> {
    override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
    ): GameState {
        if (json is JsonObject) {

            val mutableEventList = json.getAsJsonArray("mutableEventList").map {
                context.deserialize<EventEcs>(it, EventEcs::class.java)
            }
            val mutableCellList = json.getAsJsonArray("mutableCellList").map {
                context.deserialize<CellEcs>(it, CellEcs::class.java)
            }
            val mutableUnitList = json.getAsJsonArray("mutableUnitList").map {
                context.deserialize<UnitEcs>(it, UnitEcs::class.java)
            }
            val aiFractionList = json.getAsJsonArray("aiFractionList").map {
                context.deserialize<FractionsType>(it, FractionsType::class.java)
            }
            val turn = json.getAsJsonObject("turn")

            return GameState(
                    mutableEventList.toMutableList(),
                    mutableCellList.toMutableList(),
                    mutableUnitList.toMutableList(),
                    aiFractionList,
                    context.deserialize<Turn>(turn, Turn::class.java)
            )
        } else {
            throw SerializationException("Wrong data")
        }
    }
}