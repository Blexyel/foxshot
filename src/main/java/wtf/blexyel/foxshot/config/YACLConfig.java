package wtf.blexyel.foxshot.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import wtf.blexyel.foxshot.misc.FileServices;

public class YACLConfig {
  public static Screen create(Screen parent) {
    Config.HANDLER.load();
    FileServices initialService = Config.service;
    return YetAnotherConfigLib.createBuilder()
        .title(Component.literal("Foxshot Config"))
        // ### START TOGGLES ### //
        .category(
            ConfigCategory.createBuilder()
                .name(Component.literal("General"))
                .option(
                    YACLConfigHelper.booleanOption(
                        true, "Enabled", "Enables/Disables foxshot", ConfigEnums.ENABLED))
                .option(
                    YACLConfigHelper.booleanOption(
                        true,
                        "Auto Upload",
                        "Upload screenshots to foxbox automatically",
                        ConfigEnums.AUTO_UPLOAD))
                .option(
                    YACLConfigHelper.booleanOption(
                        true, "Toast", "Show upload toasts", ConfigEnums.TOAST))
                .option(
                    YACLConfigHelper.booleanOption(
                        true, "Message", "Show upload messages in chat", ConfigEnums.MESSAGE))
                .build())
        // ### END TOGGLES ### //
        // ### START UPLOAD CONFIG ### //
        .category(
            ConfigCategory.createBuilder()
                .name(Component.literal("Uploads"))
                .option(
                    YACLConfigHelper.hiddenStringOption(
                        "",
                        "Username",
                        "Username for selected service\nCatbox userhash goes in here",
                        ConfigEnums.USERNAME))
                .option(
                    YACLConfigHelper.hiddenStringOption(
                        "", "Token", "Token for selected service", ConfigEnums.TOKEN))
                // ### CUSTOM UPLOAD CONFIG ### //
                .group(
                    OptionGroup.createBuilder()
                        .name(Component.literal("Custom upload config"))
                        .description(
                            OptionDescription.of(
                                Component.literal(
                                    "To be used when not using any of the built-in services\n\nNote: Select service for API compatibility")))
                        .option(
                            YACLConfigHelper.fileServicesOption(
                                FileServices.CATBOX,
                                "Service",
                                "The service to be used",
                                ConfigEnums.SERVICE))
                        .option(
                            YACLConfigHelper.stringOption(
                                "https://foxbox.moe",
                                "URL",
                                "URL for foxbox",
                                ConfigEnums.CUSTOM_URL))
                        .option(
                            YACLConfigHelper.booleanOption(
                                true, "HTTPS", "enable HTTPS", ConfigEnums.HTTPS))
                        .build())
                // ### CUSTOM UPLOAD CONFIG ### //
                .build())
        // ### END UPLOAD CONFIG ### //
        /*
        () -> {
          Config.HANDLER.save();
          FileServices newService = Config.service;
          if ((initialService != FileServices.CUSTOM && newService == FileServices.CUSTOM)
              || (initialService == FileServices.CUSTOM && newService != FileServices.CUSTOM)) {
            Minecraft.getInstance().setScreen(create(parent));
          }
        }*/
        .save(Config.HANDLER::save)
        .build()
        .generateScreen(parent);
  }

  public static final List<Integer> indices =
      IntStream.range(0, getMaxIndex()).boxed().collect(Collectors.toList());

  public static int getMaxIndex() {
    int count = 0;
    for (Field field : Config.class.getDeclaredFields()) {
      if (field.isAnnotationPresent(SerialEntry.class)) {
        if (field.getName().toLowerCase().contains("index")) {
          count++;
        }
      }
    }
    return count;
  }
}
