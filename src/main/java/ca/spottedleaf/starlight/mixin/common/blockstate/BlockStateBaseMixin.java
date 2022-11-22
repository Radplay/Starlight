package ca.spottedleaf.starlight.mixin.common.blockstate;

import ca.spottedleaf.starlight.common.blockstate.ExtendedAbstractBlockState;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.AbstractStateHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockState.class)
public abstract class BlockStateBaseMixin extends AbstractStateHolder<Block, BlockState> implements StateHolder<BlockState>, ExtendedAbstractBlockState {

    @Shadow
    @Final
    private boolean useShapeForLightOcclusion;

    @Shadow
    private BlockState.Cache cache;

    @Unique
    private int opacityIfCached;

    @Unique
    private boolean isConditionallyFullOpaque;

    protected BlockStateBaseMixin(final Block object, ImmutableMap<Property<?>, Comparable<?>> immutableMap) {
        super(object, immutableMap);
    }

    /**
     * Initialises our light state for this block.
     */
    @Inject(
            method = "initCache",
            at = @At("RETURN")
    )
    public void initLightAccessState(final CallbackInfo ci) {
        this.isConditionallyFullOpaque = this.useShapeForLightOcclusion;
        this.opacityIfCached = this.cache == null || this.isConditionallyFullOpaque ? -1 : this.cache.lightBlock;
    }

    public final boolean isConditionallyFullOpaque() {
        return this.isConditionallyFullOpaque;
    }

    public final int getOpacityIfCached() {
        return this.opacityIfCached;
    }
}
