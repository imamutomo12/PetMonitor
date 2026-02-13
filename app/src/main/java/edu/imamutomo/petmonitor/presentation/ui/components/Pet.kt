package edu.imamutomo.petmonitor.presentation.ui.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import androidx.core.content.ContextCompat
import edu.imamutomo.petmonitor.R
import javax.inject.Inject
import javax.inject.Singleton
// Flyweight Interface
interface PetTypeIcon {
    fun display(context: Context, size: IconSize): Drawable
    fun getTypeName(): String
}

enum class IconSize {
    SMALL, MEDIUM, LARGE
}

// Concrete Flyweight
class SharedPetTypeIcon(
    private val typeName: String,
    private val iconResId: Int,
    private val intrinsicColor: Int
) : PetTypeIcon {
    // Intrinsic state (shared)
    private var cachedDrawable: Drawable? = null

    override fun display(context: Context, size: IconSize): Drawable {
        // Extrinsic state (size) passed as parameter
        return cachedDrawable?.mutate()?.apply {
            setBounds(0, 0, size.getPixels(context), size.getPixels(context))
        } ?: run {
            ContextCompat.getDrawable(context, iconResId)?.apply {
                setTint(intrinsicColor)
                cachedDrawable = this.constantState?.newDrawable()
                setBounds(0, 0, size.getPixels(context), size.getPixels(context))
            } ?: throw IllegalStateException("Drawable not found for $typeName")
        }
    }

    override fun getTypeName(): String = typeName
}

private fun IconSize.getPixels(context: Context): Int {
    val density = context.resources.displayMetrics.density
    return when (this) {
        IconSize.SMALL -> (24 * density).toInt()
        IconSize.MEDIUM -> (48 * density).toInt()
        IconSize.LARGE -> (96 * density).toInt()
    }
}

// Flyweight Factory
@Singleton
class PetTypeIconFactory @Inject constructor() {
    private val icons = mutableMapOf<String, SharedPetTypeIcon>()

    init {
        // Initialize with common pet types (intrinsic state)
        registerIcon("dog", R.drawable.ic_launcher_foreground, R.color.brown)
        registerIcon("cat", R.drawable.ic_launcher_foreground, R.color.orange)
        registerIcon("bird", R.drawable.ic_launcher_foreground, R.color.blue)
        registerIcon("fish", R.drawable.ic_launcher_foreground, R.color.cyan)
        registerIcon("rabbit", R.drawable.ic_launcher_foreground, R.color.gray)
        registerIcon("hamster", R.drawable.ic_launcher_foreground, R.color.gold)
        registerIcon("reptile", R.drawable.ic_launcher_foreground, R.color.green)
        registerIcon("other", R.drawable.ic_launcher_foreground, R.color.purple)
    }

    private fun registerIcon(type: String, iconRes: Int, colorRes: Int) {
        icons[type.lowercase()] = SharedPetTypeIcon(type, iconRes, colorRes)
    }

    fun getIcon(petType: String): PetTypeIcon {
        return icons[petType.lowercase()] ?: icons["other"]!!
    }

    fun getIconCount(): Int = icons.size

    // For memory monitoring
    fun getMemoryStats(): String {
        return "Cached icons: ${icons.size}, Estimated memory: ${icons.size * 2}KB"
    }
}

// Context for using flyweight
class PetIconRenderer @Inject constructor(
    private val iconFactory: PetTypeIconFactory
) {
    fun renderPetIcon(
        context: Context,
        petType: String,
        size: IconSize,
        overlay: Drawable? = null
    ): Drawable {
        val icon = iconFactory.getIcon(petType)
        val baseDrawable = icon.display(context, size)

        return overlay?.let {
            LayerDrawable(arrayOf(baseDrawable, it)).apply {
                setBounds(0, 0, size.getPixels(context), size.getPixels(context))
            }
        } ?: baseDrawable
    }
}