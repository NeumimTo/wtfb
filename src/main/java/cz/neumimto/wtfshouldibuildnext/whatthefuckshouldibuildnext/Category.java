package cz.neumimto.wtfshouldibuildnext.whatthefuckshouldibuildnext;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;


import java.util.ArrayList;
import java.util.List;

import static org.spongepowered.api.text.TextTemplate.arg;

@ConfigSerializable
public class Category {

    @Setting("name")
    private String name;

    @Setting("themes")
    private List<String> themes = new ArrayList<>();

    @Setting("things")
    private List<String> things = new ArrayList<>();

    @Setting("urls")
    private List<String> urls = new ArrayList<>();

    @Setting
    private TextTemplate template;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getThemes() {
        return themes;
    }

    public void setThemes(List<String> themes) {
        this.themes = themes;
    }

    public void setThings(List<String> things) {
        this.things = things;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public void setTemplate(TextTemplate template) {
        this.template = template;
    }

    public List<String> getThings() {
        return things;
    }

    public List<String> getUrls() {
        return urls;
    }

    public TextTemplate getTemplate() {
        return template;
    }
}
