package me.zimzaza4.geyserutils.common.form;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
@Builder
public class NpcDialogueButton {

    private String text;
    private List<String> commands;
    private ButtonMode mode;
    private boolean hasNextForm;

    public enum ButtonMode {
        BUTTON_MODE,
        ON_ENTER,
        ON_EXIT
    }

}
