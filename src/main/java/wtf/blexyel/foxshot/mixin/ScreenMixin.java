package wtf.blexyel.foxshot.mixin;

import static wtf.blexyel.foxshot.client.FoxshotClient.LOGGER;

import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.ClickEvent.Custom;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.blexyel.foxshot.util.UploadHandler;

@Mixin(Screen.class)
public class ScreenMixin {
  @Inject(method = "defaultHandleGameClickEvent", at = @At("HEAD"), cancellable = true)
  private static void defaultHandleGameClickEvent(
      ClickEvent event, Minecraft minecraft, Screen activeScreen, CallbackInfo ci) {
    if (event instanceof Custom(Identifier id, Optional<Tag> payload)) {
      if (id.equals(Identifier.parse("foxshot:upload_event"))) {
        LOGGER.info("should be correct now, idfk {} {}", id, payload.get());

        Thread.startVirtualThread(() -> UploadHandler.upload(payload.get().toString()));
        ci.cancel();
      }
    }
  }
}
