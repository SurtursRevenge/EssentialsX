package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.User;
import org.bukkit.Server;

import static com.earth2me.essentials.I18n.tl;


public class Commandafk extends EssentialsCommand {
    public Commandafk() {
        super("afk");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length > 0 && user.isAuthorized("essentials.afk.others")) {
            User afkUser = user; // if no player found, but message specified, set command executor to target user
            String message;
            try {
                afkUser = getPlayer(server, user, args, 0);
                message = args.length > 1 ? getFinalArg(args, 1) : null;
            } catch (PlayerNotFoundException e) {
                // If only one arg is passed, assume the command executor is targeting another player.
                if (args.length == 1) {
                    throw e;
                }
                message = getFinalArg(args, 0);
            }
            toggleAfk(afkUser, message);
        } else {
            String message = args.length > 0 ? getFinalArg(args, 0) : null;
            toggleAfk(user, message);
        }
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length > 0) {
            User afkUser = getPlayer(server, args, 0, true, false);
            String message = args.length > 1 ? getFinalArg(args, 1) : null;
            toggleAfk(afkUser, message);
        } else {
            throw new NotEnoughArgumentsException();
        }
    }

    private void toggleAfk(User user, String message) {
        user.setDisplayNick();
        String msg = "";
        if (!user.toggleAfk()) {
            //user.sendMessage(_("markedAsNotAway"));
            if (!user.isHidden()) {
                msg = tl("userIsNotAway", user.getDisplayName());
            }
            user.updateActivity(false);
        } else {
            //user.sendMessage(_("markedAsAway"));
            if (!user.isHidden()) {
                if (message != null) {
                    msg = tl("userIsAwayWithMessage", user.getDisplayName(), message);
                } else {
                    msg = tl("userIsAway", user.getDisplayName());
                }
            }
            user.setAfkMessage(message);
        }
        if (!msg.isEmpty()) {
            ess.broadcastMessage(user, msg);
        }
    }
}

