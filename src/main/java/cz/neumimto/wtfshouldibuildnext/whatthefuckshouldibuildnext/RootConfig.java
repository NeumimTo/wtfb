package cz.neumimto.wtfshouldibuildnext.whatthefuckshouldibuildnext;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public class RootConfig {

    @Setting("categories")
    private List<Category> categories = new ArrayList<>();

    public List<Category> getCategories() {
        return categories;
    }
}
