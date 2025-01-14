package me.sparklearts.capecore.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.multiplayer.PlayerInfo;

import me.sparklearts.capecore.utils.CapeManager;

import javax.annotation.Nullable;

/**
 * A Mixin class that hooks into the {@code AbstractClientPlayer} class to modify
 * behavior related to cape textures.
 * <p>
 * This class specifically allows the modification of the result of the
 * {@code getCloakTextureLocation} method to provide custom cape textures based
 * on the associated player's name and their cape availability in the {@code CapeManager}.
 */
@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {

    @Shadow
    @Nullable
    protected abstract PlayerInfo getPlayerInfo();

    /**
     * Modifies the return value of the {@code getCloakTextureLocation} method to provide
     * a custom cloak texture for players who have cape data in the {@code CapeManager}.
     *
     * @param cir A {@code CallbackInfoReturnable<ResourceLocation>} object that allows modifying
     *            the return value of the targeted method.
     */
    @Inject(method = "getCloakTextureLocation", at = @At("RETURN"), cancellable = true)
    public void getCloakTextureLocation(CallbackInfoReturnable<ResourceLocation> cir) {
        PlayerInfo playerInfo = this.getPlayerInfo();

        if (playerInfo != null) {
            String username = playerInfo.getProfile().getName();
            if (CapeManager.hasCape(username)) {
                cir.setReturnValue(CapeManager.getCape(username));
            }
        }
    }
}
