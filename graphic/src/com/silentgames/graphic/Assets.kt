package com.silentgames.graphic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.SkinLoader
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.ObjectMap
import kotlin.math.min

class Assets {

    private val manager: AssetManager = AssetManager()

    private val fontChars = "абвгдежзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+±=()*&.;:,{}\"´`'<>"
    private val skinResources = ObjectMap<String, Any>()

    private val atlasDescriptor = AssetDescriptor("atlas/game.atlas", TextureAtlas::class.java)
    private val skinDescriptor = AssetDescriptor("ui/default-ui-skin.json", Skin::class.java,
            SkinLoader.SkinParameter("atlas/game.atlas", skinResources))

//    private val i18nDescriptor = AssetDescriptor("i18n/medieval-tycoon", I18NBundle::class.java, I18NBundleLoader.I18NBundleParameter(Locale.getDefault()))

    val uiSkin: Skin by lazy {
        manager.get(skinDescriptor)
    }

//    val i18n: I18NBundle by lazy {
//        manager.get(i18nDescriptor)
//    }

    init {
        manager.load(atlasDescriptor)
//        manager.load(i18nDescriptor)

        val resolver = InternalFileHandleResolver()
        manager.setLoader<FreeTypeFontGenerator, FreeTypeFontGeneratorLoader.FreeTypeFontGeneratorParameters>(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(resolver))
        manager.setLoader<BitmapFont, FreetypeFontLoader.FreeTypeFontLoaderParameter>(BitmapFont::class.java, ".ttf", FreetypeFontLoader(resolver))

        loadColors()
        loadDefaultFonts()

        manager.load(skinDescriptor)
        manager.finishLoading()
    }

    private fun loadColors() {
        skinResources.put("color-mongoose", Color.valueOf("BAA083"))

        skinResources.put("clear", Color.CLEAR)
        skinResources.put("black", Color.BLACK)

        skinResources.put("white", Color.WHITE)
        skinResources.put("light_gray", Color.LIGHT_GRAY)
        skinResources.put("gray", Color.GRAY)
        skinResources.put("dark_gray", Color.DARK_GRAY)

        skinResources.put("blue", Color.BLUE)
        skinResources.put("navy", Color.NAVY)
        skinResources.put("royal", Color.ROYAL)
        skinResources.put("slate", Color.SLATE)
        skinResources.put("sky", Color.SKY)
        skinResources.put("cyan", Color.CYAN)
        skinResources.put("teal", Color.TEAL)

        skinResources.put("green", Color.GREEN)
        skinResources.put("chartreuse", Color.CHARTREUSE)
        skinResources.put("lime", Color.LIME)
        skinResources.put("forest", Color.FOREST)
        skinResources.put("olive", Color.OLIVE)

        skinResources.put("yellow", Color.YELLOW)
        skinResources.put("gold", Color.GOLD)
        skinResources.put("goldenrod", Color.GOLDENROD)
        skinResources.put("orange", Color.ORANGE)

        skinResources.put("brown", Color.BROWN)
        skinResources.put("tan", Color.TAN)
        skinResources.put("firebrick", Color.FIREBRICK)

        skinResources.put("red", Color.RED)
        skinResources.put("scarlet", Color.SCARLET)
        skinResources.put("coral", Color.CORAL)
        skinResources.put("salmon", Color.SALMON)
        skinResources.put("pink", Color.PINK)
        skinResources.put("magenta", Color.MAGENTA)

        skinResources.put("purple", Color.PURPLE)
        skinResources.put("violet", Color.VIOLET)
        skinResources.put("maroon", Color.MAROON)
    }

    private fun loadDefaultFonts() {
        val dimension: Int = min(Gdx.graphics.width, Gdx.graphics.height)

        val smallFont = FreetypeFontLoader.FreeTypeFontLoaderParameter()
        smallFont.fontFileName = "fonts/Roboto-Bold.ttf"
        smallFont.fontParameters.characters = fontChars
        smallFont.fontParameters.color = Color.WHITE
        smallFont.fontParameters.borderColor = Color.valueOf("ffffff80")
        smallFont.fontParameters.borderWidth = .5f
        smallFont.fontParameters.size = dimension / 46
        manager.load("small-font.ttf", BitmapFont::class.java, smallFont)
        manager.finishLoadingAsset<Any>("small-font.ttf")
        skinResources.put("small-font", manager.get<Any>("small-font.ttf"))

        val regularFont = FreetypeFontLoader.FreeTypeFontLoaderParameter()
        regularFont.fontFileName = "fonts/Roboto-Bold.ttf"
        regularFont.fontParameters.characters = fontChars
        regularFont.fontParameters.color = Color.WHITE
        regularFont.fontParameters.borderColor = Color.valueOf("ffffff80")
        regularFont.fontParameters.borderWidth = 1f
        regularFont.fontParameters.shadowColor = Color.valueOf("00000080")
        regularFont.fontParameters.shadowOffsetX = 3
        regularFont.fontParameters.shadowOffsetY = 3
        regularFont.fontParameters.size = dimension / 40
        manager.load("regular-font.ttf", BitmapFont::class.java, regularFont)
        manager.finishLoadingAsset<Any>("regular-font.ttf")
        skinResources.put("regular-font", manager.get<Any>("regular-font.ttf"))

        val largeFont = FreetypeFontLoader.FreeTypeFontLoaderParameter()
        largeFont.fontFileName = "fonts/Merriweather-Bold.ttf"
        largeFont.fontParameters.characters = fontChars
        largeFont.fontParameters.color = Color.WHITE
        largeFont.fontParameters.borderColor = Color.valueOf("00000080")
        largeFont.fontParameters.borderWidth = 4f
        largeFont.fontParameters.size = dimension / 26
        manager.load("large-font.ttf", BitmapFont::class.java, largeFont)
        manager.finishLoadingAsset<Any>("large-font.ttf")
        skinResources.put("large-font", manager.get<Any>("large-font.ttf"))
    }
}