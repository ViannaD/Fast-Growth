package com.example.fastgrowth;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Fast Growth
 *
 * Faz TODAS as plantacoes crescerem mais rapido: trigo, cenoura, batata,
 * beterraba, cana de acucar, bambu, nether wart, cacau, abobora/melancia
 * (talos), amora silvestre, torchflower, pitcher plant, kelp, cactus, cave
 * vines, twisting/weeping vines e mudas de arvore (incluindo o propagulo do
 * mangue).
 *
 * A logica real fica no FastGrowthMixin: toda vez que o jogo faz um random
 * tick num desses blocos, o mixin chama o mesmo metodo de crescimento
 * algumas vezes extras, multiplicando a chance de avancar de estagio nesse
 * instante - sem alterar as regras de crescimento (luz, agua por perto,
 * terra lavrada, etc. continuam valendo, pois e o mesmo codigo vanilla).
 */
public class FastGrowthMod implements ModInitializer {

	public static final String MOD_ID = "fastgrowth";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	/**
	 * Quantos ticks de crescimento EXTRAS simular a cada random tick real.
	 * 0 = comportamento vanilla. 6 = crescimento em media ~7x mais rapido.
	 * Aumente para plantar e colher quase instantaneamente, ou diminua para
	 * um efeito mais sutil.
	 */
	public static final int EXTRA_RANDOM_TICKS = 6;

	/**
	 * Todos os blocos de plantacao afetados. Adicione ou remova blocos aqui
	 * conforme a sua preferencia.
	 */
	public static final Set<Block> FAST_GROWTH_BLOCKS = Set.of(
			// Plantacoes classicas
			Blocks.WHEAT,
			Blocks.CARROTS,
			Blocks.POTATOES,
			Blocks.BEETROOTS,
			Blocks.NETHER_WART,
			Blocks.TORCHFLOWER_CROP,
			Blocks.PITCHER_CROP,

			// Cana de acucar e bambu
			Blocks.SUGAR_CANE,
			Blocks.BAMBOO,
			Blocks.BAMBOO_SAPLING,

			// Cacau, abobora e melancia
			Blocks.COCOA,
			Blocks.MELON_STEM,
			Blocks.PUMPKIN_STEM,

			// Amora silvestre e cacto
			Blocks.SWEET_BERRY_BUSH,
			Blocks.CACTUS,

			// Kelp e vinhas de caverna / nether
			Blocks.KELP,
			Blocks.KELP_PLANT,
			Blocks.CAVE_VINES,
			Blocks.CAVE_VINES_PLANT,
			Blocks.TWISTING_VINES,
			Blocks.TWISTING_VINES_PLANT,
			Blocks.WEEPING_VINES,
			Blocks.WEEPING_VINES_PLANT,

			// Mudas de arvore
			Blocks.OAK_SAPLING,
			Blocks.SPRUCE_SAPLING,
			Blocks.BIRCH_SAPLING,
			Blocks.JUNGLE_SAPLING,
			Blocks.ACACIA_SAPLING,
			Blocks.DARK_OAK_SAPLING,
			Blocks.CHERRY_SAPLING,
			Blocks.MANGROVE_PROPAGULE
	);

	@Override
	public void onInitialize() {
		LOGGER.info("[Fast Growth] Acelerando o crescimento de {} tipos de plantacao ({}x mais rapido).",
				FAST_GROWTH_BLOCKS.size(), EXTRA_RANDOM_TICKS + 1);
	}
}
