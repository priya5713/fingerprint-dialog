package com.livinglife.utilities

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import java.io.File
import java.util.Arrays
import java.util.HashMap

object Utils {

    private val cachedFontMap = HashMap<String, Typeface>()

    fun pxToSp(context: Context, px: Float): Int {
        return Math.round(px / context.resources.displayMetrics.scaledDensity)
    }

    fun spToPx(context: Context, sp: Float): Int {
        return Math.round(sp * context.resources.displayMetrics.scaledDensity)
    }

    fun findFont(context: Context, fontPath: String?, defaultFontPath: String?): Typeface? {

        if (fontPath == null) {
            return Typeface.DEFAULT
        }

        val fontName = File(fontPath).name
        var defaultFontName = ""
        if (!TextUtils.isEmpty(defaultFontPath)) {
            defaultFontName = File(defaultFontPath).name
        }

        return if (cachedFontMap.containsKey(fontName)) {
            cachedFontMap[fontName]
        } else {
            try {
                val assets = context.resources.assets

                if (Arrays.asList(*assets.list("")).contains(fontPath)) {
                    val typeface = Typeface.createFromAsset(context.assets, fontName)
                    cachedFontMap[fontName] = typeface
                    typeface
                } else if (Arrays.asList(*assets.list("fonts")).contains(fontName)) {
                    val typeface = Typeface.createFromAsset(context.assets, String.format("fonts/%s", fontName))
                    cachedFontMap[fontName] = typeface
                    typeface
                } else if (Arrays.asList(*assets.list("iconfonts")).contains(fontName)) {
                    val typeface = Typeface.createFromAsset(context.assets, String.format("iconfonts/%s", fontName))
                    cachedFontMap[fontName] = typeface
                    typeface
                } else if (!TextUtils.isEmpty(defaultFontPath) && Arrays.asList(*assets.list("")).contains(defaultFontPath)) {
                    val typeface = Typeface.createFromAsset(context.assets, defaultFontPath)
                    cachedFontMap[defaultFontName] = typeface
                    typeface
                } else {
                    throw Exception("Font not Found")
                }

            } catch (e: Exception) {
//                Log.e(CustomViewLayout.TAG, String.format("Unable to find %s font. Using Typeface.DEFAULT instead.", fontName))
                cachedFontMap[fontName] = Typeface.DEFAULT
                Typeface.DEFAULT
            }
        }
    }
}