package cn.stars.starx.command;

import cn.stars.starx.StarX;
import cn.stars.starx.module.Module;
import cn.stars.starx.setting.Setting;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.ui.notification.NotificationType;

public final class CommandManager {

    public static Command[] COMMANDS;

    public void callCommand(final String input) {
        final String[] spit = input.split(" ");
        final String command = spit[0];
        final String args = input.substring(command.length()).trim();

        for (final Command c : COMMANDS) {
            for (final String alias : c.getCommandInfo().aliases()) {
                if (alias.equalsIgnoreCase(command)) {
                    try {
                        c.onCommand(args, args.split(" "));
                    } catch (final Exception e) {
                        e.printStackTrace();
                        StarX.INSTANCE.getNotificationManager().registerNotification(
                                "Invalid command usage \"" + c.getCommandInfo().syntax() + "\"."
                                , "Command", NotificationType.ERROR);
                        StarX.INSTANCE.showMsg("Invalid command usage \"" + c.getCommandInfo().syntax() + "\".");
                    }

                    return;
                }
            }
        }

        for (final Module module : StarX.INSTANCE.getModuleManager().getModuleList()) {
            if (module.getModuleInfo().name().equalsIgnoreCase(command)) {
                if (spit.length > 1) {

                    if (module.getSettingAlternative(spit[1]) != null) {
                        final Setting setting = module.getSettingAlternative(spit[1]);

                        try {
                            try {
                                if (setting instanceof BoolValue) {
                                    ((BoolValue) setting).setEnabled(Boolean.parseBoolean(spit[2]));
                                } else if (setting instanceof NumberValue) {
                                    ((NumberValue) setting).setValue(Double.parseDouble(spit[2]));
                                } else if (setting instanceof ModeValue) {
                                    ((ModeValue) setting).set(spit[2]);
                                }


                            } catch (final NumberFormatException ignored) {
                                StarX.INSTANCE.getNotificationManager().registerNotification("Settings name error.Dont type space! (eg. Rotation Mode -> RotationMode)", "Command", NotificationType.ERROR);
                                StarX.INSTANCE.showMsg("Settings name error.Dont type space! (eg. Rotation Mode -> RotationMode)");
                                return;
                            }
                        } catch (final ArrayIndexOutOfBoundsException ignored) {
                            StarX.INSTANCE.getNotificationManager().registerNotification("Settings name error.Dont type space! (eg. Rotation Mode -> RotationMode)", "Command", NotificationType.ERROR);
                            StarX.INSTANCE.showMsg("Settings name error.Dont type space! (eg. Rotation Mode -> RotationMode)");
                        }

                        return;
                    }

                    StarX.INSTANCE.getNotificationManager().registerNotification("Settings " + spit[1].toLowerCase() + " in " + command.toLowerCase() + " doesn't exist!", "Command", NotificationType.ERROR);
                    StarX.INSTANCE.showMsg("Settings \" + spit[1].toLowerCase() + \" in \" + command.toLowerCase() + \" doesn't exist!");
                    return;
                }
            }
        }

        StarX.INSTANCE.getNotificationManager().registerNotification("Module or command " + command.toLowerCase() + " doesn't exist.", "Command", NotificationType.ERROR);
        StarX.INSTANCE.showMsg("Module or command " + command.toLowerCase() + " doesn't exist.");
    }
}
