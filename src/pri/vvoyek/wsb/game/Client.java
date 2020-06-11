package pri.vvoyek.wsb.game;

import java.util.Random;

public class Client {
    private static int lastId = 0;

    public enum Type {
        EASY, STRICT, EVIL
    }
    public final Type type;
    private final int id;

    private Client(Type type) {
        this.type = type;
        this.id = ++lastId;
    }

    public String toString() {
        return id + "_" + type.toString();
    }

    public static Client getRandomClient() {

        Random r = new Random();
        Type[] ta = Type.values();
        int i = r.nextInt(ta.length);
        Type t = ta[i];
        return new Client(t);

        //return new Client(Type.values()[new Random().nextInt(Type.values().length)]);
    }
}
