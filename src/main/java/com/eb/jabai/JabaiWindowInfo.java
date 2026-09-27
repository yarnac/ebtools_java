package com.eb.jabai;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JabaiWindowInfo(
        @JsonProperty("id") int id,
        @JsonProperty("pid") int pid,
        @JsonProperty("app") String app,
        @JsonProperty("title") String title,
        @JsonProperty("scratchpad") String scratchpad,
        @JsonProperty("frame") Frame frame,
        @JsonProperty("role") String role,
        @JsonProperty("subrole") String subrole,
        @JsonProperty("root-window") boolean rootWindow,
        @JsonProperty("display") int display,
        @JsonProperty("space") int space,
        @JsonProperty("level") int level,
        @JsonProperty("sub-level") int subLevel,
        @JsonProperty("layer") String layer,
        @JsonProperty("sub-layer") String subLayer,
        @JsonProperty("opacity") double opacity,
        @JsonProperty("split-type") String splitType,
        @JsonProperty("split-child") String splitChild,
        @JsonProperty("stack-index") int stackIndex,
        @JsonProperty("can-move") boolean canMove,
        @JsonProperty("can-resize") boolean canResize,
        @JsonProperty("has-focus") boolean hasFocus,
        @JsonProperty("has-shadow") boolean hasShadow,
        @JsonProperty("has-parent-zoom") boolean hasParentZoom,
        @JsonProperty("has-fullscreen-zoom") boolean hasFullscreenZoom,
        @JsonProperty("has-ax-reference") boolean hasAxReference,
        @JsonProperty("is-native-fullscreen") boolean isNativeFullscreen,
        @JsonProperty("is-visible") boolean isVisible,
        @JsonProperty("is-minimized") boolean isMinimized,
        @JsonProperty("is-hidden") boolean isHidden,
        @JsonProperty("is-floating") boolean isFloating,
        @JsonProperty("is-sticky") boolean isSticky,
        @JsonProperty("is-grabbed") boolean isGrabbed
) {

    /**
     * Inneres Record für den frame-Objekt-Bereich.
     * Feldnamen entsprechen exakt den JSON-Schlüsseln ('x', 'y' etc.).
     */
    public record Frame(
            double x,
            double y,
            double w,
            double h
    ) {}
}

