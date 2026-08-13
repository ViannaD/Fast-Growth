package com.example.fastgrowth.mixin;

import com.example.fastgrowth.FastGrowthMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Sempre que um bloco de plantacao (trigo, cana, bambu, etc. - veja a lista
 * em FastGrowthMod.FAST_GROWTH_BLOCKS) recebe um random tick do jogo, este
 * mixin simula ticks extras chamando o mesmo metodo de crescimento varias
 * vezes seguidas. Isso multiplica a chance de crescer naquele instante sem
 * mudar a logica de crescimento em si (luz, agua por perto, terra lavrada
 * etc. continuam sendo respeitados, pois e o mesmo codigo vanilla rodando
 * mais vezes).
 *
 * O bonus so e aplicado de dia. A noite, a plantacao volta a crescer na
 * velocidade normal do vanilla. Em dimensoes sem ciclo de dia/noite (Nether,
 * End, dimensoes customizadas sem skylight), o bonus fica sempre ativo, ja
 * que "dia" nao existe nelas.
 */
@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class FastGrowthMixin {

	@Inject(
			method = "randomTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V",
			at = @At("RETURN")
	)
	private void fastgrowth$extraGrowthTicks(ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
		BlockState state = (BlockState) (Object) this;
		Block block = state.getBlock();

		if (!FastGrowthMod.FAST_GROWTH_BLOCKS.contains(block)) {
			return;
		}

		boolean hasDayNightCycle = level.dimensionType().hasSkyLight();
		if (hasDayNightCycle && !level.isDay()) {
			// Dimensao com ciclo de dia/noite (ex.: Overworld) e agora e noite:
			// nao aplica o bonus, deixa crescer na velocidade vanilla normal.
			return;
		}

		BlockBehaviourInvokerMixin invoker = (BlockBehaviourInvokerMixin) block;
		for (int i = 0; i < FastGrowthMod.EXTRA_RANDOM_TICKS; i++) {
			invoker.fastgrowth$invokeRandomTick(state, level, pos, random);
		}
	}
}
