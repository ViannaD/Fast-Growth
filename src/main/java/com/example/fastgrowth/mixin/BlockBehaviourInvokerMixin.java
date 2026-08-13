package com.example.fastgrowth.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * `Block#randomTick(BlockState, ServerLevel, BlockPos, RandomSource)` (herdado
 * de BlockBehaviour) e "protected", entao nao pode ser chamado diretamente de
 * fora do pacote. Este mixin de "Invoker" cria uma ponte publica para ele,
 * usada pelo FastGrowthMixin para simular ticks aleatorios extras.
 */
@Mixin(BlockBehaviour.class)
public interface BlockBehaviourInvokerMixin {

	@Invoker("randomTick")
	void fastgrowth$invokeRandomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random);
}
