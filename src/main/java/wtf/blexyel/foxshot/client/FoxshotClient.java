package wtf.blexyel.foxshot.client;

import java.net.URI;
import java.util.Optional;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FoxshotClient implements ClientModInitializer {

  public static final String MOD_ID = "foxshot";

  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  @Override
  public void onInitializeClient() {
    LOGGER.info("foxshot client initialized");
  }

  public static void sendUploadMessage(String message, String slug) {
    Minecraft client = Minecraft.getInstance();

    client.player.sendSystemMessage(
        Component.literal(message)
            .setStyle(
                Style.EMPTY
                    .withClickEvent(
                        new ClickEvent.Custom(
                            Identifier.parse("foxshot:upload_event"),
                            Optional.of(StringTag.valueOf(slug))))
                    .withColor(TextColor.fromRgb(0xB4BEFE))));
  }

  public static void sendMessage(String message, String url) {
    Minecraft client = Minecraft.getInstance();

    if (!url.isEmpty()) {
      client.player.sendSystemMessage(
          Component.literal(message)
              .setStyle(Style.EMPTY.withClickEvent(new ClickEvent.OpenUrl(URI.create(url))))
              .withColor(0xA6E3A1));
    } else {
      client.player.sendSystemMessage(Component.literal(message).withColor(0xF9E2AF));
    }
  }

  public static void toast(String message, boolean success) {
    Minecraft client = Minecraft.getInstance();

    if (success) {
      client
          .getToastManager()
          .addToast(
              SystemToast.multiline(
                  client,
                  CustomSystemToastId.UPLOAD_SUCCESS,
                  Component.literal("Screenshot uploaded"),
                  Component.literal(message)));
    } else {
      client
          .getToastManager()
          .addToast(
              SystemToast.multiline(
                  client,
                  CustomSystemToastId.UPLOAD_FAILURE,
                  Component.literal("Screenshot upload failed!"),
                  Component.literal(message)));
    }
  }
}
