package xyz.cahbyben.simplerkeybinds.mixin;

import xyz.cahbyben.simplerkeybinds.RadialMenuFire;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyMapping.class)
public class KeyMappingMixin {

    @Inject(at = @At("HEAD"), method = "consumeClick", cancellable = true)
    private void onConsumeClick(CallbackInfoReturnable<Boolean> cir) {
        KeyMapping self = (KeyMapping) (Object) this;
        if (RadialMenuFire.consume(self)) {
            cir.setReturnValue(true);
        }
    }
}
