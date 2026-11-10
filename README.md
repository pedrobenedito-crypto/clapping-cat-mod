# Clapping Cat (Minecraft 1.20.1 · Forge)

Dê **salmão cru** para um gato e ele levanta, flutua um pouquinho e fica batendo palma, igual ao meme.

## Como usar no jogo
- **Clique direito num gato com salmão cru** → vira o Gato Batendo Palma (corações + som de palma).
- **Shift + clique direito com a mão vazia** no gato que bate palma → volta a ser o gato normal (nome, dono, coleira etc. são restaurados).
- Funciona com todas as variantes de gato (preto, laranja, siamês...), porque usa as skins originais do jogo.
- Também dá pra invocar direto: `/summon clappingcat:clapping_cat`

## Build online no GitHub
1. Crie um repositório novo no GitHub e envie todo o conteúdo desta pasta (incluindo a pasta `.github`).
2. Vá na aba **Actions** → workflow **Build mod** (roda sozinho a cada push, ou clique em *Run workflow*).
3. Quando terminar, abra a execução e baixe o artifact **clapping-cat-mod**: dentro está o `.jar`.
4. Coloque o `.jar` na pasta `mods` do Minecraft 1.20.1 com Forge 47.x.

O workflow usa Java 17 + Gradle 8.1.1 (instalado pelo próprio Actions, não precisa de gradle wrapper).

## Build local (opcional)
```
gradle build
```
O jar sai em `build/libs/`. Rodar o jogo para testar: `gradle runClient`.

## Estrutura
- `entity/ClappingCatEntity` – a entidade (guarda o gato original, sincroniza a skin, toca o som da palma no cliente)
- `client/ClappingCatModel` – modelo em pé + animação de palma (mesmos UVs do gato vanilla)
- `client/ClappingCatRenderer` – flutuação e escala
- `event/CatInteractionHandler` – salmão cru transforma o gato
- `assets/clappingcat/sounds/clap.ogg` – som de palma sintetizado (troque por outro .ogg se quiser)
