package ca.spottedleaf.starlight.mixin.common.world;

import ca.spottedleaf.starlight.common.util.CoordinateUtils;
import ca.spottedleaf.starlight.common.world.ExtendedWorld;
import com.mojang.datafixers.util.Either;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.Dimension;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.BiFunction;

@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin extends Level implements ExtendedWorld {

    @Shadow public abstract ServerChunkCache getChunkSource();

    protected ServerWorldMixin(LevelData levelData, DimensionType dimensionType, BiFunction<Level, Dimension, ChunkSource> biFunction, ProfilerFiller profilerFiller, boolean bl) {
        super(levelData, dimensionType, biFunction, profilerFiller, bl);
    }

    @Override
    public final LevelChunk getChunkAtImmediately(final int chunkX, final int chunkZ) {
        final ChunkMap storage = this.getChunkSource().chunkMap;
        final ChunkHolder holder = storage.getVisibleChunkIfPresent(CoordinateUtils.getChunkKey(chunkX, chunkZ));

        if (holder == null) {
            return null;
        }

        final Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure> either = holder.getFutureIfPresentUnchecked(ChunkStatus.FULL).getNow(null);

        return either == null ? null : (LevelChunk)either.left().orElse(null);
    }

    @Override
    public final ChunkAccess getAnyChunkImmediately(final int chunkX, final int chunkZ) {
        final ChunkMap storage = this.getChunkSource().chunkMap;
        final ChunkHolder holder = storage.getVisibleChunkIfPresent(CoordinateUtils.getChunkKey(chunkX, chunkZ));

        return holder == null ? null : holder.getLastAvailable();
    }
}
