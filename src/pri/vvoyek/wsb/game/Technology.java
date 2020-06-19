package pri.vvoyek.wsb.game;

import java.util.ArrayList;
import java.util.List;

public enum Technology {
    FRONT_END, BACK_END, DATABASE, MOBILE, WORDPRESS, PRESTASHOP;

    public static Technology[] getRandomTechnologies() {
        List<Technology> list = new ArrayList<>();
        int count = 1 + Game.nextInt(Technology.values().length);
        for (int i = 0; i < count; i++) {
            Technology t = Technology.values()[Game.nextInt(Technology.values().length)];
            if (list.contains(t))
                continue;
            list.add(t);
        }
        return list.toArray(new Technology[0]);
    }
}
