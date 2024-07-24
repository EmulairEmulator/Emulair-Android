package com.bigbratan.emulair.ui.components.immersive

import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.gestures.verticalDrag
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun ImmersiveList(
    modifier: Modifier = Modifier,
    state: ImmersiveListState = rememberImmersiveListState(),
    orientation: Orientation,
    visibleItems: Int, // TODO: make it a float/double
    overshootItems: Int = 0, // TODO: remove it, or make it a float/double if you keep it
    selectedItemOffset: Dp = 0.dp,
    itemSpacing: Dp = 0.dp,
    // onItemClick: () -> Unit,
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
            // .dragAndClick(state, orientation, onItemClick),
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

private fun Modifier.drag(
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
                        if (change.positionChange() != Offset.Zero) { change.consume() }
                    }

                    velocity = tracker.calculateVelocity().y
                    targetValue = decay.calculateTargetValue(state.listOffset, velocity)
                } else if (orientation == Orientation.Horizontal) {
                    horizontalDrag(pointerId) { change ->
                        val horizontalDragOffset = state.listOffset + change.positionChange().x

                        launch { state.snapTo(horizontalDragOffset) }
                        tracker.addPosition(change.uptimeMillis, change.position)
                        if (change.positionChange() != Offset.Zero) { change.consume() }
                    }

                    velocity = tracker.calculateVelocity().x
                    targetValue = decay.calculateTargetValue(state.listOffset, velocity)
                }
            }

            launch { state.decayTo(velocity, targetValue) }
        }
    }
}

private fun Modifier.dragAndClick(
    state: ImmersiveListState,
    orientation: Orientation,
    onItemClick: () -> Unit,
) = pointerInput(Unit) {
    val decay = splineBasedDecay<Float>(this)

    coroutineScope {
        while (true) {
            var velocity = 0f
            var targetValue = 0f
            var isDragging = false

            awaitPointerEventScope {
                val pointerId = awaitFirstDown().id

                launch { state.stop() }

                val tracker = VelocityTracker()

                if (orientation == Orientation.Vertical) {
                    verticalDrag(pointerId) { change ->
                        val verticalDragOffset = state.listOffset + change.positionChange().y

                        launch { state.snapTo(verticalDragOffset) }
                        tracker.addPosition(change.uptimeMillis, change.position)
                        if (change.positionChange() != Offset.Zero) {
                            isDragging = true
                            change.consume()
                        }
                    }

                    velocity = tracker.calculateVelocity().y
                    targetValue = decay.calculateTargetValue(state.listOffset, velocity)
                } else if (orientation == Orientation.Horizontal) {
                    horizontalDrag(pointerId) { change ->
                        val horizontalDragOffset = state.listOffset + change.positionChange().x

                        launch { state.snapTo(horizontalDragOffset) }
                        tracker.addPosition(change.uptimeMillis, change.position)
                        if (change.positionChange() != Offset.Zero) {
                            isDragging = true
                            change.consume()
                        }
                    }

                    velocity = tracker.calculateVelocity().x
                    targetValue = decay.calculateTargetValue(state.listOffset, velocity)
                }
            }

            if (!isDragging) { onItemClick() }

            launch { state.decayTo(velocity, targetValue) }
        }
    }
}

/*
private fun Modifier.itemClick(
    state: ImmersiveListState,
    itemWidth: Int,
    onClick: (Int) -> Unit
) = this.then(
    Modifier.pointerInput(Unit) {
        detectTapGestures(
            onPress = { offset ->
                val index = (offset.x / itemWidth).toInt()

                if (index in state.firstVisibleItem..state.lastVisibleItem) {
                    val success = tryAwaitRelease()

                    if (success) {
                        coroutineScope {
                            launch { onClick(index) }
                        }
                    }
                }
                false
            }
        )
    }
)*/
