package io.github.treesoid.nations.mixin;

import io.github.treesoid.nations.server.NationsServer;
import io.github.treesoid.nations.storage.PlayerData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "afterBreak", at = @At("TAIL"))
    private void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack, CallbackInfo ci) {
        if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, stack) == 0) {
            PlayerData data = NationsServer.DATABASE_HANDLER.getOrCreatePlayerData(player.getUuid(), player.getServer());
            data.addResourcePoints(NationsServer.getResourcePointsForBlock(state.getBlock(), EnchantmentHelper.getLevel(Enchantments.FORTUNE, stack)));
        }
    }
}
