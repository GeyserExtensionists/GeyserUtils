package me.zimzaza4.geyserutils.common.camera.data;

import lombok.Getter;

@Getter
public enum EaseType {
    LINEAR("linear", 0),
    SPRING("spring", 1),
    EASE_IN_SINE("in_sine", 2),
    EASE_OUT_SINE("out_sine", 3),
    EASE_IN_OUT_SINE("in_out_sine", 4),
    EASE_IN_QUAD("in_quad", 5),
    EASE_OUT_QUAD("out_quad", 6),
    EASE_IN_OUT_QUAD("in_out_quad", 7),
    EASE_IN_CUBIC("in_cubic", 8),
    EASE_OUT_CUBIC("out_cubic", 9),
    EASE_IN_OUT_CUBIC("in_out_cubic", 10),
    EASE_IN_QUART("in_quart", 11),
    EASE_OUT_QUART("out_quart", 12),
    EASE_IN_OUT_QUART("in_out_quart", 13),
    EASE_IN_QUINT("in_quint", 14),
    EASE_OUT_QUINT("out_quint", 15),
    EASE_IN_OUT_QUINT("in_out_quint", 16),
    EASE_IN_EXPO("in_expo", 17),
    EASE_OUT_EXPO("out_expo", 18),
    EASE_IN_OUT_EXPO("in_out_expo", 19),
    EASE_IN_CIRC("in_circ", 20),
    EASE_OUT_CIRC("out_circ", 21),
    EASE_IN_OUT_CIRC("in_out_circ", 22),
    EASE_IN_BACK("in_back", 23),
    EASE_OUT_BACK("out_back", 24),
    EASE_IN_OUT_BACK("in_out_back", 25),
    EASE_IN_ELASTIC("in_elastic", 26),
    EASE_OUT_ELASTIC("out_elastic", 27),
    EASE_IN_OUT_ELASTIC("in_out_elastic", 28),
    EASE_IN_BOUNCE("in_bounce", 29),
    EASE_OUT_BOUNCE("out_bounce", 30),
    EASE_IN_OUT_BOUNCE("in_out_bounce", 31);


    private final String type;
    private final int index;

    EaseType(String type, int index) {
        this.type = type;
        this.index = index;
    }

}