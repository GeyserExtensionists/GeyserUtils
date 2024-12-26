package me.zimzaza4.geyserutils.common.packet;

import lombok.Getter;
import lombok.Setter;
import me.zimzaza4.geyserutils.common.animation.Animation;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class AnimateEntityCustomPayloadPacket extends CustomPayloadPacket {

    private String animation;
    private String nextState;
    private String stopExpression;
    private int stopExpressionVersion;
    private String controller;
    private float blendOutTime;
    private List<Integer> entityJavaIds = new ArrayList<>();

    public void parseFromAnimation(Animation animation) {
        this.animation = animation.getAnimation();
        this.nextState = animation.getNextState();
        this.blendOutTime = animation.getBlendOutTime();
        this.stopExpression = animation.getStopExpression();
        this.controller = animation.getController();
        this.stopExpressionVersion = animation.getStopExpressionVersion();
    }

}
