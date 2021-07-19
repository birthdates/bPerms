package com.birthdates.bperms.sponge.manager;

import com.birthdates.bperms.command.Command;
import com.birthdates.bperms.manager.CommandManager;
import com.birthdates.bperms.sponge.BPermsPlugin;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

/**
 * {@inheritDoc}
 */
public class SpongeCommandManager extends CommandManager {

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerCommand(Command command) {
        CommandSpec commandSpec = CommandSpec.builder()
                .description(Text.of(""))
                .permission(command.getPermission())
                .arguments(GenericArguments.remainingJoinedStrings(Text.of("args")))
                .executor((commandSource, commandContext) -> {
                    command.executed(commandSource, command.getName(), commandContext.<String>getOne("args").orElse("").split(" "));
                    return CommandResult.success();
                })
                .build();
        Sponge.getCommandManager().register(BPermsPlugin.getInstance(), commandSpec, ArrayUtils.addAll(command.getAliases(), command.getName()));
    }

}
