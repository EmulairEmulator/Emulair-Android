package com.bigbratan.emulair.ui.components

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FloatSpringSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.clipScrollableContainer
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.verticalDrag
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.overscroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

data class ImmersiveListConfig(
    /** The width occupied by the custom `Layout`. */
    val contentWidth: Float = 0f,
    /** The total number of items in the list. */
    val numItems: Int = 0,
    /** The number of items visible on the screen. */
    val visibleItems: Int = 0,
    /** The number of items that can be scrolled past the first and last visible items. */
    val overshootItems: Int = 0,
    /** The initial offset of the list, and at the same time, the offset of the currently selected item (because said item is always at the beginning).
     *
     * It works similarly to how `ContentPadding(start = x.dp)` works in a regular `Row`.
     * Only start content padding is needed because there already is enough end content padding by default.
     * The default end content padding exists because the last item is allowed to be the only one visible on the screen, unlike regular `Row`s which implement clipping. */
    val selectedItemOffset: Float = 0f,
    /** The blank space between items. It is added everywhere where `itemWidth` is also present. */
    val itemSpacing: Float = 0f,
)

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

@Composable
fun ImmersiveList(
    modifier: Modifier = Modifier,
    state: ImmersiveListState = rememberImmersiveListState(),
    orientation: Orientation,
    visibleItems: Int, // TODO: make it a float/double
    overshootItems: Int = 0, // TODO: remove it, or make it a float/double if you keep it
    selectedItemOffset: Dp = 0.dp,
    itemSpacing: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    val localDensity = LocalDensity.current

    check(visibleItems > 0) { "Number of visible items must be positive" }
    check(overshootItems >= 0) { "Number of overshoot items must be non-negative" }
    check(selectedItemOffset >= 0.dp) { "Selected item offset must be non-negative" }
    check(itemSpacing >= 0.dp) { "Item spacing must be non-negative" }

    Layout(
        modifier = modifier
            .clipToBounds()
            .drag(state, orientation),
        content = content,
    ) { measurables, constraints ->
        val itemWidth = constraints.maxWidth / visibleItems
        val itemConstraints = Constraints.fixed(width = itemWidth, height = itemWidth)
        val placeables = measurables.map { measurable -> measurable.measure(itemConstraints) }

        state.setup(
            ImmersiveListConfig(
                contentWidth = constraints.maxWidth.toFloat(),
                numItems = placeables.size,
                visibleItems = visibleItems,
                overshootItems = overshootItems,
                selectedItemOffset = selectedItemOffset.toPx(),
                itemSpacing = itemSpacing.toPx(),
            ),
            localDensity,
        )

        layout(
            width = constraints.maxWidth,
            height = itemWidth,
        ) {
            for (i in state.firstVisibleItem..state.lastVisibleItem) {
                placeables[i].placeRelative(state.offsetFor(i))
            }
        }
    }
}

fun Modifier.drag(
    state: ImmersiveListState,
    orientation: Orientation,
) = pointerInput(Unit) {
    val decay = splineBasedDecay<Float>(this)

    coroutineScope {
        while (true) {
            var velocity = 0f
            var targetValue = 0f

            awaitPointerEventScope {
                val pointerId = awaitFirstDown().id

                launch { state.stop() }

                val tracker = VelocityTracker()

                if (orientation == Orientation.Vertical) {
                    verticalDrag(pointerId) { change ->
                        val verticalDragOffset = state.listOffset + change.positionChange().y

                        launch { state.snapTo(verticalDragOffset) }
                        tracker.addPosition(change.uptimeMillis, change.position)
                        if (change.positionChange() != Offset.Zero) change.consume()
                    }

                    velocity = tracker.calculateVelocity().y
                    targetValue = decay.calculateTargetValue(state.listOffset, velocity)
                } else if (orientation == Orientation.Horizontal) {
                    horizontalDrag(pointerId) { change ->
                        val horizontalDragOffset = state.listOffset + change.positionChange().x

                        launch { state.snapTo(horizontalDragOffset) }
                        tracker.addPosition(change.uptimeMillis, change.position)
                        if (change.positionChange() != Offset.Zero) change.consume()
                    }

                    velocity = tracker.calculateVelocity().x
                    targetValue = decay.calculateTargetValue(state.listOffset, velocity)
                }
            }

            launch { state.decayTo(velocity, targetValue) }
        }
    }
}

@Composable
fun ImmersiveRow(
    modifier: Modifier = Modifier,
    state: ImmersiveListState = rememberImmersiveListState(),
    visibleItems: Int,
    selectedItemOffset: Dp,
    itemSpacing: Dp,
    content: @Composable () -> Unit,
) {
    ImmersiveList(
        modifier = modifier,
        state = state,
        orientation = Orientation.Horizontal,
        visibleItems = visibleItems,
        selectedItemOffset = selectedItemOffset,
        itemSpacing = itemSpacing,
        content = content,
    )
}

@Composable
fun ImmersiveColumn(
    modifier: Modifier = Modifier,
    state: ImmersiveListState = rememberImmersiveListState(),
    visibleItems: Int,
    selectedItemOffset: Dp,
    itemSpacing: Dp,
    content: @Composable () -> Unit,
) {
    ImmersiveList(
        modifier = modifier,
        state = state,
        orientation = Orientation.Vertical,
        visibleItems = visibleItems,
        selectedItemOffset = selectedItemOffset,
        itemSpacing = itemSpacing,
        content = content,
    )
}