package com.silentgames.graphic.engine

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import com.silentgames.core.logic.Constants
import com.silentgames.graphic.Assets
import com.silentgames.graphic.engine.base.Sprite

open class Background(
        axis: EngineAxis,
        bmpId: String,
        assets: Assets
) : Sprite(axis, bmpId, assets) {

    override fun initAnimation(textures: Array<TextureRegion>): Animation<TextureRegion> =
            Animation(4f, textures, Animation.PlayMode.LOOP_PINGPONG)

    override fun draw(batch: Batch, width: Int, height: Int, stateTime: Float) {
        val axis = getCoordinates(axis, width, height)
        runningAnimation?.getKeyFrame(stateTime, true)?.let {
            batch.draw(
                    it,
                    axis.x,
                    axis.y,
                    getSize(width, Constants.verticalCountOfCells),
                    getSize(height, Constants.horizontalCountOfCells)
            )
        }
    }

}