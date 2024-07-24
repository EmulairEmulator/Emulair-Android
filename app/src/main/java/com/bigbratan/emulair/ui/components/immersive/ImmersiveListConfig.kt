package com.bigbratan.emulair.ui.components.immersive

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