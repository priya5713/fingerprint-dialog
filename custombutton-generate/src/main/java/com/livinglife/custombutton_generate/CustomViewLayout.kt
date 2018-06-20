package com.livinglife.custombutton_generate

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import java.util.ArrayList

import com.livinglife.custombutton_generate.R.styleable.*
import com.livinglife.utilities.Utils

class CustomViewLayout : LinearLayout {

    private var mContext: Context? = null

    // # Background Attributes
    private var mDefaultBackgroundColor = Color.BLACK
    private var mFocusBackgroundColor = 0
    private var mDisabledBackgroundColor = Color.parseColor("#f6f7f9")
    private var mDisabledTextColor = Color.parseColor("#bec2c9")
    private var mDisabledBorderColor = Color.parseColor("#dddfe2")

    // # Text Attributes
    private var mDefaultTextColor = Color.WHITE
    private var mDefaultIconColor = Color.WHITE
    private val mTextPosition = 1
    private var mDefaultTextSize = Utils.spToPx(context, 15f)
    private var mDefaultTextGravity = 0x11 // Gravity.CENTER
    private var mText: String? = null

    // # Icon Attributes
    private var mIconResource: Drawable? = null
    private var mFontIconSize = Utils.spToPx(context, 15f)
    private var mFontIcon: String? = null
    private var mIconPosition = 1

    private var mIconPaddingLeft = 10
    private var mIconPaddingRight = 10
    private var mIconPaddingTop = 0
    private var mIconPaddingBottom = 0


    private var mBorderColor = Color.TRANSPARENT
    private var mBorderWidth = 0

    private var mRadius = 0
    private var mRadiusTopLeft = 0
    private var mRadiusTopRight = 0
    private var mRadiusBottomLeft = 0
    private var mRadiusBottomRight = 0

    private var mEnabled = true

    private var mTextAllCaps = false

    private var mTextTypeFace: Typeface? = null
    private var mIconTypeFace: Typeface? = null
    private var textStyle: Int = 0

    private val mDefaultIconFont = "fontawesome.ttf"
    private val mDefaultTextFont = "robotoregular.ttf"

    /**
     * Return Icon of the FancyButton
     *
     * @return ImageView Object
     */
    private var iconImageObject: ImageView? = null
        private set
    /**
     * Return Icon Font of the FancyButton
     *
     * @return TextView Object
     */
    var iconFontObject: TextView? = null
        private set
    /**
     * Return TextView Object of the FancyButton
     *
     * @return TextView Object
     */
    var textViewObject: TextView? = null
        private set

    private var mGhost = false // Default is a solid button !
    private var mUseSystemFont = false // Default is using robotoregular.ttf
    private val mUseRippleEffect = true

    /**
     * Return Text of the button
     *
     * @return Text
     */
    val text: CharSequence
        get() = if (textViewObject != null)
            textViewObject!!.text
        else
            ""

    /**
     * Default constructor
     *
     * @param context : Context
     */
    constructor(context: Context) : super(context) {
        this.mContext = context

        mTextTypeFace = Utils.findFont(mContext!!, mDefaultTextFont, null)
        mIconTypeFace = Utils.findFont(mContext!!, mDefaultIconFont, null)
        initializeFancyButton()
    }


    /**
     * Default constructor called from Layouts
     *
     * @param context : Context
     * @param attrs   : Attributes Array
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.mContext = context

        val attrsArray = context.obtainStyledAttributes(attrs, FancyButtonsAttrs, 0, 0)
        initAttributesArray(attrsArray)
        attrsArray.recycle()

        initializeFancyButton()

    }

    /**
     * Initialize Button dependencies
     * - Initialize Button Container : The LinearLayout
     * - Initialize Button TextView
     * - Initialize Button Icon
     * - Initialize Button Font Icon
     */
    private fun initializeFancyButton() {

        initializeButtonContainer()

        textViewObject = setupTextView()
        iconImageObject = setupIconView()
        iconFontObject = setupFontIconView()

//        val textIndex: Int
//        val view1: View
//        val view2: View


        this.removeAllViews()
        setupBackground()

        val views = ArrayList<View>()

        if (mIconPosition == POSITION_LEFT || mIconPosition == POSITION_TOP) {

            if (iconImageObject != null) {
                views.add(iconImageObject!!)
            }

            if (iconFontObject != null) {
                views.add(iconFontObject!!)
            }
            if (textViewObject != null) {
                views.add(textViewObject!!)
            }

        } else {
            if (textViewObject != null) {
                views.add(textViewObject!!)
            }

            if (iconImageObject != null) {
                views.add(iconImageObject!!)
            }

            if (iconFontObject != null) {
                views.add(iconFontObject!!)
            }
        }

        for (view in views) {
            this.addView(view)
        }
    }

    /**
     * Setup Text View
     *
     * @return : TextView
     */
    private fun setupTextView(): TextView {
        if (mText == null) {
            mText = "Fancy Button"
        }

        val textView = TextView(mContext)
        textView.text = mText

        textView.gravity = mDefaultTextGravity
        textView.setTextColor(if (mEnabled) mDefaultTextColor else mDisabledTextColor)
        textView.textSize = Utils.pxToSp(context, mDefaultTextSize.toFloat()).toFloat()
        textView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        if (!isInEditMode && !mUseSystemFont) {
            textView.setTypeface(mTextTypeFace, textStyle) //we can pass null in first arg
        }
        return textView
    }

    /**
     * Setup Font Icon View
     *
     * @return : TextView
     */
    private fun setupFontIconView(): TextView? {

        if (mFontIcon != null) {
            val fontIconView = TextView(mContext)
            fontIconView.setTextColor(if (mEnabled) mDefaultIconColor else mDisabledTextColor)

            val iconTextViewParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            iconTextViewParams.rightMargin = mIconPaddingRight
            iconTextViewParams.leftMargin = mIconPaddingLeft
            iconTextViewParams.topMargin = mIconPaddingTop
            iconTextViewParams.bottomMargin = mIconPaddingBottom

            if (textViewObject != null) {

                if (mIconPosition == POSITION_TOP || mIconPosition == POSITION_BOTTOM) {
                    iconTextViewParams.gravity = Gravity.CENTER
                    fontIconView.gravity = Gravity.CENTER
                } else {
                    fontIconView.gravity = Gravity.CENTER_VERTICAL
                    iconTextViewParams.gravity = Gravity.CENTER_VERTICAL
                }
            } else {
                iconTextViewParams.gravity = Gravity.CENTER
                fontIconView.gravity = Gravity.CENTER_VERTICAL
            }


            fontIconView.layoutParams = iconTextViewParams
            if (!isInEditMode) {
                fontIconView.textSize = Utils.pxToSp(context, mFontIconSize.toFloat()).toFloat()
                fontIconView.text = mFontIcon
                fontIconView.typeface = mIconTypeFace
            } else {
                fontIconView.textSize = Utils.pxToSp(context, mFontIconSize.toFloat()).toFloat()
                fontIconView.text = "O"
            }
            return fontIconView
        }
        return null
    }

    /**
     * Text Icon resource view
     *
     * @return : ImageView
     */
    private fun setupIconView(): ImageView? {
        if (mIconResource != null) {
            val iconView = ImageView(mContext)
            iconView.setImageDrawable(mIconResource)
            iconView.setPadding(mIconPaddingLeft, mIconPaddingTop, mIconPaddingRight, mIconPaddingBottom)

            val iconViewParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            if (textViewObject != null) {
                if (mIconPosition == POSITION_TOP || mIconPosition == POSITION_BOTTOM)
                    iconViewParams.gravity = Gravity.CENTER
                else
                    iconViewParams.gravity = Gravity.START

                iconViewParams.rightMargin = 10
                iconViewParams.leftMargin = 10
            } else {
                iconViewParams.gravity = Gravity.CENTER_VERTICAL
            }
            iconView.layoutParams = iconViewParams

            return iconView
        }
        return null
    }

    /**
     * Initialize Attributes arrays
     *
     * @param attrsArray : Attributes array
     */
    private fun initAttributesArray(attrsArray: TypedArray) {

        mDefaultBackgroundColor = attrsArray.getColor(FancyButtonsAttrs_fb_defaultColor, mDefaultBackgroundColor)
        mFocusBackgroundColor = attrsArray.getColor(FancyButtonsAttrs_fb_focusColor, mFocusBackgroundColor)
        mDisabledBackgroundColor = attrsArray.getColor(FancyButtonsAttrs_fb_disabledColor, mDisabledBackgroundColor)

        mEnabled = attrsArray.getBoolean(FancyButtonsAttrs_android_enabled, true)

        mDisabledTextColor = attrsArray.getColor(FancyButtonsAttrs_fb_disabledTextColor, mDisabledTextColor)
        mDisabledBorderColor = attrsArray.getColor(FancyButtonsAttrs_fb_disabledBorderColor, mDisabledBorderColor)
        mDefaultTextColor = attrsArray.getColor(FancyButtonsAttrs_fb_textColor, mDefaultTextColor)
        // if default color is set then the icon's color is the same (the default for icon's color)
        mDefaultIconColor = attrsArray.getColor(FancyButtonsAttrs_fb_iconColor, mDefaultTextColor)

        mDefaultTextSize = attrsArray.getDimension(FancyButtonsAttrs_fb_textSize, mDefaultTextSize.toFloat()).toInt()
        mDefaultTextSize = attrsArray.getDimension(FancyButtonsAttrs_android_textSize, mDefaultTextSize.toFloat()).toInt()

        mDefaultTextGravity = attrsArray.getInt(FancyButtonsAttrs_fb_textGravity, mDefaultTextGravity)

        mBorderColor = attrsArray.getColor(FancyButtonsAttrs_fb_borderColor, mBorderColor)
        mBorderWidth = attrsArray.getDimension(FancyButtonsAttrs_fb_borderWidth, mBorderWidth.toFloat()).toInt()

        mRadius = attrsArray.getDimension(FancyButtonsAttrs_fb_radius, mRadius.toFloat()).toInt()

        mRadiusTopLeft = attrsArray.getDimension(FancyButtonsAttrs_fb_radiusTopLeft, mRadius.toFloat()).toInt()
        mRadiusTopRight = attrsArray.getDimension(FancyButtonsAttrs_fb_radiusTopRight, mRadius.toFloat()).toInt()
        mRadiusBottomLeft = attrsArray.getDimension(FancyButtonsAttrs_fb_radiusBottomLeft, mRadius.toFloat()).toInt()
        mRadiusBottomRight = attrsArray.getDimension(FancyButtonsAttrs_fb_radiusBottomRight, mRadius.toFloat()).toInt()

        mFontIconSize = attrsArray.getDimension(FancyButtonsAttrs_fb_fontIconSize, mFontIconSize.toFloat()).toInt()

        mIconPaddingLeft = attrsArray.getDimension(FancyButtonsAttrs_fb_iconPaddingLeft, mIconPaddingLeft.toFloat()).toInt()
        mIconPaddingRight = attrsArray.getDimension(FancyButtonsAttrs_fb_iconPaddingRight, mIconPaddingRight.toFloat()).toInt()
        mIconPaddingTop = attrsArray.getDimension(FancyButtonsAttrs_fb_iconPaddingTop, mIconPaddingTop.toFloat()).toInt()
        mIconPaddingBottom = attrsArray.getDimension(FancyButtonsAttrs_fb_iconPaddingBottom, mIconPaddingBottom.toFloat()).toInt()

        mTextAllCaps = attrsArray.getBoolean(FancyButtonsAttrs_fb_textAllCaps, false)
        mTextAllCaps = attrsArray.getBoolean(FancyButtonsAttrs_android_textAllCaps, false)

        mGhost = attrsArray.getBoolean(FancyButtonsAttrs_fb_ghost, mGhost)
        mUseSystemFont = attrsArray.getBoolean(FancyButtonsAttrs_fb_useSystemFont, mUseSystemFont)

        var text = attrsArray.getString(FancyButtonsAttrs_fb_text)

        if (text == null) { //no fb_text attribute
            text = attrsArray.getString(FancyButtonsAttrs_android_text)
        }

        mIconPosition = attrsArray.getInt(FancyButtonsAttrs_fb_iconPosition, mIconPosition)

        textStyle = attrsArray.getInt(FancyButtonsAttrs_android_textStyle, Typeface.NORMAL)

        val fontIcon = attrsArray.getString(FancyButtonsAttrs_fb_fontIconResource)

        val iconFontFamily = attrsArray.getString(FancyButtonsAttrs_fb_iconFont)
        val textFontFamily = attrsArray.getString(FancyButtonsAttrs_fb_textFont)

        try {
            mIconResource = attrsArray.getDrawable(FancyButtonsAttrs_fb_iconResource)
        } catch (e: Exception) {
            mIconResource = null
        }

        if (fontIcon != null)
            mFontIcon = fontIcon

        if (text != null)
            mText = if (mTextAllCaps) text.toUpperCase() else text

        if (!isInEditMode) {
            mIconTypeFace = if (iconFontFamily != null)
                mContext?.let { Utils.findFont(it, iconFontFamily, mDefaultIconFont) }
            else
                mContext?.let { Utils.findFont(it, mDefaultIconFont, null) }

            mTextTypeFace = if (textFontFamily != null)
                mContext?.let { Utils.findFont(it, textFontFamily, mDefaultTextFont) }
            else
                mContext?.let { Utils.findFont(it, mDefaultTextFont, null) }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getRippleDrawable(defaultDrawable: Drawable, focusDrawable: Drawable, disabledDrawable: Drawable): Drawable {
        return if (!mEnabled) {
            disabledDrawable
        } else {
            RippleDrawable(ColorStateList.valueOf(mFocusBackgroundColor), defaultDrawable, focusDrawable)
        }

    }

    private fun getGradient(startColor: Color, endColor: Color) {

    }

    /**
     * This method applies radius to the drawable corners
     * Specify radius for each corner if radius attribute is not defined
     *
     * @param drawable Drawable
     */
    private fun applyRadius(drawable: GradientDrawable) {
        if (mRadius > 0) {
            drawable.cornerRadius = mRadius.toFloat()
        } else {
            drawable.cornerRadii = floatArrayOf(mRadiusTopLeft.toFloat(), mRadiusTopLeft.toFloat(), mRadiusTopRight.toFloat(), mRadiusTopRight.toFloat(), mRadiusBottomRight.toFloat(), mRadiusBottomRight.toFloat(), mRadiusBottomLeft.toFloat(), mRadiusBottomLeft.toFloat())
        }
    }

    @SuppressLint("NewApi")
    private fun setupBackground() {
        // Default Drawable
        val defaultDrawable = GradientDrawable()
        applyRadius(defaultDrawable)


        if (mGhost) {
            defaultDrawable.setColor(resources.getColor(android.R.color.transparent)) // Hollow Background
        } else {
            defaultDrawable.setColor(mDefaultBackgroundColor)
        }

        //Focus Drawable
        val focusDrawable = GradientDrawable()
        applyRadius(focusDrawable)

        focusDrawable.setColor(mFocusBackgroundColor)

        // Disabled Drawable
        val disabledDrawable = GradientDrawable()
        applyRadius(disabledDrawable)

        disabledDrawable.setColor(mDisabledBackgroundColor)
        disabledDrawable.setStroke(mBorderWidth, mDisabledBorderColor)

        // Handle Border
        if (mBorderColor != 0) {
            defaultDrawable.setStroke(mBorderWidth, mBorderColor)
        }

        // Handle disabled border color
        if (!mEnabled) {
            defaultDrawable.setStroke(mBorderWidth, mDisabledBorderColor)
            if (mGhost) {
                disabledDrawable.setColor(resources.getColor(android.R.color.transparent))
            }
        }


        if (mUseRippleEffect && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            this.background = getRippleDrawable(defaultDrawable, focusDrawable, disabledDrawable)

        } else {

            val states = StateListDrawable()

            // Focus/Pressed Drawable
            val drawable2 = GradientDrawable()
            applyRadius(drawable2)

            if (mGhost) {
                drawable2.setColor(resources.getColor(android.R.color.transparent)) // No focus color
            } else {
                drawable2.setColor(mFocusBackgroundColor)
            }

            // Handle Button Border
            if (mBorderColor != 0) {
                if (mGhost) {
                    drawable2.setStroke(mBorderWidth, mFocusBackgroundColor) // Border is the main part of button now
                } else {
                    drawable2.setStroke(mBorderWidth, mBorderColor)
                }
            }

            if (!mEnabled) {
                if (mGhost) {
                    drawable2.setStroke(mBorderWidth, mDisabledBorderColor)
                } else {
                    drawable2.setStroke(mBorderWidth, mDisabledBorderColor)
                }
            }

            if (mFocusBackgroundColor != 0) {
                states.addState(intArrayOf(android.R.attr.state_pressed), drawable2)
                states.addState(intArrayOf(android.R.attr.state_focused), drawable2)
                states.addState(intArrayOf(-android.R.attr.state_enabled), disabledDrawable)
            }

            states.addState(intArrayOf(), defaultDrawable)

            this.background = states
        }
    }


    /**
     * Initialize button container
     */
    private fun initializeButtonContainer() {

        if (mIconPosition == POSITION_TOP || mIconPosition == POSITION_BOTTOM) {
            this.orientation = LinearLayout.VERTICAL
        } else {
            this.orientation = LinearLayout.HORIZONTAL
        }

        if (this.layoutParams == null) {
            val containerParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            this.layoutParams = containerParams
        }

        this.gravity = Gravity.CENTER
        // disable click listeners for fix bug in this issue as:
        // https://github.com/medyo/Fancybuttons/issues/100
        //this.setClickable(true);
        //this.setFocusable(true);

        if (mIconResource == null && mFontIcon == null && paddingLeft == 0 && paddingRight == 0 && paddingTop == 0 && paddingBottom == 0) {
            //fix for all version of androids and screens
            this.setPadding(20, 0, 20, 0)
        }
    }

    /**
     * Set Text of the button
     *
     * @param text : Text
     */
    fun setText(text: String?) {
        var text = text
        text = if (mTextAllCaps) text!!.toUpperCase() else text
        this.mText = text
        if (textViewObject == null)
            initializeFancyButton()
        else
            textViewObject!!.text = text
    }

    /**
     * Set the capitalization of text
     *
     * @param textAllCaps : is text to be capitalized
     */
    fun setTextAllCaps(textAllCaps: Boolean) {
        this.mTextAllCaps = textAllCaps
        setText(mText)
    }

    /**
     * Set the color of text
     *
     * @param color : Color
     * use Color.parse('#code')
     */
    fun setTextColor(color: Int) {
        this.mDefaultTextColor = color
        if (textViewObject == null)
            initializeFancyButton()
        else
            textViewObject!!.setTextColor(color)

    }

    /**
     * Setting the icon's color independent of the text color
     *
     * @param color : Color
     */
    fun setIconColor(color: Int) {
        if (iconFontObject != null) {
            iconFontObject!!.setTextColor(color)
        }
    }

    /**
     * Set Background color of the button
     *
     * @param color : use Color.parse('#code')
     */
    override fun setBackgroundColor(color: Int) {
        this.mDefaultBackgroundColor = color
        if (iconImageObject != null || iconFontObject != null || textViewObject != null) {
            this.setupBackground()
        }
    }

    /**
     * Set Focus color of the button
     *
     * @param color : use Color.parse('#code')
     */
    fun setFocusBackgroundColor(color: Int) {
        this.mFocusBackgroundColor = color
        if (iconImageObject != null || iconFontObject != null || textViewObject != null)
            this.setupBackground()

    }

    /**
     * Set Disabled state color of the button
     *
     * @param color : use Color.parse('#code')
     */
    fun setDisableBackgroundColor(color: Int) {
        this.mDisabledBackgroundColor = color
        if (iconImageObject != null || iconFontObject != null || textViewObject != null)
            this.setupBackground()

    }

    /**
     * Set Disabled state color of the button text
     *
     * @param color : use Color.parse('#code')
     */
    fun setDisableTextColor(color: Int) {
        this.mDisabledTextColor = color
        if (textViewObject == null)
            initializeFancyButton()
        else if (!mEnabled)
            textViewObject!!.setTextColor(color)

    }

    /**
     * Set Disabled state color of the button border
     *
     * @param color : use Color.parse('#code')
     */
    fun setDisableBorderColor(color: Int) {
        this.mDisabledBorderColor = color
        if (iconImageObject != null || iconFontObject != null || textViewObject != null) {
            this.setupBackground()
        }
    }

    /**
     * Set the size of Text in sp
     *
     * @param textSize : Text Size
     */
    fun setTextSize(textSize: Int) {
        this.mDefaultTextSize = Utils.spToPx(context, textSize.toFloat())
        if (textViewObject != null)
            textViewObject!!.textSize = textSize.toFloat()
    }

    /**
     * Set the gravity of Text
     *
     * @param gravity : Text Gravity
     */

    fun setTextGravity(gravity: Int) {
        this.mDefaultTextGravity = gravity
        if (textViewObject != null) {
            this.gravity = gravity
        }
    }

    /**
     * Set Padding for mIconView and mFontIconSize
     *
     * @param paddingLeft   : Padding Left
     * @param paddingTop    : Padding Top
     * @param paddingRight  : Padding Right
     * @param paddingBottom : Padding Bottom
     */
    fun setIconPadding(paddingLeft: Int, paddingTop: Int, paddingRight: Int, paddingBottom: Int) {
        this.mIconPaddingLeft = paddingLeft
        this.mIconPaddingTop = paddingTop
        this.mIconPaddingRight = paddingRight
        this.mIconPaddingBottom = paddingBottom
        if (iconImageObject != null) {
            iconImageObject!!.setPadding(this.mIconPaddingLeft, this.mIconPaddingTop, this.mIconPaddingRight, this.mIconPaddingBottom)
        }
        if (iconFontObject != null) {
            iconFontObject!!.setPadding(this.mIconPaddingLeft, this.mIconPaddingTop, this.mIconPaddingRight, this.mIconPaddingBottom)
        }
    }

    /**
     * Set an icon from resources to the button
     *
     * @param drawable : Int resource
     */
    fun setIconResource(drawable: Int) {
        this.mIconResource = mContext!!.resources.getDrawable(drawable)
        if (iconImageObject == null || iconFontObject != null) {
            iconFontObject = null
            initializeFancyButton()
        } else
            iconImageObject!!.setImageDrawable(mIconResource)
    }

    /**
     * Set a drawable to the button
     *
     * @param drawable : Drawable resource
     */
    fun setIconResource(drawable: Drawable) {
        this.mIconResource = drawable
        if (iconImageObject == null || iconFontObject != null) {
            iconFontObject = null
            initializeFancyButton()
        } else
            iconImageObject!!.setImageDrawable(mIconResource)
    }

    /**
     * Set a font icon to the button (eg FFontAwesome or Entypo...)
     *
     * @param icon : Icon value eg : \uf082
     */
    fun setIconResource(icon: String) {
        this.mFontIcon = icon
        if (iconFontObject == null) {
            iconImageObject = null
            initializeFancyButton()
        } else
            iconFontObject!!.text = icon
    }

    /**
     * Set Icon size of the button (for only font icons) in sp
     *
     * @param iconSize : Icon Size
     */
    fun setFontIconSize(iconSize: Int) {
        this.mFontIconSize = Utils.spToPx(context, iconSize.toFloat())
        if (iconFontObject != null)
            iconFontObject!!.textSize = iconSize.toFloat()
    }

    /**
     * Set Icon Position
     * Use the global variables (FancyButton.POSITION_LEFT, FancyButton.POSITION_RIGHT, FancyButton.POSITION_TOP, FancyButton.POSITION_BOTTOM)
     *
     * @param position : Position
     */
    fun setIconPosition(position: Int) {
        if (position in 1..4)
            mIconPosition = position
        else
            mIconPosition = POSITION_LEFT

        initializeFancyButton()
    }

    /**
     * Set color of the button border
     *
     * @param color : Color
     * use Color.parse('#code')
     */
    fun setBorderColor(color: Int) {
        this.mBorderColor = color
        if (iconImageObject != null || iconFontObject != null || textViewObject != null) {
            this.setupBackground()
        }
    }

    /**
     * Set Width of the button
     *
     * @param width : Width
     */
    fun setBorderWidth(width: Int) {
        this.mBorderWidth = width
        if (iconImageObject != null || iconFontObject != null || textViewObject != null) {
            this.setupBackground()
        }
    }

    /**
     * Set Border Radius of the button
     *
     * @param radius : Radius
     */
    fun setRadius(radius: Int) {
        this.mRadius = radius
        if (iconImageObject != null || iconFontObject != null || textViewObject != null) {
            this.setupBackground()
        }
    }

    /**
     * Set Border Radius for each button corner
     * Top Left, Top Right, Bottom Left, Bottom Right
     *
     * @param radius : Array of int
     */
    fun setRadius(radius: IntArray) {
        this.mRadiusTopLeft = radius[0]
        this.mRadiusTopRight = radius[1]
        this.mRadiusBottomLeft = radius[2]
        this.mRadiusBottomRight = radius[3]

        if (iconImageObject != null || iconFontObject != null || textViewObject != null) {
            this.setupBackground()
        }
    }

    /**
     * Set custom font for button Text
     *
     * @param fontName : Font Name
     * Place your text fonts in assets
     */
    fun setCustomTextFont(fontName: String) {
        mTextTypeFace = mContext?.let { Utils.findFont(it, fontName, mDefaultTextFont) }

        if (textViewObject == null)
            initializeFancyButton()
        else
            textViewObject!!.setTypeface(mTextTypeFace, textStyle)
    }

    /**
     * Set Custom font for button icon
     *
     * @param fontName : Font Name
     * Place your icon fonts in assets
     */
    fun setCustomIconFont(fontName: String) {

        mIconTypeFace = mContext?.let { Utils.findFont(it, fontName, mDefaultIconFont) }

        if (iconFontObject == null)
            initializeFancyButton()
        else
            iconFontObject!!.typeface = mIconTypeFace

    }

    /**
     * Override setEnabled and rebuild the fancybutton view
     * To redraw the button according to the state : enabled or disabled
     *
     * @param value
     */
    override fun setEnabled(value: Boolean) {
        super.setEnabled(value)
        this.mEnabled = value
        initializeFancyButton()

    }

    /**
     * Setting the button to have hollow or solid shape
     *
     * @param ghost
     */
    fun setGhost(ghost: Boolean) {
        this.mGhost = ghost

        if (iconImageObject != null || iconFontObject != null || textViewObject != null) {
            this.setupBackground()
        }

    }

    /**
     * If enabled, the button title will ignore its custom font and use the default system font
     *
     * @param status : true || false
     */
    fun setUsingSystemFont(status: Boolean) {
        this.mUseSystemFont = status
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private inner class CustomOutline internal constructor(internal var width: Int, internal var height: Int) : ViewOutlineProvider() {

        override fun getOutline(view: View, outline: Outline) {

            if (mRadius == 0) {
                outline.setRect(0, 10, width, height)
            } else {
                outline.setRoundRect(0, 10, width, height, mRadius.toFloat())
            }

        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            outlineProvider = CustomOutline(w, h)
        }
    }

    companion object {

        val TAG = CustomViewLayout::class.java.simpleName

        /**
         * Tags to identify icon position
         */
        const val POSITION_LEFT = 1
        const val POSITION_RIGHT = 2
        const val POSITION_TOP = 3
        const val POSITION_BOTTOM = 4
    }
}
////////////////////////////////


//
//package com.livinglife.custombutton_generate
//
//import android.annotation.SuppressLint
//import android.annotation.TargetApi
//import android.content.Context
//import android.content.res.ColorStateList
//import android.content.res.TypedArray
//import android.graphics.*
//import android.graphics.drawable.Drawable
//import android.graphics.drawable.GradientDrawable
//import android.graphics.drawable.RippleDrawable
//import android.graphics.drawable.StateListDrawable
//import android.os.Build
//import android.support.v4.content.ContextCompat
//import android.support.v4.content.res.ResourcesCompat
//import android.util.AttributeSet
//import android.view.Gravity
//import android.view.View
//import android.view.ViewGroup
//import android.view.ViewOutlineProvider
//import android.widget.FrameLayout
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.TextView
//import com.livinglife.custombutton_generate.CustomViewLayout.LayoutParams.Companion.POSITION_BOTTOM
//import com.livinglife.custombutton_generate.CustomViewLayout.LayoutParams.Companion.POSITION_LEFT
//import com.livinglife.custombutton_generate.CustomViewLayout.LayoutParams.Companion.POSITION_TOP
//import com.livinglife.custombutton_generate.R.styleable.*
//import com.livinglife.utilities.ShapeUtils
//import com.livinglife.utilities.Utils
//import java.util.*
//
//class CustomViewLayout : LinearLayout {
//
//
//    private var mContext: Context? = null
//
//    // # Background Attributes
//    private var mDefaultBackgroundColor = Color.BLACK
//    private var mFocusBackgroundColor = 0
//    private var mDisabledBackgroundColor = Color.parseColor("#f6f7f9")
//    private var mDisabledTextColor = Color.parseColor("#bec2c9")
//    private var mDisabledBorderColor = Color.parseColor("#dddfe2")
//
//    // # Text Attributes
//    private var mDefaultTextColor = Color.WHITE
//    private var mDefaultIconColor = Color.WHITE
//    private val mTextPosition = 1
//    private var mDefaultTextSize = Utils.spToPx(context, 15f)
//    private var mDefaultTextGravity = 0x11 // Gravity.CENTER
//    private var mText: String? = null
//
//    // # Icon Attributes
//    private var mIconResource: Drawable? = null
//    private var mFontIconSize = Utils.spToPx(context, 15f)
//    private var mFontIcon: String? = null
//    private var mIconPosition = 1
//
//    private var mIconPaddingLeft = 10
//    private var mIconPaddingRight = 10
//    private var mIconPaddingTop = 0
//    private var mIconPaddingBottom = 0
//
//
//    private var mBorderColor = Color.TRANSPARENT
//    private var mBorderWidth = 0
//
//    private var mRadius = 0
//    private var mRadiusTopLeft = 0
//    private var mRadiusTopRight = 0
//    private var mRadiusBottomLeft = 0
//    private var mRadiusBottomRight = 0
//
//    private var mEnabled = true
//
//    private var mTextAllCaps = false
//
//    private var mTextTypeFace: Typeface? = null
//    private var mIconTypeFace: Typeface? = null
//    private var textStyle: Int = 0
//
//    private val mDefaultIconFont = "fontawesome.ttf"
//    private val mDefaultTextFont = "robotoregular.ttf"
//
//    /**
//     * Return Icon of the FancyButton
//     *
//     * @return ImageView Object
//     */
//    private var iconImageObject: ImageView? = null
//        private set
//    /**
//     * Return Icon Font of the FancyButton
//     *
//     * @return TextView Object
//     */
//    var iconFontObject: TextView? = null
//        private set
//    /**
//     * Return TextView Object of the FancyButton
//     *
//     * @return TextView Object
//     */
//    var textViewObject: TextView? = null
//        private set
//
//    private var mGhost = false // Default is a solid button !
//    private var mUseSystemFont = false // Default is using robotoregular.ttf
//    private val mUseRippleEffect = true
//
//
//    private val DEFAULT_CHILD_GRAVITY = Gravity.TOP or Gravity.START
//
//    private var SIZE_UNSET = -1
//    private var SIZE_DEFAULT = 0
//    private var foregroundDraw: Drawable? = null
//    private val selfBounds = Rect()
//    private val overlayBounds = Rect()
//    private var foregroundDrawGravity = Gravity.FILL
//    private var foregroundDrawInPadding = true
//    private var foregroundDrawBoundsChanged = false
//
//    var cornerRadiusTL: Float = 0.0f
//    var cornerRadiusTR: Float = 0.0f
//    var cornerRadiusBL: Float = 0.0f
//    var cornerRadiusBR: Float = 0.0f
//
//    private val bgPaint = Paint()
//    var shadowColor: Int = 0
//        set(value) {
//            field = value
//            updatePaintShadow(shadowRadius, shadowDx, shadowDy, value)
//        }
//    var foregroundColor: Int = 0
//        set(value) {
//            field = value
//            updateForegroundColor()
//        }
//    var backgroundClr: Int = 0
//        set(value) {
//            field = value
//            invalidate()
//        }
//
//    var shadowRadius = 0f
//        set(value) {
//            var v = value
//            if (v > getShadowMarginMax() && getShadowMarginMax() != 0f) {
//                v = getShadowMarginMax()
//            }
//            field = value
//            updatePaintShadow(v, shadowDx, shadowDy, shadowColor)
//        }
//        get() {
//            return if (field > getShadowMarginMax() && getShadowMarginMax() != 0f) {
//                getShadowMarginMax()
//            } else {
//                field
//            }
//        }
//    var shadowDx = 0f
//        set(value) {
//            field = value
//            updatePaintShadow(shadowRadius, value, shadowDy, shadowColor)
//        }
//    var shadowDy = 0f
//        set(value) {
//            field = value
//            updatePaintShadow(shadowRadius, shadowDx, value, shadowColor)
//        }
//
//    var shadowMarginTop: Int = 0
//        set(value) {
//            field = value
//            updatePaintShadow()
//        }
//    var shadowMarginLeft: Int = 0
//        set(value) {
//            field = value
//            updatePaintShadow()
//        }
//    var shadowMarginRight: Int = 0
//        set(value) {
//            field = value
//            updatePaintShadow()
//        }
//    var shadowMarginBottom: Int = 0
//        set(value) {
//            field = value
//            updatePaintShadow()
//        }
//
//    init {
//
//    }
//
//    /**
//     * Return Text of the button
//     *
//     * @return Text
//     */
//    val text: CharSequence
//        get() = if (textViewObject != null)
//            textViewObject!!.text
//        else
//            ""
//
//    /**
//     * Default constructor
//     *
//     * @param context : Context
//     */
//    constructor(context: Context) : super(context) {
//        this.mContext = context
//
//        mTextTypeFace = Utils.findFont(mContext!!, mDefaultTextFont, null)
//        mIconTypeFace = Utils.findFont(mContext!!, mDefaultIconFont, null)
//        initializeFancyButton()
//    }
//
//
//    /**
//     * Default constructor called from Layouts
//     *
//     * @param context : Context
//     * @param attrs   : Attributes Array
//     */
//    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
//        this.mContext = context
//
//        val attrsArray = context.obtainStyledAttributes(attrs, FancyButtonsAttrs, 0, 0)
//        initAttributesArray(attrsArray)
//        attrsArray.recycle()
//
//        initializeFancyButton()
//    }
//
//
//    constructor(context: Context, attrs: AttributeSet, defStyleInt: Int) : super(context, attrs, defStyleInt) {
//        this.mContext = context
//
//        initializeFancyButton()
//
//
//        val a = getContext().obtainStyledAttributes(attrs, R.styleable.ShadowView,
//                defStyleInt, 0)
//        shadowColor = a.getColor(R.styleable.ShadowView_shadowColor
//                , ContextCompat.getColor(context, R.color.shadow_view_default_shadow_color))
//        foregroundColor = a.getColor(R.styleable.ShadowView_foregroundColor
//                , ContextCompat.getColor(context, R.color.shadow_view_foreground_color_dark))
//        backgroundClr = a.getColor(R.styleable.ShadowView_backgroundColor, Color.WHITE)
//        shadowDx = a.getFloat(R.styleable.ShadowView_shadowDx, 0f)
//        shadowDy = a.getFloat(R.styleable.ShadowView_shadowDy, 1f)
//        shadowRadius = a.getDimensionPixelSize(R.styleable.ShadowView_shadowRadius, SIZE_DEFAULT).toFloat()
//        val d = a.getDrawable(R.styleable.ShadowView_android_foreground)
//        if (d != null) {
//            setForeground(d)
//        }
//        val shadowMargin = a.getDimensionPixelSize(R.styleable.ShadowView_shadowMargin, SIZE_UNSET)
//        if (shadowMargin >= 0) {
//            shadowMarginTop = shadowMargin
//            shadowMarginLeft = shadowMargin
//            shadowMarginRight = shadowMargin
//            shadowMarginBottom = shadowMargin
//        } else {
//            shadowMarginTop = a.getDimensionPixelSize(R.styleable.ShadowView_shadowMarginTop, SIZE_DEFAULT)
//            shadowMarginLeft = a.getDimensionPixelSize(R.styleable.ShadowView_shadowMarginLeft, SIZE_DEFAULT)
//            shadowMarginRight = a.getDimensionPixelSize(R.styleable.ShadowView_shadowMarginRight, SIZE_DEFAULT)
//            shadowMarginBottom = a.getDimensionPixelSize(R.styleable.ShadowView_shadowMarginBottom, SIZE_DEFAULT)
//        }
//
//        val cornerRadius = a.getDimensionPixelSize(R.styleable.ShadowView_cornerRadius, SIZE_UNSET).toFloat()
//        if (cornerRadius >= 0) {
//            cornerRadiusTL = cornerRadius
//            cornerRadiusTR = cornerRadius
//            cornerRadiusBL = cornerRadius
//            cornerRadiusBR = cornerRadius
//        } else {
//            cornerRadiusTL = a.getDimensionPixelSize(R.styleable.ShadowView_cornerRadiusTL, SIZE_DEFAULT).toFloat()
//            cornerRadiusTR = a.getDimensionPixelSize(R.styleable.ShadowView_cornerRadiusTR, SIZE_DEFAULT).toFloat()
//            cornerRadiusBL = a.getDimensionPixelSize(R.styleable.ShadowView_cornerRadiusBL, SIZE_DEFAULT).toFloat()
//            cornerRadiusBR = a.getDimensionPixelSize(R.styleable.ShadowView_cornerRadiusBR, SIZE_DEFAULT).toFloat()
//        }
//        a.recycle()
//        bgPaint.color = backgroundClr
//        bgPaint.isAntiAlias = true
//        bgPaint.style = Paint.Style.FILL
//        setLayerType(LAYER_TYPE_SOFTWARE, null)
//        setWillNotDraw(false)
//        background = null
//    }
//
//    /**
//     * Initialize Button dependencies
//     * - Initialize Button Container : The LinearLayout
//     * - Initialize Button TextView
//     * - Initialize Button Icon
//     * - Initialize Button Font Icon
//     */
//    private fun initializeFancyButton() {
//
//        initializeButtonContainer()
//
//        textViewObject = setupTextView()
//        iconImageObject = setupIconView()
//        iconFontObject = setupFontIconView()
//
//
//        this.removeAllViews()
//        setupBackground()
//
//        val views = ArrayList<View>()
//
//        if (mIconPosition == POSITION_LEFT || mIconPosition == POSITION_TOP) {
//
//            if (iconImageObject != null) {
//                views.add(iconImageObject!!)
//            }
//
//            if (iconFontObject != null) {
//                views.add(iconFontObject!!)
//            }
//            if (textViewObject != null) {
//                views.add(textViewObject!!)
//            }
//
//        } else {
//            if (textViewObject != null) {
//                views.add(textViewObject!!)
//            }
//
//            if (iconImageObject != null) {
//                views.add(iconImageObject!!)
//            }
//
//            if (iconFontObject != null) {
//                views.add(iconFontObject!!)
//            }
//        }
//
//        for (view in views) {
//            this.addView(view)
//        }
//    }
//
//    /**
//     * Setup Text View
//     *
//     * @return : TextView
//     */
//    private fun setupTextView(): TextView {
//        if (mText == null) {
//            mText = "Fancy Button"
//        }
//
//        val textView = TextView(mContext)
//        textView.text = mText
//
//        textView.gravity = mDefaultTextGravity
//        textView.setTextColor(if (mEnabled) mDefaultTextColor else mDisabledTextColor)
//        textView.textSize = Utils.pxToSp(context, mDefaultTextSize.toFloat()).toFloat()
//        textView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//        if (!isInEditMode && !mUseSystemFont) {
//            textView.setTypeface(mTextTypeFace, textStyle) //we can pass null in first arg
//        }
//        return textView
//    }
//
//    /**
//     * Setup Font Icon View
//     *
//     * @return : TextView
//     */
//    private fun setupFontIconView(): TextView? {
//
//        if (mFontIcon != null) {
//            val fontIconView = TextView(mContext)
//            fontIconView.setTextColor(if (mEnabled) mDefaultIconColor else mDisabledTextColor)
//
//            val iconTextViewParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//            iconTextViewParams.rightMargin = mIconPaddingRight
//            iconTextViewParams.leftMargin = mIconPaddingLeft
//            iconTextViewParams.topMargin = mIconPaddingTop
//            iconTextViewParams.bottomMargin = mIconPaddingBottom
//
//            if (textViewObject != null) {
//
//                if (mIconPosition == POSITION_TOP || mIconPosition == POSITION_BOTTOM) {
//                    iconTextViewParams.gravity = Gravity.CENTER
//                    fontIconView.gravity = Gravity.CENTER
//                } else {
//                    fontIconView.gravity = Gravity.CENTER_VERTICAL
//                    iconTextViewParams.gravity = Gravity.CENTER_VERTICAL
//                }
//            } else {
//                iconTextViewParams.gravity = Gravity.CENTER
//                fontIconView.gravity = Gravity.CENTER_VERTICAL
//            }
//
//
//            fontIconView.layoutParams = iconTextViewParams
//            if (!isInEditMode) {
//                fontIconView.textSize = Utils.pxToSp(context, mFontIconSize.toFloat()).toFloat()
//                fontIconView.text = mFontIcon
//                fontIconView.typeface = mIconTypeFace
//            } else {
//                fontIconView.textSize = Utils.pxToSp(context, mFontIconSize.toFloat()).toFloat()
//                fontIconView.text = "O"
//            }
//            return fontIconView
//        }
//        return null
//    }
//
//    /**
//     * Text Icon resource view
//     *
//     * @return : ImageView
//     */
//    private fun setupIconView(): ImageView? {
//        if (mIconResource != null) {
//            val iconView = ImageView(mContext)
//            iconView.setImageDrawable(mIconResource)
//            iconView.setPadding(mIconPaddingLeft, mIconPaddingTop, mIconPaddingRight, mIconPaddingBottom)
//
//            val iconViewParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//            if (textViewObject != null) {
//                if (mIconPosition == POSITION_TOP || mIconPosition == POSITION_BOTTOM)
//                    iconViewParams.gravity = Gravity.CENTER
//                else
//                    iconViewParams.gravity = Gravity.START
//
//                iconViewParams.rightMargin = 10
//                iconViewParams.leftMargin = 10
//            } else {
//                iconViewParams.gravity = Gravity.CENTER_VERTICAL
//            }
//            iconView.layoutParams = iconViewParams
//
//            return iconView
//        }
//        return null
//    }
//
//    /**
//     * Initialize Attributes arrays
//     *
//     * @param attrsArray : Attributes array
//     */
//    private fun initAttributesArray(attrsArray: TypedArray) {
//
//        mDefaultBackgroundColor = attrsArray.getColor(FancyButtonsAttrs_fb_defaultColor, mDefaultBackgroundColor)
//        mFocusBackgroundColor = attrsArray.getColor(FancyButtonsAttrs_fb_focusColor, mFocusBackgroundColor)
//        mDisabledBackgroundColor = attrsArray.getColor(FancyButtonsAttrs_fb_disabledColor, mDisabledBackgroundColor)
//
//        mEnabled = attrsArray.getBoolean(FancyButtonsAttrs_android_enabled, true)
//
//        mDisabledTextColor = attrsArray.getColor(FancyButtonsAttrs_fb_disabledTextColor, mDisabledTextColor)
//        mDisabledBorderColor = attrsArray.getColor(FancyButtonsAttrs_fb_disabledBorderColor, mDisabledBorderColor)
//        mDefaultTextColor = attrsArray.getColor(FancyButtonsAttrs_fb_textColor, mDefaultTextColor)
//        // if default color is set then the icon's color is the same (the default for icon's color)
//        mDefaultIconColor = attrsArray.getColor(FancyButtonsAttrs_fb_iconColor, mDefaultTextColor)
//
//        mDefaultTextSize = attrsArray.getDimension(FancyButtonsAttrs_fb_textSize, mDefaultTextSize.toFloat()).toInt()
//        mDefaultTextSize = attrsArray.getDimension(FancyButtonsAttrs_android_textSize, mDefaultTextSize.toFloat()).toInt()
//
//        mDefaultTextGravity = attrsArray.getInt(FancyButtonsAttrs_fb_textGravity, mDefaultTextGravity)
//
//        mBorderColor = attrsArray.getColor(FancyButtonsAttrs_fb_borderColor, mBorderColor)
//        mBorderWidth = attrsArray.getDimension(FancyButtonsAttrs_fb_borderWidth, mBorderWidth.toFloat()).toInt()
//
//        mRadius = attrsArray.getDimension(FancyButtonsAttrs_fb_radius, mRadius.toFloat()).toInt()
//
//        mRadiusTopLeft = attrsArray.getDimension(FancyButtonsAttrs_fb_radiusTopLeft, mRadius.toFloat()).toInt()
//        mRadiusTopRight = attrsArray.getDimension(FancyButtonsAttrs_fb_radiusTopRight, mRadius.toFloat()).toInt()
//        mRadiusBottomLeft = attrsArray.getDimension(FancyButtonsAttrs_fb_radiusBottomLeft, mRadius.toFloat()).toInt()
//        mRadiusBottomRight = attrsArray.getDimension(FancyButtonsAttrs_fb_radiusBottomRight, mRadius.toFloat()).toInt()
//
//        mFontIconSize = attrsArray.getDimension(FancyButtonsAttrs_fb_fontIconSize, mFontIconSize.toFloat()).toInt()
//
//        mIconPaddingLeft = attrsArray.getDimension(FancyButtonsAttrs_fb_iconPaddingLeft, mIconPaddingLeft.toFloat()).toInt()
//        mIconPaddingRight = attrsArray.getDimension(FancyButtonsAttrs_fb_iconPaddingRight, mIconPaddingRight.toFloat()).toInt()
//        mIconPaddingTop = attrsArray.getDimension(FancyButtonsAttrs_fb_iconPaddingTop, mIconPaddingTop.toFloat()).toInt()
//        mIconPaddingBottom = attrsArray.getDimension(FancyButtonsAttrs_fb_iconPaddingBottom, mIconPaddingBottom.toFloat()).toInt()
//
//        mTextAllCaps = attrsArray.getBoolean(FancyButtonsAttrs_fb_textAllCaps, false)
//        mTextAllCaps = attrsArray.getBoolean(FancyButtonsAttrs_android_textAllCaps, false)
//
//        mGhost = attrsArray.getBoolean(FancyButtonsAttrs_fb_ghost, mGhost)
//        mUseSystemFont = attrsArray.getBoolean(FancyButtonsAttrs_fb_useSystemFont, mUseSystemFont)
//
//        var text = attrsArray.getString(FancyButtonsAttrs_fb_text)
//
//        if (text == null) { //no fb_text attribute
//            text = attrsArray.getString(FancyButtonsAttrs_android_text)
//        }
//
//        mIconPosition = attrsArray.getInt(FancyButtonsAttrs_fb_iconPosition, mIconPosition)
//
//        textStyle = attrsArray.getInt(FancyButtonsAttrs_android_textStyle, Typeface.NORMAL)
//
//        val fontIcon = attrsArray.getString(FancyButtonsAttrs_fb_fontIconResource)
//
//        val iconFontFamily = attrsArray.getString(FancyButtonsAttrs_fb_iconFont)
//        val textFontFamily = attrsArray.getString(FancyButtonsAttrs_fb_textFont)
//
//        try {
//            mIconResource = attrsArray.getDrawable(FancyButtonsAttrs_fb_iconResource)
//        } catch (e: Exception) {
//            mIconResource = null
//        }
//
//        if (fontIcon != null)
//            mFontIcon = fontIcon
//
//        if (text != null)
//            mText = if (mTextAllCaps) text.toUpperCase() else text
//
//        if (!isInEditMode) {
//            mIconTypeFace = if (iconFontFamily != null)
//                mContext?.let { Utils.findFont(it, iconFontFamily, mDefaultIconFont) }
//            else
//                mContext?.let { Utils.findFont(it, mDefaultIconFont, null) }
//
//            mTextTypeFace = if (textFontFamily != null)
//                mContext?.let { Utils.findFont(it, textFontFamily, mDefaultTextFont) }
//            else
//                mContext?.let { Utils.findFont(it, mDefaultTextFont, null) }
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private fun getRippleDrawable(defaultDrawable: Drawable, focusDrawable: Drawable, disabledDrawable: Drawable): Drawable {
//        return if (!mEnabled) {
//            disabledDrawable
//        } else {
//            RippleDrawable(ColorStateList.valueOf(mFocusBackgroundColor), defaultDrawable, focusDrawable)
//        }
//
//    }
//
//    /**
//     * This method applies radius to the drawable corners
//     * Specify radius for each corner if radius attribute is not defined
//     *
//     * @param drawable Drawable
//     */
//    private fun applyRadius(drawable: GradientDrawable) {
//        if (mRadius > 0) {
//            drawable.cornerRadius = mRadius.toFloat()
//        } else {
//            drawable.cornerRadii = floatArrayOf(mRadiusTopLeft.toFloat(), mRadiusTopLeft.toFloat(), mRadiusTopRight.toFloat(), mRadiusTopRight.toFloat(), mRadiusBottomRight.toFloat(), mRadiusBottomRight.toFloat(), mRadiusBottomLeft.toFloat(), mRadiusBottomLeft.toFloat())
//        }
//    }
//
//    @SuppressLint("NewApi")
//    private fun setupBackground() {
//        // Default Drawable
//        val defaultDrawable = GradientDrawable()
//        applyRadius(defaultDrawable)
//
//
//        if (mGhost) {
//            defaultDrawable.setColor(ResourcesCompat.getColor(resources, android.R.color.transparent, null)) // Hollow Background
//        } else {
//            defaultDrawable.setColor(mDefaultBackgroundColor)
//        }
//
//        //Focus Drawable
//        val focusDrawable = GradientDrawable()
//        applyRadius(focusDrawable)
//
//        focusDrawable.setColor(mFocusBackgroundColor)
//
//        // Disabled Drawable
//        val disabledDrawable = GradientDrawable()
//        applyRadius(disabledDrawable)
//
//        disabledDrawable.setColor(mDisabledBackgroundColor)
//        disabledDrawable.setStroke(mBorderWidth, mDisabledBorderColor)
//
//        // Handle Border
//        if (mBorderColor != 0) {
//            defaultDrawable.setStroke(mBorderWidth, mBorderColor)
//        }
//
//        // Handle disabled border color
//        if (!mEnabled) {
//            defaultDrawable.setStroke(mBorderWidth, mDisabledBorderColor)
//            if (mGhost) {
//                disabledDrawable.setColor(ResourcesCompat.getColor(resources, android.R.color.transparent, null))
//            }
//        }
//
//
//        if (mUseRippleEffect && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//            this.background = getRippleDrawable(defaultDrawable, focusDrawable, disabledDrawable)
//
//        } else {
//
//            val states = StateListDrawable()
//
//            // Focus/Pressed Drawable
//            val drawable2 = GradientDrawable()
//            applyRadius(drawable2)
//
//            if (mGhost) {
//                drawable2.setColor(ResourcesCompat.getColor(resources, android.R.color.transparent, null)) // No focus color
//            } else {
//                drawable2.setColor(mFocusBackgroundColor)
//            }
//
//            // Handle Button Border
//            if (mBorderColor != 0) {
//                if (mGhost) {
//                    drawable2.setStroke(mBorderWidth, mFocusBackgroundColor) // Border is the main part of button now
//                } else {
//                    drawable2.setStroke(mBorderWidth, mBorderColor)
//                }
//            }
//
//            if (!mEnabled) {
//                if (mGhost) {
//                    drawable2.setStroke(mBorderWidth, mDisabledBorderColor)
//                } else {
//                    drawable2.setStroke(mBorderWidth, mDisabledBorderColor)
//                }
//            }
//
//            if (mFocusBackgroundColor != 0) {
//                states.addState(intArrayOf(android.R.attr.state_pressed), drawable2)
//                states.addState(intArrayOf(android.R.attr.state_focused), drawable2)
//                states.addState(intArrayOf(-android.R.attr.state_enabled), disabledDrawable)
//            }
//
//            states.addState(intArrayOf(), defaultDrawable)
//
//            this.background = states
//        }
//    }
//
//
//    /**
//     * Initialize button container
//     */
//    private fun initializeButtonContainer() {
//
//        if (mIconPosition == POSITION_TOP || mIconPosition == POSITION_BOTTOM) {
//            this.orientation = LinearLayout.VERTICAL
//        } else {
//            this.orientation = LinearLayout.HORIZONTAL
//        }
//
//        if (this.layoutParams == null) {
//            val containerParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//            this.layoutParams = containerParams
//        }
//
//        this.gravity = Gravity.CENTER
//        // disable click listeners for fix bug in this issue as:
//        // https://github.com/medyo/Fancybuttons/issues/100
//        //this.setClickable(true);
//        //this.setFocusable(true);
//
//        if (mIconResource == null && mFontIcon == null && paddingLeft == 0 && paddingRight == 0 && paddingTop == 0 && paddingBottom == 0) {
//            //fix for all version of androids and screens
//            this.setPadding(20, 0, 20, 0)
//        }
//    }
//
//    /**
//     * Set Text of the button
//     *
//     * @param text : Text
//     */
//    fun setText(text: String?) {
//        var text = text
//        text = if (mTextAllCaps) text?.toUpperCase() else text
//        this.mText = text
//        if (textViewObject == null)
//            initializeFancyButton()
//        else
//            textViewObject?.text = text
//    }
//
//    /**
//     * Set the capitalization of text
//     *
//     * @param textAllCaps : is text to be capitalized
//     */
//    fun setTextAllCaps(textAllCaps: Boolean) {
//        this.mTextAllCaps = textAllCaps
//        setText(mText)
//    }
//
//    /**
//     * Set the color of text
//     *
//     * @param color : Color
//     * use Color.parse('#code')
//     */
//    fun setTextColor(color: Int) {
//        this.mDefaultTextColor = color
//        if (textViewObject == null)
//            initializeFancyButton()
//        else
//            textViewObject?.setTextColor(color)
//
//    }
//
//    /**
//     * Setting the icon's color independent of the text color
//     *
//     * @param color : Color
//     */
//    fun setIconColor(color: Int) {
//        if (iconFontObject != null) {
//            iconFontObject?.setTextColor(color)
//        }
//    }
//
//    /**
//     * Set Background color of the button
//     *
//     * @param color : use Color.parse('#code')
//     */
//    override fun setBackgroundColor(color: Int) {
//        this.mDefaultBackgroundColor = color
//        if (iconImageObject != null || iconFontObject != null || textViewObject != null) {
//            this.setupBackground()
//        }
//    }
//
//    /**
//     * Set Focus color of the button
//     *
//     * @param color : use Color.parse('#code')
//     */
//    fun setFocusBackgroundColor(color: Int) {
//        this.mFocusBackgroundColor = color
//        if (iconImageObject != null || iconFontObject != null || textViewObject != null)
//            this.setupBackground()
//
//    }
//
//    /**
//     * Set Disabled state color of the button
//     *
//     * @param color : use Color.parse('#code')
//     */
//    fun setDisableBackgroundColor(color: Int) {
//        this.mDisabledBackgroundColor = color
//        if (iconImageObject != null || iconFontObject != null || textViewObject != null)
//            this.setupBackground()
//
//    }
//
//    /**
//     * Set Disabled state color of the button text
//     *
//     * @param color : use Color.parse('#code')
//     */
//    fun setDisableTextColor(color: Int) {
//        this.mDisabledTextColor = color
//        if (textViewObject == null)
//            initializeFancyButton()
//        else if (!mEnabled)
//            textViewObject?.setTextColor(color)
//
//    }
//
//    /**
//     * Set Disabled state color of the button border
//     *
//     * @param color : use Color.parse('#code')
//     */
//    fun setDisableBorderColor(color: Int) {
//        this.mDisabledBorderColor = color
//        if (iconImageObject != null || iconFontObject != null || textViewObject != null) {
//            this.setupBackground()
//        }
//    }
//
//    /**
//     * Set the size of Text in sp
//     *
//     * @param textSize : Text Size
//     */
//    fun setTextSize(textSize: Int) {
//        this.mDefaultTextSize = Utils.spToPx(context, textSize.toFloat())
//        if (textViewObject != null)
//            textViewObject?.textSize = textSize.toFloat()
//    }
//
//    /**
//     * Set the gravity of Text
//     *
//     * @param gravity : Text Gravity
//     */
//
//    fun setTextGravity(gravity: Int) {
//        this.mDefaultTextGravity = gravity
//        if (textViewObject != null) {
//            this.gravity = gravity
//        }
//    }
//
//    /**
//     * Set Padding for mIconView and mFontIconSize
//     *
//     * @param paddingLeft   : Padding Left
//     * @param paddingTop    : Padding Top
//     * @param paddingRight  : Padding Right
//     * @param paddingBottom : Padding Bottom
//     */
//    fun setIconPadding(paddingLeft: Int, paddingTop: Int, paddingRight: Int, paddingBottom: Int) {
//        this.mIconPaddingLeft = paddingLeft
//        this.mIconPaddingTop = paddingTop
//        this.mIconPaddingRight = paddingRight
//        this.mIconPaddingBottom = paddingBottom
//        if (iconImageObject != null) {
//            iconImageObject?.setPadding(this.mIconPaddingLeft, this.mIconPaddingTop, this.mIconPaddingRight, this.mIconPaddingBottom)
//        }
//        if (iconFontObject != null) {
//            iconFontObject?.setPadding(this.mIconPaddingLeft, this.mIconPaddingTop, this.mIconPaddingRight, this.mIconPaddingBottom)
//        }
//    }
//
//    /**
//     * Set an icon from resources to the button
//     *
//     * @param drawable : Int resource
//     */
//    fun setIconResource(drawable: Int) {
//
//        this.mIconResource = mContext?.resources?.getDrawable(drawable)
//        if (iconImageObject == null || iconFontObject != null) {
//            iconFontObject = null
//            initializeFancyButton()
//        } else
//            iconImageObject?.setImageDrawable(mIconResource)
//    }
//
//    /**
//     * Set a drawable to the button
//     *
//     * @param drawable : Drawable resource
//     */
//    fun setIconResource(drawable: Drawable) {
//        this.mIconResource = drawable
//        if (iconImageObject == null || iconFontObject != null) {
//            iconFontObject = null
//            initializeFancyButton()
//        } else
//            iconImageObject?.setImageDrawable(mIconResource)
//    }
//
//    /**
//     * Set a font icon to the button (eg FFontAwesome or Entypo...)
//     *
//     * @param icon : Icon value eg : \uf082
//     */
//    fun setIconResource(icon: String) {
//        this.mFontIcon = icon
//        if (iconFontObject == null) {
//            iconImageObject = null
//            initializeFancyButton()
//        } else
//            iconFontObject?.text = icon
//    }
//
//    /**
//     * Set Icon size of the button (for only font icons) in sp
//     *
//     * @param iconSize : Icon Size
//     */
//    fun setFontIconSize(iconSize: Int) {
//        this.mFontIconSize = Utils.spToPx(context, iconSize.toFloat())
//        if (iconFontObject != null)
//            iconFontObject?.textSize = iconSize.toFloat()
//    }
//
//    /**
//     * Set Icon Position
//     * Use the global variables (FancyButton.POSITION_LEFT, FancyButton.POSITION_RIGHT, FancyButton.POSITION_TOP, FancyButton.POSITION_BOTTOM)
//     *
//     * @param position : Position
//     */
//    fun setIconPosition(position: Int) {
//        mIconPosition = if (position in 1..4) {
//            position
//        } else
//            POSITION_LEFT
//
//        initializeFancyButton()
//    }
//
//    /**
//     * Set color of the button border
//     *
//     * @param color : Color
//     * use Color.parse('#code')
//     */
//    fun setBorderColor(color: Int) {
//        this.mBorderColor = color
//        if (iconImageObject != null || iconFontObject != null || textViewObject != null) {
//            this.setupBackground()
//        }
//    }
//
//    /**
//     * Set Width of the button
//     *
//     * @param width : Width
//     */
//    fun setBorderWidth(width: Int) {
//        this.mBorderWidth = width
//        if (iconImageObject != null || iconFontObject != null || textViewObject != null) {
//            this.setupBackground()
//        }
//    }
//
//    /**
//     * Set Border Radius of the button
//     *
//     * @param radius : Radius
//     */
//    fun setRadius(radius: Int) {
//        this.mRadius = radius
//        if (iconImageObject != null || iconFontObject != null || textViewObject != null) {
//            this.setupBackground()
//        }
//    }
//
//    /**
//     * Set Border Radius for each button corner
//     * Top Left, Top Right, Bottom Left, Bottom Right
//     *
//     * @param radius : Array of int
//     */
//    fun setRadius(radius: IntArray) {
//        this.mRadiusTopLeft = radius[0]
//        this.mRadiusTopRight = radius[1]
//        this.mRadiusBottomLeft = radius[2]
//        this.mRadiusBottomRight = radius[3]
//
//        if (iconImageObject != null || iconFontObject != null || textViewObject != null) {
//            this.setupBackground()
//        }
//    }
//
//    /**
//     * Set custom font for button Text
//     *
//     * @param fontName : Font Name
//     * Place your text fonts in assets
//     */
//    fun setCustomTextFont(fontName: String) {
//        mTextTypeFace = mContext?.let { Utils.findFont(it, fontName, mDefaultTextFont) }
//
//        if (textViewObject == null)
//            initializeFancyButton()
//        else
//            textViewObject!!.setTypeface(mTextTypeFace, textStyle)
//    }
//
//    /**
//     * Set Custom font for button icon
//     *
//     * @param fontName : Font Name
//     * Place your icon fonts in assets
//     */
//    fun setCustomIconFont(fontName: String) {
//
//        mIconTypeFace = mContext?.let { Utils.findFont(it, fontName, mDefaultIconFont) }
//
//        if (iconFontObject == null)
//            initializeFancyButton()
//        else
//            iconFontObject?.typeface = mIconTypeFace
//
//    }
//
//    /**
//     * Override setEnabled and rebuild the fancybutton view
//     * To redraw the button according to the state : enabled or disabled
//     *
//     * @param value
//     */
//    override fun setEnabled(value: Boolean) {
//        super.setEnabled(value)
//        this.mEnabled = value
//        initializeFancyButton()
//
//    }
//
//    /**
//     * Setting the button to have hollow or solid shape
//     *
//     * @param ghost
//     */
//    fun setGhost(ghost: Boolean) {
//        this.mGhost = ghost
//
//        if (iconImageObject != null || iconFontObject != null || textViewObject != null) {
//            this.setupBackground()
//        }
//
//    }
//
//    /**
//     * If enabled, the button title will ignore its custom font and use the default system font
//     *
//     * @param status : true || false
//     */
//    fun setUsingSystemFont(status: Boolean) {
//        this.mUseSystemFont = status
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private inner class CustomOutline internal constructor(internal var width: Int, internal var height: Int) : ViewOutlineProvider() {
//
//        override fun getOutline(view: View, outline: Outline) {
//
//            if (mRadius == 0) {
//                outline.setRect(0, 10, width, height)
//            } else {
//                outline.setRoundRect(0, 10, width, height, mRadius.toFloat())
//            }
//
//        }
//    }
//
////    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
////        super.onSizeChanged(w, h, oldw, oldh)
////
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////            outlineProvider = CustomOutline(w, h)
////        }
////    }
//
//
//    private fun updatePaintShadow() {
//        updatePaintShadow(shadowRadius, shadowDx, shadowDy, shadowColor)
//    }
//
//    private fun updatePaintShadow(radius: Float, dx: Float, dy: Float, color: Int) {
//        bgPaint.setShadowLayer(radius, dx, dy,
//                color)
//        invalidate()
//    }
//
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        var maxHeight = 0
//        var maxWidth = 0
//        var childState = 0
//
//        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec))
//        val shadowMeasureWidthMatchParent = layoutParams.width == LinearLayout.LayoutParams.MATCH_PARENT
//        val shadowMeasureHeightMatchParent = layoutParams.height == LinearLayout.LayoutParams.MATCH_PARENT
//        var widthSpec = widthMeasureSpec
//        if (shadowMeasureWidthMatchParent) {
//            val childWidthSize = measuredWidth - shadowMarginRight - shadowMarginLeft
//            widthSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY)
//        }
//        var heightSpec = heightMeasureSpec
//        if (shadowMeasureHeightMatchParent) {
//            val childHeightSize = measuredHeight - shadowMarginTop - shadowMarginBottom
//            heightSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY)
//        }
//        val child = getChildAt(0)
//        if (child.visibility !== android.view.View.GONE) {
//            measureChildWithMargins(child, widthSpec, 0, heightSpec, 0)
//            val lp = child.layoutParams as LinearLayout.LayoutParams
//            maxWidth =
//                    if (shadowMeasureWidthMatchParent)
//                        Math.max(maxWidth,
//                                child.measuredWidth + lp.leftMargin + lp.rightMargin)
//                    else
//                        Math.max(maxWidth,
//                                child.measuredWidth + shadowMarginLeft + shadowMarginRight + lp.leftMargin + lp.rightMargin)
//            maxHeight =
//                    if (shadowMeasureHeightMatchParent)
//                        Math.max(maxHeight,
//                                child.measuredHeight + lp.topMargin + lp.bottomMargin)
//                    else
//                        Math.max(maxHeight,
//                                child.measuredHeight + shadowMarginTop + shadowMarginBottom + lp.topMargin + lp.bottomMargin)
//
//            childState = android.view.View.combineMeasuredStates(childState, child.measuredState)
//        }
//        maxWidth += paddingLeft + paddingRight
//        maxHeight += paddingTop + paddingBottom
//        maxHeight = Math.max(maxHeight, suggestedMinimumHeight)
//        maxWidth = Math.max(maxWidth, suggestedMinimumWidth)
//        val drawable = foreground
//        if (drawable != null) {
//            maxHeight = Math.max(maxHeight, drawable.minimumHeight)
//            maxWidth = Math.max(maxWidth, drawable.minimumWidth)
//        }
//        setMeasuredDimension(android.view.View.resolveSizeAndState(maxWidth
//                , if (shadowMeasureWidthMatchParent) widthMeasureSpec else widthSpec, childState),
//                android.view.View.resolveSizeAndState(maxHeight
//                        , if (shadowMeasureHeightMatchParent) heightMeasureSpec else heightSpec,
//                        childState shl android.view.View.MEASURED_HEIGHT_STATE_SHIFT))
//    }
//
//    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
//        layoutChildren(left, top, right, bottom, false)
//        if (changed)
//            foregroundDrawBoundsChanged = changed
//    }
//
//    private fun layoutChildren(left: Int, top: Int, right: Int, bottom: Int, forceLeftGravity: Boolean) {
//        val count = childCount
//
//        val parentLeft = getPaddingLeftWithForeground()
//        val parentRight = right - left - getPaddingRightWithForeground()
//
//        val parentTop = getPaddingTopWithForeground()
//        val parentBottom = bottom - top - getPaddingBottomWithForeground()
//
//        for (i in 0..(count - 1)) {
//            val child = getChildAt(i)
//            if (child.visibility != android.view.View.GONE) {
//                val lp = child.layoutParams as LinearLayout.LayoutParams
//
//                val width = child.measuredWidth
//                val height = child.measuredHeight
//
//                var childLeft = 0
//                var childTop: Int
//
//                var gravity1 = lp.gravity
//                if (gravity1 == -1) {
//                    gravity1 = DEFAULT_CHILD_GRAVITY
//                }
//
//                val layoutDirection = layoutDirection
//                val absoluteGravity = Gravity.getAbsoluteGravity(gravity1, layoutDirection)
//                val verticalGravity = gravity1 and Gravity.VERTICAL_GRAVITY_MASK
//
//                when (absoluteGravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
//                    Gravity.CENTER_HORIZONTAL -> childLeft = parentLeft + (parentRight - parentLeft - width) / 2 +
//                            lp.leftMargin - lp.rightMargin + shadowMarginLeft - shadowMarginRight
//                    Gravity.RIGHT -> {
//                        if (!forceLeftGravity) {
//                            childLeft = parentRight - width - lp.rightMargin - shadowMarginRight
//                        }
//                    }
//                    Gravity.LEFT -> {
//                        childLeft = parentLeft + lp.leftMargin + shadowMarginLeft
//                    }
//                    else -> childLeft = parentLeft + lp.leftMargin + shadowMarginLeft
//                }
//                when (verticalGravity) {
//                    Gravity.TOP -> childTop = parentTop + lp.topMargin + shadowMarginTop
//                    Gravity.CENTER_VERTICAL -> childTop = parentTop + (parentBottom - parentTop - height) / 2 +
//                            lp.topMargin - lp.bottomMargin + shadowMarginTop - shadowMarginBottom
//                    Gravity.BOTTOM -> childTop = parentBottom - height - lp.bottomMargin - shadowMarginBottom
//                    else -> childTop = parentTop + lp.topMargin + shadowMarginTop
//                }
//                child.layout(childLeft, childTop, childLeft + width, childTop + height)
//            }
//        }
//    }
//
//    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
//
//        canvas?.let {
//            val w = measuredWidth
//            val h = measuredHeight
//            val path = ShapeUtils.roundedRect(shadowMarginLeft.toFloat(), shadowMarginTop.toFloat(), (w - shadowMarginRight).toFloat()
//                    , (h - shadowMarginBottom).toFloat()
//                    , cornerRadiusTL
//                    , cornerRadiusTR
//                    , cornerRadiusBR
//                    , cornerRadiusBL)
//            it.drawPath(path, bgPaint)
//            canvas.clipPath(path)
//        }
//    }
//
//    override fun draw(canvas: Canvas?) {
//        super.draw(canvas)
//
//        canvas?.let {
//            canvas.save()
//            val w = measuredWidth
//            val h = measuredHeight
//            val path = ShapeUtils.roundedRect(shadowMarginLeft.toFloat(), shadowMarginTop.toFloat(), (w - shadowMarginRight).toFloat()
//                    , (h - shadowMarginBottom).toFloat()
//                    , cornerRadiusTL
//                    , cornerRadiusTR
//                    , cornerRadiusBR
//                    , cornerRadiusBL)
//            canvas.clipPath(path)
//            drawForeground(canvas)
//            canvas.restore()
//        }
//    }
//
//    private fun getShadowMarginMax() = intArrayOf(shadowMarginLeft, shadowMarginTop, shadowMarginRight, shadowMarginBottom).max()?.toFloat()
//            ?: 0f
//
//    fun drawForeground(canvas: Canvas?) {
//        foregroundDraw?.let {
//            if (foregroundDrawBoundsChanged) {
//                foregroundDrawBoundsChanged = false
//                val w = right - left
//                val h = bottom - top
//                if (foregroundDrawInPadding) {
//                    selfBounds.set(0, 0, w, h)
//                } else {
//                    selfBounds.set(paddingLeft, paddingTop,
//                            w - paddingRight, h - paddingBottom)
//                }
//                Gravity.apply(foregroundDrawGravity, it.intrinsicWidth,
//                        it.intrinsicHeight, selfBounds, overlayBounds)
//                it.bounds = overlayBounds
//            }
//            it.draw(canvas)
//        }
//    }
//
//    override fun getForeground(): Drawable? {
//        return foregroundDraw
//    }
//
//    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        super.onSizeChanged(w, h, oldw, oldh)
//        foregroundDrawBoundsChanged = true
//
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            outlineProvider = CustomOutline(w, h)
//        }
//    }
//
//    override fun getForegroundGravity(): Int {
//        return foregroundDrawGravity
//    }
//
//    override fun setForegroundGravity(foregroundGravity: Int) {
//        var foregroundGravity = foregroundGravity
//        if (foregroundDrawGravity != foregroundGravity) {
//            if (foregroundGravity and Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK == 0) {
//                foregroundGravity = foregroundGravity or Gravity.START
//            }
//            if (foregroundGravity and Gravity.VERTICAL_GRAVITY_MASK == 0) {
//                foregroundGravity = foregroundGravity or Gravity.TOP
//            }
//            foregroundDrawGravity = foregroundGravity
//            if (foregroundDrawGravity == Gravity.FILL && foregroundDraw != null) {
//                val padding = Rect()
//                foregroundDraw?.getPadding(padding)
//            }
//            requestLayout()
//        }
//    }
//
//    override fun verifyDrawable(who: Drawable): Boolean {
//        return super.verifyDrawable(who) || who === foregroundDraw
//    }
//
//    override fun jumpDrawablesToCurrentState() {
//        super.jumpDrawablesToCurrentState()
//        foregroundDraw?.let { it.jumpToCurrentState() }
//    }
//
//    override fun drawableStateChanged() {
//        super.drawableStateChanged()
//        foregroundDraw?.takeIf { it.isStateful }?.let { it.state = drawableState }
//    }
//
//    override fun setForeground(drawable: Drawable?) {
//        if (foregroundDraw != null) {
//            foregroundDraw?.callback = null
//            unscheduleDrawable(foregroundDraw)
//        }
//        foregroundDraw = drawable
//
//        updateForegroundColor()
//
//        if (drawable != null) {
//            setWillNotDraw(false)
//            drawable.callback = this
//            if (drawable.isStateful) {
//                drawable.state = drawableState
//            }
//            if (foregroundDrawGravity == Gravity.FILL) {
//                val padding = Rect()
//                drawable.getPadding(padding)
//            }
//        }
//        requestLayout()
//        invalidate()
//    }
//
//    private fun updateForegroundColor() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            (foregroundDraw as RippleDrawable?)?.setColor(ColorStateList.valueOf(foregroundColor))
//        } else {
//            foregroundDraw?.setColorFilter(foregroundColor, PorterDuff.Mode.SRC_ATOP)
//        }
//    }
//
//    override fun drawableHotspotChanged(x: Float, y: Float) {
//        super.drawableHotspotChanged(x, y)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            foregroundDraw?.let { it.setHotspot(x, y) }
//    }
//
//    fun setShadowMargin(left: Int, top: Int, right: Int, bottom: Int) {
//        shadowMarginLeft = left
//        shadowMarginTop = top
//        shadowMarginRight = right
//        shadowMarginBottom = bottom
//        requestLayout()
//        invalidate()
//    }
//
//    fun setCornerRadius(tl: Float, tr: Float, br: Float, bl: Float) {
//        cornerRadiusTL = tl
//        cornerRadiusTR = tr
//        cornerRadiusBR = br
//        cornerRadiusBL = bl
//        invalidate()
//    }
//
//    override fun generateLayoutParams(attrs: AttributeSet): LinearLayout.LayoutParams? {
//        return LinearLayout.LayoutParams(context, attrs)
//    }
//
//    override fun shouldDelayChildPressedState(): Boolean {
//        return false
//    }
//
//    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
//        return p is LayoutParams
//    }
//
//    override fun generateLayoutParams(lp: ViewGroup.LayoutParams): LinearLayout.LayoutParams {
//        return LinearLayout.LayoutParams(lp)
//    }
//
//    override fun getAccessibilityClassName(): CharSequence {
//        return FrameLayout::class.java.name
//    }
//
//    fun getPaddingLeftWithForeground(): Int {
//        return paddingLeft
//    }
//
//    fun getPaddingRightWithForeground(): Int {
//        return paddingRight
//    }
//
//    private fun getPaddingTopWithForeground(): Int {
//        return paddingTop
//    }
//
//    private fun getPaddingBottomWithForeground(): Int {
//        return paddingBottom
//    }
//
//    class LayoutParams : LinearLayout.LayoutParams {
//        var gravity2 = UNSPECIFIED_GRAVITY
//
//        constructor(c: Context, attrs: AttributeSet?) : super(c, attrs) {
//            val a = c.obtainStyledAttributes(attrs, R.styleable.ShadowView_Layout)
//            gravity2 = a.getInt(R.styleable.ShadowView_Layout_layout_gravity, UNSPECIFIED_GRAVITY)
//            a.recycle()
//        }
//
//        constructor(source: ViewGroup.LayoutParams) : super(source)
//
//        companion object {
//            val UNSPECIFIED_GRAVITY = -1
//
//
//            val TAG = CustomViewLayout::class.java.simpleName
//
//            /**
//             * Tags to identify icon position
//             */
//            const val POSITION_LEFT = 1
//            const val POSITION_RIGHT = 2
//            const val POSITION_TOP = 3
//            const val POSITION_BOTTOM = 4
//
//        }
//    }
//}

