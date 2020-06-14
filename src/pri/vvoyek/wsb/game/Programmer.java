package pri.vvoyek.wsb.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Programmer extends Employee {
    private final List<Technology> skills = new ArrayList<>();

    public Programmer(String name, Technology[] skills) {
        super(name);
        Collections.addAll(this.skills, skills);
    }

    public Programmer(String name, Technology t) {
        super(name);
        this.skills.add(t);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        for (Technology t : skills) {
            sb.append(" ");
            sb.append(t);
        }
        return sb.toString();
    }

    public boolean hasSkill(Technology t) {
        return skills.contains(t);
    }

    public void work(Project p) {
        p.doTheJob(this);
    }

    public static Programmer getNewProgrammer() {
        List<Technology> skills = new ArrayList<>();
        String name = getNextName();
        int skillsCount = 1 + Game.nextInt(Technology.values().length);
        for (int i = 0; i < skillsCount; i++) {
            Technology t = Technology.values()[Game.nextInt(Technology.values().length)];
            if (skills.contains(t))
                continue;
            skills.add(t);
        }
        return new Programmer(name, skills.toArray(new Technology[0]));
    }
}