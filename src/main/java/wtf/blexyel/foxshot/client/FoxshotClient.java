package wtf.blexyel.foxshot.client;

import java.net.URI;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wtf.blexyel.foxshot.config.Config;

public class FoxshotClient implements ClientModInitializer {

  public static final String MOD_ID = "foxshot";

  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  @Override
  public void onInitializeClient() {
    LOGGER.info("foxshot initialized");
    Config.HANDLER.load();
    // Register the test CustomUploadHandler request builder thingi.
    // CustomUploadHandler.register(new TestUploadHandler());
  }

  public static void sendUploadMessage(String message, String slug) {
    Minecraft client = Minecraft.getInstance();
    if (client.player == null) return;

    client.player.displayClientMessage(
        Component.literal(message)
            .setStyle(
                Style.EMPTY
                    .withClickEvent(new ClickEvent.RunCommand("foxshot:upload_event;" + slug))
                    .withColor(TextColor.fromRgb(0xB4BEFE))),
        false);

    /*
               false,
               Optional.of(StringTag.valueOf(slug))))
    */
  }

  public static void sendMessage(String message, String url) {
    Minecraft client = Minecraft.getInstance();
    if (client.player == null) return;

    if (!url.isEmpty()) {
      client.player.displayClientMessage(
          Component.literal(message)
              .setStyle(Style.EMPTY.withClickEvent(new ClickEvent.OpenUrl(URI.create(url))))
              .withColor(0xA6E3A1),
          false);
    } else {
      client.player.displayClientMessage(Component.literal(message).withColor(0xF9E2AF), false);
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
