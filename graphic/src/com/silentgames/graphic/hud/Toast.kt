package com.silentgames.graphic.hud

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.BitmapFontCache
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.Viewport

class Toast internal constructor(
    private val msg: String?,
    length: Length,
    private val font: BitmapFont?,
    private val backgroundColor: Color?,
    private val fadingDuration: Float,
    private val maxRelativeWidth: Float,
    private val fontColor: Color,
    // left bottom corner
    private val positionY: Float,
    private val customMargin: Int?,
    private var toastWidth: Int = 0,
    private var toastHeight: Int = 0,
    private var timeToLive: Float = Length.SHORT.duration,
    private var positionX: Float = 0f,
    private var fontX: Float = 0f,
    // left top corner
    private var fontY: Float = 0f,
    private var fontWidth: Int = 0
) {
    // in seconds
    enum class Length(val duration: Float) {
        SHORT(2f), LONG(3.5f);
    }

    private val spriteBatch = SpriteBatch()
    private var renderer: ShapeRenderer? = null
    private var opacity = 1f

    init {
        timeToLive = length.duration
        update(Gdx.graphics.width.toFloat())
    }

    fun update(screenWidth: Float) {
        renderer = ShapeRenderer()
        renderer?.color = backgroundColor
        // measure text box
        val layoutSimple = GlyphLayout()
        layoutSimple.setText(font, msg)
        val lineHeight = layoutSimple.height.toInt()
        fontWidth = layoutSimple.width.toInt()
        var fontHeight = layoutSimple.height.toInt()
        val margin = customMargin ?: lineHeight * 2
        val maxTextWidth = screenWidth * maxRelativeWidth
        if (fontWidth > maxTextWidth) {
            val cache = BitmapFontCache(font, true)
            val layout: GlyphLayout = cache.addText(msg, 0f, 0f, maxTextWidth, Align.center, true)
            fontWidth = layout.width.toInt()
            fontHeight = layout.height.toInt()
        }
        toastHeight = fontHeight + 2 * margin
        toastWidth = fontWidth + 2 * margin
        positionX = screenWidth / 2 - toastWidth / 2
        fontX = positionX + margin
        fontY = positionY + margin + fontHeight
    }

    fun render(viewport: Viewport): Boolean = render(viewport, Gdx.graphics.deltaTime)

    /**
     * Displays toast<br></br>
     * Must be called at the end of [Game.render]<br></br>
     * @param delta [Gdx.graphics.deltaTime]
     * @return activeness of the toast (true while being displayed, false otherwise)
     */
    fun render(viewport: Viewport, delta: Float): Boolean {
        timeToLive -= delta
        if (timeToLive < 0) {
            return false
        }
        renderer?.begin(ShapeRenderer.ShapeType.Filled)
        renderer?.circle(positionX, positionY + toastHeight / 2, toastHeight / 2.toFloat())
        renderer?.rect(positionX, positionY, toastWidth.toFloat(), toastHeight.toFloat())
        renderer?.circle(
            positionX + toastWidth,
            positionY + toastHeight / 2,
            toastHeight / 2.toFloat()
        )
        renderer?.end()

        val camera: Camera = viewport.camera
        camera.update()

        spriteBatch.projectionMatrix = camera.combined

        spriteBatch.begin()
        if (timeToLive > 0 && opacity > 0.15) {
            if (timeToLive < fadingDuration) {
                opacity = timeToLive / fadingDuration
            }
            font?.setColor(fontColor.r, fontColor.g, fontColor.b, fontColor.a * opacity)
            font?.draw(spriteBatch, msg, fontX, fontY, fontWidth.toFloat(), Align.center, true)
        }
        spriteBatch.end()
        return true
    }

    /**
     * Factory for creating toasts
     */
    class ToastFactory private constructor() {
        private var font: BitmapFont? = null
        private var backgroundColor: Color = Color(55f / 256, 55f / 256, 55f / 256, 1f)
        private var fontColor: Color = Color(1f, 1f, 1f, 1f)
        private var positionY: Float
        private var fadingDuration = 0.5f
        private var maxRelativeWidth = 0.65f
        private var customMargin: Int? = null

        /**
         * Creates new toast
         * @param text message
         * @param length toast duration
         * @return newly created toast
         */
        fun create(text: String?, length: Length): Toast {
            return Toast(
                text,
                length,
                font,
                backgroundColor,
                fadingDuration,
                maxRelativeWidth,
                fontColor,
                positionY,
                customMargin
            )
        }

        /**
         * Builder for creating factory
         */
        class Builder {
            private var built = false
            private val factory = ToastFactory()

            /**
             * Specify font for toasts
             * @param font font
             * @return this
             */
            fun font(font: BitmapFont?): Builder {
                check()
                factory.font = font
                return this
            }

            /**
             * Specify background color for toasts.<br></br>
             * Note: Alpha channel is not supported (yet).<br></br>
             * Default: rgb(55,55,55)
             * @param color background color
             * @return this
             */
            fun backgroundColor(color: Color): Builder {
                check()
                factory.backgroundColor = color
                return this
            }

            /**
             * Specify font color for toasts.<br></br>
             * Default: white
             * @param color font color
             * @return this
             */
            fun fontColor(color: Color): Builder {
                check()
                factory.fontColor = color
                return this
            }

            /**
             * Specify vertical position for toasts<br></br>
             * Default: bottom part
             * @param positionY vertical position of bottom left corner
             * @return this
             */
            fun positionY(positionY: Float): Builder {
                check()
                factory.positionY = positionY
                return this
            }

            /**
             * Specify fading duration for toasts<br></br>
             * Default: 0.5s
             * @param fadingDuration duration in seconds which it takes to disappear
             * @return this
             */
            fun fadingDuration(fadingDuration: Float): Builder {
                check()
                require(fadingDuration >= 0) { "Duration must be non-negative number" }
                factory.fadingDuration = fadingDuration
                return this
            }

            /**
             * Specify max text width for toasts<br></br>
             * Default: 0.65
             * @param maxTextRelativeWidth max text width relative to screen (Eg. 0.5 = max text width is equal to 50% of screen width)
             * @return this
             */
            fun maxTextRelativeWidth(maxTextRelativeWidth: Float): Builder {
                check()
                factory.maxRelativeWidth = maxTextRelativeWidth
                return this
            }

            /**
             * Specify text margin for toasts<br></br>
             * Default: line height
             * @param margin margin in px
             * @return this
             */
            fun margin(margin: Int): Builder {
                check()
                factory.customMargin = margin
                return this
            }

            /**
             * Builds factory
             * @return new factory
             */
            fun build(): ToastFactory {
                check()
                checkNotNull(factory.font) { "Font is not set" }
                built = true
                return factory
            }

            private fun check() {
                check(!built) { "Builder can be used only once" }
            }
        }

        init {
            val screenHeight = Gdx.graphics.height.toFloat()
            val bottomGap = 100f
            positionY = bottomGap + (screenHeight - bottomGap) / 10
        }
    }
}
