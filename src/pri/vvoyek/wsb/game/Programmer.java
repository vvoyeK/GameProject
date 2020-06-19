package pri.vvoyek.wsb.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Programmer extends Employee {
    private final List<Technology> skills = new ArrayList<>();
    public final int bugRate;

    public Programmer(String name, Technology[] skills, int bugRate) {
        super(name);
        Collections.addAll(this.skills, skills);
        this.bugRate = bugRate;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        sb.append(" PROGRAMISTA");
        for (Technology t : skills) {
            sb.append(" ");
            sb.append(t);
        }
        return sb.toString();
    }

    public boolean hasSkill(Technology t) {
        return skills.contains(t);
    }

    public boolean bugFree() {
        return Game.nextInt(100) >= bugRate;
    }

    public void work(Project p) {
        p.doTheJob(this);
    }

    public static Programmer getNewProgrammer(int bugRate) {
        String name = getNextName();
        Technology[] skills = Technology.getRandomTechnologies();
        return new Programmer(name, skills, bugRate);
    }
}
