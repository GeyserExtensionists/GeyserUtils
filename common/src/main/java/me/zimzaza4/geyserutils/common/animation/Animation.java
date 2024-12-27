package me.zimzaza4.geyserutils.common.animation;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Animation {

    public static final float DEFAULT_BLEND_OUT_TIME = 0.0f;
    public static final String DEFAULT_STOP_EXPRESSION = "query.any_animation_finished";
    public static final String DEFAULT_CONTROLLER = "__runtime_controller";
    public static final String DEFAULT_NEXT_STATE = "default";
    public static final int DEFAULT_STOP_EXPRESSION_VERSION = 16777216;

    private String animation;
    @Builder.Default
    private String nextState = DEFAULT_NEXT_STATE;
    @Builder.Default
    private float blendOutTime = DEFAULT_BLEND_OUT_TIME;
    @Builder.Default
    private String stopExpression = DEFAULT_STOP_EXPRESSION;
    @Builder.Default
    private String controller = DEFAULT_CONTROLLER;
    @Builder.Default
    private int stopExpressionVersion = DEFAULT_STOP_EXPRESSION_VERSION;

}