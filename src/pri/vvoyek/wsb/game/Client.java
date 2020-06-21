package pri.vvoyek.wsb.game;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private static int lastId = 0;

    public enum Type {
        EASY, STRICT, EVIL
    }
    public final Type type;
    private final int id;
    public final List<Project> projects = new ArrayList<>();

    private Client(Type type) {
        this.type = type;
        this.id = ++lastId;
    }

    public String toString() {
        return id + "_" + type.toString();
    }

    public void payBills(LocalDate today) {
        for (Project p : projects) {
            if (!p.wasFullyPaid() && p.paymentDate.equals(today)) {
                double payment = p.getPrice();
                p.contractor.receivePayment(payment, p.name);
                p.payment = payment;
            }
        }
    }

    public static Client getRandomClient() {
        Type[] ta = Type.values();
        int i = Game.nextInt(ta.length);
        Type t = ta[i];
        return new Client(t);
    }
}
