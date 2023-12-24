package me.zimzaza4.geyserutils.geyser.form.element;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Value;
import lombok.experimental.Accessors;
import me.zimzaza4.geyserutils.common.form.element.NpcDialogueButton;

import java.util.List;

@Value
@Accessors( fluent = true )
public class Button {
    String text;
    List<String> commands;
    NpcDialogueButton.ButtonMode mode;
    Runnable click;
    boolean hasNextForm;

    public JsonObject toJsonObject() {
        JsonObject button = new JsonObject();
        button.addProperty( "button_name", this.text );

        JsonArray data = new JsonArray();

        for ( String command : this.commands ) {
            JsonObject cmdLine = new JsonObject();
            cmdLine.addProperty( "cmd_line", command );
            cmdLine.addProperty( "cmd_ver", 19 );

            data.add( cmdLine );
        }

        button.add( "data", data );
        button.addProperty( "mode", this.mode.ordinal() );
        button.addProperty( "text", "" );
        button.addProperty( "type", 1 );

        return button;
    }
}
