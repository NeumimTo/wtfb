package cz.neumimto.wtfshouldibuildnext.whatthefuckshouldibuildnext;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import ninja.leaping.configurate.SimpleConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.dispatcher.SimpleDispatcher;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.spongepowered.api.text.TextTemplate.arg;

@Plugin(
        id = "wtfb",
        name = "wtfb",
        authors = "NeumimTo",
        version = "1.0.0",
        description = "Made for bored builders on Darwin Reforged"
)
public class Whatthefuckshouldibuildnext {

    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path root;


    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    private RootConfig rootConfig;

    private SecureRandom secureRandom = new SecureRandom();

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        File file = new File(root.toFile(), "things.conf");
        if (!file.exists()) {
            logger.info("Generating default config ...");
            Category architecture = new Category();
            architecture.setName("Architecture");
            architecture.getThemes().add("Baroque");
            architecture.getThemes().add("Rococo");
            architecture.getThemes().add("Renaissance");
            architecture.getThemes().add("Roman");
            architecture.getThemes().add("Gothic");
            architecture.getThemes().add("Post-apo");
            architecture.getThemes().add("Ruined");
            architecture.getThemes().add("Steampunk");
            architecture.getThemes().add("Elven");
            architecture.getThemes().add("Dwarven");

            architecture.getThings().add("Castle");
            architecture.getThings().add("Village");
            architecture.getThings().add("Factory");
            architecture.getThings().add("Ship");
            architecture.getThings().add("Spaceship");
            architecture.getThings().add("Market square");
            architecture.getThings().add("Fortress");
            architecture.getThings().add("Forum (plaza)");
            architecture.getThings().add("Castle");
            architecture.getThings().add("Statue");
            architecture.getThings().add("Mansion");
            architecture.getThings().add("Theatre");
            architecture.getThings().add("Mansion");
            architecture.getThings().add("Bridge");
            architecture.setTemplate(TextTemplate.of(
                    TextColors.GOLD, TextStyles.BOLD, "Your next project shall be: ",
                    arg("theme").color(TextColors.RED).style(TextStyles.BOLD), " ",
                    arg("thing").color(TextColors.AQUA).style(TextStyles.BOLD), "."
            ));
            Category landscape = new Category();
            landscape.setName("Landscape");
            landscape.getThemes().add("");

            landscape.getThings().add("Mountain range");
            landscape.getThings().add("Cavern");
            landscape.getThings().add("Waterfall");
            landscape.getThings().add("Park");
            landscape.getThings().add("Mountain summit");
            landscape.getThings().add("Swamps");
            landscape.getThings().add("Forrest");
            landscape.getThings().add("Oasis");
            landscape.getThings().add("Lake");
            landscape.setTemplate(TextTemplate.of(
                    TextColors.GOLD, TextStyles.BOLD, "Your next project shall be: ",
                    arg("thing").color(TextColors.AQUA).style(TextStyles.BOLD), "."
            ));

            RootConfig config = new RootConfig();
            config.getCategories().add(landscape);
            config.getCategories().add(architecture);
            saveConfig(config, file.toPath());
        }
        rootConfig = loadConfig(file.toPath());
        registerCommands();
        logger.info("done");
    }

    private void registerCommands() {
        SimpleDispatcher rootCommand = new SimpleDispatcher();

        CommandSpec reload = CommandSpec.builder()
                .description(Text.of("Reloads the plugin"))
                .permission("wtfb.reload")
                .executor((src, args) -> {
                    rootConfig = loadConfig(new File(root.toFile(), "things.conf").toPath());
                    src.sendMessage(Text.of(TextColors.GOLD, "Successfully reloaded..."));
                    return CommandResult.success();
                })
                .build();


        CommandSpec suggestion = CommandSpec.builder()
                .description(Text.of("Suggests you what to build"))
                .permission("wtfb.suggest")
                .executor((src, args) -> {
                    Category category = getRandomElement(rootConfig.getCategories());
                    String theme = getRandomElement(category.getThemes());
                    String thing = getRandomElement(category.getThings());
                    Text text = category.getTemplate().apply(ImmutableMap.of("thing", thing, "theme", theme)).build();
                    src.sendMessage(text);
                    return CommandResult.success();
                })
                .build();

        CommandSpec inspire = CommandSpec.builder()
                .description(Text.of("Might send a link of a cute doggo.. or an architectural wonder"))
                .permission("wtfb.inspire")
                .executor((src, args) -> {
                    Set<String> set = new HashSet<>();
                    for (Category category : rootConfig.getCategories()) {
                        set.addAll(category.getUrls());
                    }
                    if (set.isEmpty()) {
                        src.sendMessage(Text.of(" - Not configured :(, blame admins"));
                        return CommandResult.empty();
                    }
                    String surl = getRandomElement(new ArrayList<>(set));
                    try {
                        URI url = new URI(surl);
                        Text text = Text.builder("Link: ").style(TextStyles.BOLD).color(TextColors.GREEN).append(
                                Text.builder(url.getHost())
                                        .color(TextColors.GOLD)
                                        .onClick(TextActions.openUrl(url.toURL()))
                                        .onHover(TextActions.showText(Text.of(surl)))
                                        .build()
                        ).build();
                        src.sendMessage(text);
                    } catch (MalformedURLException | URISyntaxException e) {
                        throw new RuntimeException("Invalid url format " + surl, e);
                    }
                    return CommandResult.success();
                })
                .build();

        rootCommand.register(reload, "reload");
        rootCommand.register(suggestion, "suggest");
        rootCommand.register(inspire, "inspire");
        Sponge.getCommandManager().register(this, rootCommand, "wtfb");
        Sponge.getCommandManager().register(this, suggestion, "ihaveliterallynoideawhatthefuckshouldibuildnext");


    }



    private <T> T getRandomElement(List<T> o) {
        return o.get(secureRandom.nextInt(o.size()));
    }

    private void saveConfig(RootConfig config, Path path) {
        try {
            if (!path.toFile().getParentFile().exists()) {
                Files.createDirectories(path.toFile().getParentFile().toPath());
            }
            ObjectMapper.BoundInstance configMapper = ObjectMapper.forObject(config);
            HoconConfigurationLoader hcl = HoconConfigurationLoader.builder().setPath(path).build();
            SimpleConfigurationNode scn = SimpleConfigurationNode.root();
            configMapper.serialize(scn);
            hcl.save(scn);
        } catch (Exception e) {
            throw new RuntimeException("Could not write file. ", e);
        }
    }

    private RootConfig loadConfig(Path path) {
        try {
            logger.info("Loading config...");
            ObjectMapper<RootConfig> mapper = ObjectMapper.forClass(RootConfig.class);
            HoconConfigurationLoader hcl = HoconConfigurationLoader.builder().setPath(path).build();
            return mapper.bind(new RootConfig()).populate(hcl.load());
        } catch (Exception e) {
            throw new RuntimeException("Could not load file " + path, e);
        }
    }
}
