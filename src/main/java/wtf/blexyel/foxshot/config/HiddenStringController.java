package wtf.blexyel.foxshot.config;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.string.IStringController;

public record HiddenStringController(Option<String> option) implements IStringController<String> {
  @Override
  public Option<String> option() {
    return option;
  }

  @Override
  public String getString() {
    return option().pendingValue();
  }

  @Override
  public void setFromString(String value) {
    option().requestSet(value);
  }

  @Override
  public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
    return new HiddenStringControllerElement(this, screen, widgetDimension, true);
  }
}
