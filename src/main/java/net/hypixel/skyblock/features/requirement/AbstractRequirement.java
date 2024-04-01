package net.hypixel.skyblock.features.requirement;

import net.hypixel.skyblock.user.User;

public interface AbstractRequirement {

    boolean hasRequirement(User user);

    String getMessage();


}
