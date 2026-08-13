# Fast Growth (Fabric, Minecraft 1.21.11)

Mod que faz **todas as plantações crescerem mais rápido**: trigo, cenoura,
batata, beterraba, cana de açúcar, bambu, nether wart, cacau, talos de
abóbora/melancia, amora silvestre, cacto, torchflower, pitcher plant, kelp,
cave vines, twisting/weeping vines e mudas de árvore (incluindo o propágulo
do mangue).

## Como funciona

O Minecraft faz uma planta crescer através de um **"random tick"**: a cada
poucos segundos, o jogo sorteia algumas posições de bloco por chunk e chama
`randomTick` nelas. Para plantas, esse método decide (com base em luz,
umidade da terra, água por perto, etc.) se a planta avança de estágio.

Este mod injeta (via mixin) no ponto onde esse `randomTick` é despachado e,
sempre que o bloco sorteado é uma das plantações da lista, **chama o mesmo
método de crescimento várias vezes extras** naquele instante. Isso multiplica
a chance de crescimento sem alterar as regras do jogo — luz, água, terra
lavrada etc. continuam sendo exigidas normalmente, porque é literalmente o
mesmo código vanilla rodando mais vezes.

Arquivos principais:

- **`BlockBehaviourInvokerMixin`** — cria uma "ponte" pública para o método
  `randomTick` do bloco, que normalmente é `protected` e não pode ser
  chamado de fora do pacote do jogo.
- **`FastGrowthMixin`** — a cada random tick real num bloco da lista, chama
  `randomTick` mais `EXTRA_RANDOM_TICKS` vezes.
- **`FastGrowthMod`** — define a lista de blocos afetados e o multiplicador.

## Bônus só de dia

O crescimento acelerado só é aplicado enquanto é **dia** no Overworld (ou em
qualquer dimensão com ciclo dia/noite). De noite, as plantações voltam a
crescer na velocidade normal do vanilla.

Em dimensões **sem** ciclo de dia/noite (Nether, End), como "dia" não existe
nelas, o bônus fica sempre ativo.

Se preferir que o bônus valha o tempo todo (dia e noite), edite
`FastGrowthMixin.java` e remova este trecho:

```java
boolean hasDayNightCycle = level.dimensionType().hasSkyLight();
if (hasDayNightCycle && !level.isDay()) {
    return;
}
```

## Ajustando a velocidade

Em `FastGrowthMod.java`:

```java
public static final int EXTRA_RANDOM_TICKS = 1; // 1 = 2x mais rápido (o dobro)
```

- `0` → crescimento normal (vanilla).
- `1` (padrão) → 2x mais rápido (o dobro).
- `2` → ~3x mais rápido.
- `6` → ~7x mais rápido.
- `20+` → crescimento quase instantâneo.

## Adicionando/removendo plantas

Edite o `Set<Block> FAST_GROWTH_BLOCKS` em `FastGrowthMod.java`. Alguns
blocos que **não** estão na lista por padrão, mas podem ser adicionados
facilmente se quiser:

- `Blocks.VINE` (trepadeiras comuns)
- `Blocks.GLOW_LICHEN`
- `Blocks.CHORUS_FLOWER`

## Como compilar

Pré-requisitos: **JDK 21** e conexão com a internet (o Gradle/Loom baixa o
Minecraft e as mappings automaticamente). O Gradle Wrapper já está incluso.

```bash
# Linux/macOS
./gradlew build

# Windows
gradlew.bat build
```

O `.jar` compilado aparece em `build/libs/fast-growth-1.0.0.jar`.

⚠️ Confira sempre a versão mais recente do `fabric_api_version` em
`gradle.properties` (compatível com 1.21.11) em
https://modrinth.com/mod/fabric-api/versions antes de compilar, caso essa
versão específica seja descontinuada.

## Como instalar

1. Instale o [Fabric Loader](https://fabricmc.net/use/) para 1.21.11.
2. Baixe o [Fabric API](https://modrinth.com/mod/fabric-api) compatível com
   1.21.11 e coloque na pasta `mods`.
3. Coloque `fast-growth-1.0.0.jar` também na pasta `mods`.
4. Inicie o jogo/servidor normalmente.

## Estrutura do projeto

```
fast-growth/
├── build.gradle
├── gradle.properties
├── settings.gradle
├── gradlew / gradlew.bat / gradle/wrapper/
├── .github/workflows/build.yml
├── LICENSE
└── src/main/
    ├── java/com/example/fastgrowth/
    │   ├── FastGrowthMod.java              (lista de blocos + multiplicador)
    │   └── mixin/
    │       ├── BlockBehaviourInvokerMixin.java
    │       └── FastGrowthMixin.java
    └── resources/
        ├── fabric.mod.json
        └── fastgrowth.mixins.json
```
