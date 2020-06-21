package pri.vvoyek.wsb.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Programmer extends Employee {
    private final List<Technology> skills = new ArrayList<>();
    private final int bugRate;
    private final int delayRate;

    public Programmer(String name, Technology[] skills, int bugRate, int delayRate) {
        super(name);
        Collections.addAll(this.skills, skills);
        this.bugRate = bugRate;
        this.delayRate = delayRate;
    }

    public Programmer(String name, double salary, Technology[] skills, int bugRate, int delayRate) {
        super(name, salary);
        Collections.addAll(this.skills, skills);
        this.bugRate = bugRate;
        this.delayRate = delayRate;
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

    public boolean onTime() {
        return Game.nextInt(100) >= delayRate;
    }

    public static Programmer getNewProgrammer(int bugRate, int delayRate) {
        String name = getNextName();
        Technology[] skills = Technology.getRandomTechnologies();
        return new Programmer(name, skills, bugRate, delayRate);
    }
}
