package wtf.blexyel.foxshot.config;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.gui.controllers.cycling.CyclingListController;
import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.network.chat.Component;
import wtf.blexyel.foxshot.misc.FileServices;

public class YACLConfigHelper {
  public static Option<Boolean> booleanOption(
      boolean defaultValue, String name, String description, ConfigEnums option) {

    Field configField = ConfigTranslator.getField(option);

    return Option.<Boolean>createBuilder()
        .name(Component.literal(name))
        .description(OptionDescription.of(Component.literal(description)))
        .binding(
            defaultValue,
            () -> {
              try {
                return configField.getBoolean(null);
              } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
              }
            },
            newVal -> {
              try {
                configField.setBoolean(null, newVal);
              } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
              }
            })
        .controller(TickBoxControllerBuilder::create)
        .build();
  }

  public static Option<Integer> intOption(
      int defaultValue,
      String name,
      String description,
      ConfigEnums configoption,
      List<Integer> indices) {

    Field configField = ConfigTranslator.getField(configoption);

    return Option.<Integer>createBuilder()
        .name(Component.literal(name))
        .description(OptionDescription.of(Component.literal(description)))
        .binding(
            defaultValue,
            () -> {
              try {
                return configField.getInt(null);
              } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
              }
            },
            newVal -> {
              try {
                configField.setInt(null, newVal);
              } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
              }
            })
        .controller(option -> () -> new CyclingListController<>(option, indices))
        .build();
  }

  public static Option<String> stringOption(
      String defaultValue, String name, String description, ConfigEnums configoption) {
    Field configField = ConfigTranslator.getField(configoption);
    return Option.<String>createBuilder()
        .name(Component.literal(name))
        .description(OptionDescription.of(Component.literal(description)))
        .binding(
            defaultValue,
            () -> {
              try {
                return configField.get(null).toString();
              } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
              }
            },
            newVal -> {
              try {
                configField.set(null, newVal);
              } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
              }
            })
        .controller(StringControllerBuilder::create)
        .build();
  }

  public static Option<String> hiddenStringOption(
      String defaultValue, String name, String description, ConfigEnums configoption) {
    Field configField = ConfigTranslator.getField(configoption);
    return Option.<String>createBuilder()
        .name(Component.literal(name))
        .description(OptionDescription.of(Component.literal(description)))
        .binding(
            defaultValue,
            () -> {
              try {
                return configField.get(null).toString();
              } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
              }
            },
            newVal -> {
              try {
                configField.set(null, newVal);
              } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
              }
            })
        .customController(HiddenStringController::new)
        .build();
  }

  public static Option<FileServices> fileServicesOption(
      FileServices defaultValue, String name, String description, ConfigEnums configoption) {
    Field configField = ConfigTranslator.getField(configoption);
    return Option.<FileServices>createBuilder()
        .name(Component.literal(name))
        .description(OptionDescription.of(Component.literal(description)))
        .binding(
            defaultValue,
            () -> {
              try {
                return (FileServices) configField.get(null);
              } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
              }
            },
            newVal -> {
              try {
                configField.set(null, newVal);
              } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
              }
            })
        .controller(opt -> EnumControllerBuilder.create(opt).enumClass(FileServices.class))
        .build();
  }
}
