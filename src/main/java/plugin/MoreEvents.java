package plugin;

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import plugin.intercepts.InterceptCycleBlockGroup;
import plugin.intercepts.InterceptUseBlockInteractions;
import plugin.intercepts.InterceptPlaceFluid;
import plugin.intercepts.InterceptRefillContainer;

import javax.annotation.Nonnull;

public class MoreEvents extends JavaPlugin {

    public MoreEvents(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        var interactionRegistry = getCodecRegistry(Interaction.CODEC);
        interactionRegistry.register("PlaceFluid", InterceptPlaceFluid.class, InterceptPlaceFluid.CODEC);
        interactionRegistry.register("UseBlock", InterceptUseBlockInteractions.class, InterceptUseBlockInteractions.CODEC);
        interactionRegistry.register("RefillContainer", InterceptRefillContainer.class, InterceptRefillContainer.CODEC);
        interactionRegistry.register("CycleBlockGroup", InterceptCycleBlockGroup.class, InterceptCycleBlockGroup.CODEC);
    }
}
