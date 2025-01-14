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

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {

    @Shadow
    @Nullable
    protected abstract PlayerInfo getPlayerInfo();

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
