package wtf.blexyel.foxshot.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.blexyel.foxshot.network.UploadHandler;

@Mixin(Screen.class)
public class ScreenMixin {
  @Inject(method = "handleComponentClicked", at = @At("HEAD"), cancellable = true)
  private static void handleComponentClicked(Style style, CallbackInfoReturnable<Boolean> ci) {
    if (style.getClickEvent() instanceof ClickEvent.RunCommand(String id)) {
      Minecraft minecraft = Minecraft.getInstance();
      String[] payload = id.split(";");
      if (payload[0].equals("foxshot:upload_event")) {
        if (minecraft.player != null)
          minecraft.execute(
              () ->
                  minecraft.player.displayClientMessage(Component.literal("Uploading...."), false));
        Thread.startVirtualThread(() -> UploadHandler.upload(payload[1]));
        ci.cancel();
      }
    }
  }
}
