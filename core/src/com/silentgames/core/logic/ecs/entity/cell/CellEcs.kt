package com.silentgames.core.logic.ecs.entity.cell

import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.component.Texture
import com.silentgames.core.logic.ecs.entity.EntityEcs

open class CellEcs(
        position: Position,
        description: Description,
        texture: Texture
) : EntityEcs() {

    init {
        addComponent(position)
        addComponent(description)
        addComponent(texture)
    }

}