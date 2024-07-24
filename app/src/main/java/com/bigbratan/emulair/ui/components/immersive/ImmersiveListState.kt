package com.bigbratan.emulair.ui.components.immersive

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FloatSpringSpec
import androidx.compose.animation.core.Spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Stable
interface ImmersiveListState {
    val listOffset: Float
    val firstVisibleItem: Int
    val lastVisibleItem: Int
    val selectedItem: Int

    suspend fun snapTo(value: Float)
    suspend fun decayTo(velocity: Float, value: Float)
    suspend fun stop()
    fun offsetFor(itemIndex: Int): IntOffset
    fun setup(config: ImmersiveListConfig, density: Density)
}

class ImmersiveListStateImpl(
    currentOffset: Float = 0f,
) : ImmersiveListState {
    /** The configuration of the list. Holds the most important values. */
    private var config = ImmersiveListConfig()

    /** Used to animate the horizontal/vertical `offset` of the list. */
    private val animatable = Animatable(currentOffset)

    /** The width of an item. Calculated by diving `contentWidth` by `visibleItems`. */
    private var itemWidth = 0f

    /** The animation spec used for the decay animation.
     *
     * The decay animation makes scroll stopping smoother. */
    private val decayAnimationSpec = FloatSpringSpec(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessLow,
    )

    /** The minimum amount of pixels to be scrolled so that the next item is selected.
     *
     * If you scroll an amount of pixels less than the width of an item, the list snaps back to the current item. */
    private val minOffset: Float
        get() = -(config.numItems - 1) * (itemWidth + config.itemSpacing)

    /** The current horizontal/vertical offset of the list. */
    override val listOffset: Float
        get() = animatable.value

    /** The first visible (on the screen) item of the list. */
    override val firstVisibleItem: Int
        get() = ((-listOffset - config.selectedItemOffset) / (itemWidth + config.itemSpacing)).toInt()
            .coerceAtLeast(0)

    /** The last visible (on the screen) item of the list. */
    override val lastVisibleItem: Int
        get() = (((-listOffset - config.selectedItemOffset) / (itemWidth + config.itemSpacing)).toInt() + config.visibleItems)
            .coerceAtMost(config.numItems - 1)

    /** The currently selected item of the list.
     *
     * Calculations were done to be compatible with `minOffset`.
     * In other words, you need to scroll an amount of pixels equal to the width of an entire item so that the counter is increased/decreased. */
    override val selectedItem: Int
        get() = (((-listOffset * 100) / ((itemWidth + config.itemSpacing)) + 50) / 100).toInt() // TODO: optimize calculation
            .coerceAtLeast(0)
            .coerceAtMost(config.numItems - 1)

    /** Snaps the list to the given value. */
    override suspend fun snapTo(value: Float) {
        val minOvershoot =
            -(config.numItems - 1 + config.overshootItems) * (itemWidth + config.itemSpacing)
        val maxOvershoot = config.overshootItems * (itemWidth + config.itemSpacing)

        animatable.snapTo(value.coerceIn(minOvershoot, maxOvershoot))
    }

    /** Decays the list to the given value. */
    override suspend fun decayTo(velocity: Float, value: Float) {
        val constrainedValue = value.coerceIn(minOffset, 0f).absoluteValue
        val remainder =
            (constrainedValue / (itemWidth + config.itemSpacing)) - (constrainedValue / (itemWidth + config.itemSpacing)).toInt()
        val extra = if (remainder <= 0.5f) 0 else 1
        val target =
            ((constrainedValue / (itemWidth + config.itemSpacing)).toInt() + extra) * (itemWidth + config.itemSpacing)

        animatable.animateTo(
            targetValue = -target,
            initialVelocity = velocity,
            animationSpec = decayAnimationSpec,
        )
    }

    /** Stops any animations. */
    override suspend fun stop() {
        animatable.stop()
    }

    /** Sets up the list with the given configuration. */
    override fun setup(config: ImmersiveListConfig, density: Density) {
        this.config = config
        itemWidth = config.contentWidth / config.visibleItems
    }

    /** Calculates the offset for the given index. */
    override fun offsetFor(itemIndex: Int): IntOffset {
        val x =
            (listOffset + config.selectedItemOffset + itemIndex * (itemWidth + config.itemSpacing))

        return IntOffset(
            x = x.roundToInt(),
            y = 0,
        )
    }

    /** Checks if the given `ImmersiveListStateImpl` is equal to this `ImmersiveListStateImpl`. */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImmersiveListStateImpl

        if (animatable.value != other.animatable.value) return false
        if (itemWidth != other.itemWidth) return false
        if (config != other.config) return false
        if (decayAnimationSpec != other.decayAnimationSpec) return false

        return true
    }

    /** Calculates the hash code of the `ImmersiveListStateImpl`. */
    override fun hashCode(): Int {
        var result = animatable.value.hashCode()

        result = 31 * result + itemWidth.hashCode()
        result = 31 * result + config.hashCode()
        result = 31 * result + decayAnimationSpec.hashCode()

        return result
    }

    /** The companion object that holds the `Saver` for the `ImmersiveListStateImpl`. */
    companion object {
        val Saver = Saver<ImmersiveListStateImpl, List<Any>>(
            save = { listOf(it.listOffset) },
            restore = {
                ImmersiveListStateImpl(it[0] as Float)
            }
        )
    }
}

@Composable
fun rememberImmersiveListState(): ImmersiveListState {
    val state = rememberSaveable(saver = ImmersiveListStateImpl.Saver) {
        ImmersiveListStateImpl()
    }

    return state
}