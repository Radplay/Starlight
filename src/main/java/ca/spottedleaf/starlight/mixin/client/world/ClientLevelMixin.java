package ca.spottedleaf.starlight.mixin.client.world;

import ca.spottedleaf.starlight.common.world.ExtendedWorld;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.MultiPlayerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.Dimension;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.BiFunction;

@Mixin(MultiPlayerLevel.class)
public abstract class ClientLevelMixin extends Level implements ExtendedWorld {

    @Shadow public abstract ClientChunkCache getChunkSource();

    protected ClientLevelMixin(LevelData levelData, DimensionType dimensionType, BiFunction<Level, Dimension, ChunkSource> biFunction, ProfilerFiller profilerFiller, boolean bl) {
        super(levelData, dimensionType, biFunction, profilerFiller, bl);
    }

    @Override
    public final LevelChunk getChunkAtImmediately(final int chunkX, final int chunkZ) {
        return this.getChunkSource().getChunk(chunkX, chunkZ, false);
    }

    @Override
    public final ChunkAccess getAnyChunkImmediately(int chunkX, int chunkZ) {
        return this.getChunkSource().getChunk(chunkX, chunkZ, false);
    }
}
