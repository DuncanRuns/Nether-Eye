package me.duncanruns.nethereye.mixin;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin {
    @Inject(method = "locateStructure", at = @At("HEAD"), cancellable = true)
    private void locateNetherStrongholdMixin(ServerWorld world, StructureFeature<?> feature, BlockPos center, int radius, boolean skipExistingChunks, CallbackInfoReturnable<@Nullable BlockPos> info) {
        if (world.getRegistryKey().equals(ServerWorld.NETHER) && feature == StructureFeature.STRONGHOLD) {
            BlockPos overworldCenter = new BlockPos(center.getX() * 8, center.getY() * 8, center.getZ() * 8);
            ServerWorld overworld = world.getServer().getOverworld();
            BlockPos strongholdPos = overworld.getChunkManager().getChunkGenerator().locateStructure(overworld, feature, overworldCenter, radius, skipExistingChunks);
            if(strongholdPos != null) {
                info.setReturnValue(new BlockPos(strongholdPos.getX() / 8, strongholdPos.getY() / 8, strongholdPos.getZ() / 8));
            }
        }
    }
}
