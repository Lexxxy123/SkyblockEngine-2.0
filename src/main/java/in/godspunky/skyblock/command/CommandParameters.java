package in.godspunky.skyblock.command;


import in.godspunky.skyblock.ranks.PlayerRank;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandParameters {
    String description() default "";

    String usage() default "/<command>";

    String aliases() default "";

    PlayerRank permission();
}
